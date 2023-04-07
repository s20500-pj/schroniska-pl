package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.rest.model.specification.AdoptionSpecification;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.exception.AdoptionException;
import shelter.backend.utils.exception.MessageNotSendException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterAdoptionService implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AdoptionMapper adoptionMapper;
    private final EmailService emailService;
    private final long validUntil = 2L;

    @Override
    @Transactional
    public AdoptionDto beginRealAdoption(Long animalId) {
        log.debug("[beginRealAdoption] :: adoptionId: {}", animalId);
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            if (notAlreadyAdopted(animal)) {
                return adoptionMapper.toDto(registerRealAdoption(animal));
            } else {
                log.info("Animal awaits for real adoption. Animal id: {}", animalId);
                throw new AdoptionException("Zwierzę oczekuję już na adopcję realną");
            }

        } else throw new AdoptionException("Zwierzę nie istnieje dla wybranego id");
    }

    @Override
    public List<AdoptionDto> sendInvitationRealAdoption(List<Long> adoptionIds) {
        log.debug("[sendInvitationRealAdoption] :: adoptionIds: {}", adoptionIds);
        List<AdoptionDto> adoptionDtoList = new ArrayList<>();
        adoptionIds.forEach(adoptionId -> {
            Optional<Adoption> adoptionOpt = adoptionRepository.findById(adoptionId);
            adoptionOpt.ifPresent(adoption -> {
                sendInvitationEmail(adoption);
                adoptionRepository.save(adoption);
                adoptionDtoList.add(adoption.toDto());
            });
        });
        List<AdoptionDto> manualAdoptions = adoptionDtoList.stream()
                .filter(adoptionDto -> adoptionDto.getAdoptionStatus() == AdoptionStatus.REQUIRES_MANUAL_INVITATION)
                .toList();
        if (!manualAdoptions.isEmpty()) {
            String userMails = manualAdoptions.stream()
                    .map(AdoptionDto::getUser)
                    .map(UserDto::getEmail)
                    .collect(Collectors.joining(", "));
            throw new MessageNotSendException("Problem z wysłaniem maila. Wymagany jest kontakt z użytkownikiem: " + userMails);
        } else {
            return adoptionDtoList;
        }
    }

    private void sendInvitationEmail(Adoption adoption) {
        try {
            //TODO add preference for valudDate
            adoption.setValidUntil(LocalDate.now().plusWeeks(validUntil));
            emailService.sendAdoptionInvitation(adoption.getUser().getEmail(),
                    adoption.getAnimal().getShelter().getShelterName(), adoption.getValidUntil().toString(),
                    adoption.getId());
            adoption.setAdoptionStatus(AdoptionStatus.SHELTER_INVITED);
        } catch (MessageNotSendException e) {
            log.warn("Problem with sending invitation email to the user: {}", adoption.getUser().getEmail());
            adoption.setValidUntil(null);
            adoption.setAdoptionStatus(AdoptionStatus.REQUIRES_MANUAL_INVITATION);
        }
    }

    @Override
    public List<AdoptionDto> acceptManualInvitedAdoption(List<Long> adoptionIds) {
        log.debug("[acceptManualInvitedAdoption] :: adoptionIds: {}", adoptionIds);
        List<AdoptionDto> adoptionDtoList = new ArrayList<>();
        adoptionIds.forEach(adoptionId -> {
            Optional<Adoption> adoptionOpt = adoptionRepository.findById(adoptionId);
            adoptionOpt.ifPresent(adoption -> {
                if (adoption.getAdoptionStatus() == AdoptionStatus.REQUIRES_MANUAL_INVITATION) {
                    adoption.setAdoptionStatus(AdoptionStatus.SHELTER_INVITED);
                    //TODO add preference for valudDate
                    adoption.setValidUntil(LocalDate.now().plusWeeks(validUntil));
                    adoptionDtoList.add(adoption.toDto());
                }
            });
        });
        return adoptionDtoList;
    }

    @Override
    @Transactional
    public AdoptionDto confirmVisit(Long adoptionId) {
        log.debug("[confirmVisit] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.VISITED);
        adoptionRepository.save(adoption);
        updateOtherAdoptionsToPendingStatus(adoption);
        return adoption.toDto();
    }

    private void updateOtherAdoptionsToPendingStatus(Adoption adoption) {
        adoption.getAnimal().getAdoptions().stream()
                .filter(adoptionUpdate -> adoptionUpdate.getAdoptionStatus() == AdoptionStatus.REQUIRES_MANUAL_INVITATION
                        || adoptionUpdate.getAdoptionStatus() == AdoptionStatus.SHELTER_INVITED)
                .forEach(adoptionUpdate -> {
                    AdoptionStatus adoptionStatus = adoptionUpdate.getAdoptionStatus();
                    if (adoptionStatus == AdoptionStatus.REQUIRES_MANUAL_INVITATION) {
                        adoptionUpdate.setAdoptionStatus(AdoptionStatus.PENDING);
                    } else {
                        adoptionUpdate.setAdoptionStatus(AdoptionStatus.PENDING_SHELTER_INVITED);
                    }
                    adoptionUpdate.setValidUntil(null);
                    adoptionRepository.save(adoptionUpdate);
                    log.info("Status suspended for adoption, id: {}, username: {}", adoption.getId(),
                            adoption.getUser().getEmail());
                    User user = userRepository.findUserById(adoptionUpdate.getUser().getId());
                    User shelter = getUser();
                    try {
                        emailService.sendAdoptionSuspension(user.getEmail(), shelter.getShelterName(),
                                adoptionUpdate.getId());
                    } catch (MessageNotSendException e) {
                        log.warn("Adoption suspension mail can't be send. Reason: {}", e.getMessage());
                    }
                });
    }

    @Override
    public AdoptionDto extendTimeForAdoption(Long adoptionId, Long plusWeeks) {
        log.debug("[extendTimeForAdoption] :: adoptionId: {}, plusWeeks: {}", adoptionId, plusWeeks);
        LocalDate today = LocalDate.now();
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        if (adoption.getValidUntil().isBefore(today)) {
            adoption.setValidUntil(today.plusWeeks(plusWeeks));
        } else {
            adoption.setValidUntil(adoption.getValidUntil().plusWeeks(plusWeeks));
        }
        adoptionRepository.save(adoption);
        log.info("Adoption with id: {} for username: {} extended by {} week(s)",
                adoption.getId(), adoption.getUser().getEmail(), plusWeeks);
        return adoption.toDto();
    }

    @Override
    public AdoptionDto declineRealAdoption(Long adoptionId, boolean declineAll) {
        log.debug("[declineAdoption] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        User currentUser = getUser();
        if (currentUser.getUserType() == UserType.PERSON && !isEntitled(currentUser, adoptionId)) {
            // check if is security breach
            log.warn("User called for adoption not assigned to the user! User details: {}, Adoption details:{}",
                    currentUser, adoption);
            throw new AdoptionException("Adopcja o podanym ID nie isnieje");
        }
        if (!declineAll) {
            updateOtherAdoptionsToPreviousStatus(adoption);
        }
        adoption.setAdoptionStatus(AdoptionStatus.DECLINED);
        adoptionRepository.save(adoption);
        try {
            emailService.sendAdoptionCancellation(adoption.getUser().getEmail(), adoption.getUser().getId());
            log.info("Adoption id: {}, has been cancelled", adoptionId);
        } catch (MessageNotSendException e) {
            log.warn("Adoption cancellation email can't be send. Reason: {}", e.getMessage());
            throw new MessageNotSendException("Adopcja (id: " + adoptionId + ") została anulowana, jednak nie można wysłać wiadomości mailowej.");
        }
        return adoption.toDto();
    }

    private boolean isEntitled(User currentUser, Long adoptionId) {
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        return adoption.getUser().equals(currentUser);
    }

    private void updateOtherAdoptionsToPreviousStatus(Adoption adoption) {
        Animal animal = adoption.getAnimal();
        animal.getAdoptions().stream()
                .filter(adoptionUpdate -> adoptionUpdate.getAdoptionStatus() == AdoptionStatus.PENDING
                        || adoptionUpdate.getAdoptionStatus() == AdoptionStatus.PENDING_SHELTER_INVITED)
                .forEach(adoptionUpdate -> {
                    AdoptionStatus adoptionStatus = adoptionUpdate.getAdoptionStatus();
                    {
                        if (adoptionStatus == AdoptionStatus.PENDING) {
                            adoptionUpdate.setAdoptionStatus(AdoptionStatus.REQUIRES_MANUAL_INVITATION);
                        } else {
                            adoptionUpdate.setAdoptionStatus(AdoptionStatus.SHELTER_INVITED);
                        }
                        sendInvitationEmail(adoptionUpdate);
                        adoptionRepository.save(adoptionUpdate);
                        log.info("Status resumed for adoptionID: {}, username: {}", adoption.getId(),
                                adoption.getUser().getEmail());
                    }

                });
    }

    @Override
    public List<AdoptionDto> getAll() {
        List<Adoption> adoptionList;
        User user = getUser();
        log.debug("[getAll] :: userId: {}, userName: {}", user.getId(), user.getEmail());
        if (user.getUserType() == UserType.SHELTER) {
            adoptionList = adoptionRepository.findAdoptionByAnimal_ShelterId(user.getId());
        } else {
            adoptionList = adoptionRepository.findAll();
        }
        return adoptionMapper.toDtoList(adoptionList);
    }

    @Override
    public List<AdoptionDto> getUserAdoptions() {
        User user = getUser();
        log.debug("[getUserAdoptions] :: userId: {}, userMail: {}", user.getId(), user.getEmail());
        List<Adoption> adoptionList = adoptionRepository.findAdoptionByUserId(user.getId());
        return adoptionMapper.toDtoList(adoptionList);
    }

    @Override
    public List<AdoptionDto> search(Map<String, String> searchParams) {
        log.debug("[search] :: searchParams: {}", searchParams);
        AdoptionSpecification adoptionSpecification = new AdoptionSpecification(searchParams);
        List<Adoption> adoptionList = adoptionRepository.findAll(adoptionSpecification);
        User currentUser = getUser();
        if (currentUser.getUserType() == UserType.SHELTER) {
            List<Adoption> adoptionSpecificForTheShelter = adoptionList.stream()
                    .filter(adoption -> Objects.equals(adoption.getAnimal().getShelter().getId(), currentUser.getId()))
                    .toList();
            return adoptionMapper.toDtoList(adoptionSpecificForTheShelter);
        } else {
            return adoptionMapper.toDtoList(adoptionList);
        }
    }

    @Override
    @Transactional
    public AdoptionDto finalizeRealAdoption(Long id) {
        log.debug("[finalizeAdoption] :: adoptionId: {}", id);
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.ADOPTED);
        Animal animal = adoption.getAnimal();
        animal.setAnimalStatus(AnimalStatus.ADOPTED);
        animalRepository.save(animal);
        adoptionRepository.save(adoption);
        animal.getAdoptions().stream().filter(adoptionUpdate -> adoptionUpdate.getAdoptionType() == AdoptionType.REAL)
                .forEach(adoptionUpdate -> declineRealAdoption(adoptionUpdate.getId(), true));
        return adoption.toDto();
    }

    @Override
    public AdoptionDto getAdoptionById(Long id) {
        log.debug("[finalizeAdoption] :: adoptionId: {}, userName: {}", id, getUser().getEmail());
        Adoption adoption = adoptionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        return adoption.toDto();
    }

    private Adoption registerRealAdoption(Animal animal) {
        User user = getUser();
        Adoption adoption = Adoption.builder()
                .adoptionType(AdoptionType.REAL)
                .adoptionStatus(AdoptionStatus.REQUEST_REVIEW)
                .user(user)
                .build();
        adoption.addAnimal(animal);
        adoptionRepository.save(adoption);
        animalRepository.save(animal);
        return adoption;
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);
    }

    private boolean notAlreadyAdopted(Animal animal) {
        return animal.getAdoptions().stream().noneMatch(adoption -> adoption.getAdoptionStatus() == AdoptionStatus.ADOPTED ||
                adoption.getAdoptionStatus() == AdoptionStatus.VISITED) ||
                animal.getAnimalStatus() == AnimalStatus.DEAD ||
                animal.getAnimalStatus() == AnimalStatus.DELETED ||
                animal.getAnimalStatus() == AnimalStatus.UNKNOWN;
    }

    @Async
    @Scheduled(cron = "5 0 * * * ?")   //check every day at 00:05 AM
    @Transactional
    public void deleteExpiredRealAdoptions() {
        log.debug("adoption scheduler started");
        LocalDate today = LocalDate.now();
        List<Adoption> adoptionList = adoptionRepository.findAll();
        List<Adoption> expiredAdoptions = adoptionList.stream()
                .filter(adoption -> adoption.getValidUntil() != null)
                .filter(adoption -> adoption.getValidUntil().isBefore(today))
                .toList();
        for (Adoption adoption : expiredAdoptions) {
            declineRealAdoption(adoption.getId(), true);
        }
        log.debug("adoption scheduler finished");
    }
}

//TODO shendlować jakoś case gdy juź jest zadaptowany realnie, co zrobic z Virtual ->(np. wyczyscic, gdy juz potwierdz to w schronisku ->
// jakis endpoint zrobic na potwierzenie ze zostal zaadaptowany)
