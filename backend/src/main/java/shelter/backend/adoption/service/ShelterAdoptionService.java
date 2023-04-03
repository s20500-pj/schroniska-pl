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
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.rest.model.mapper.AnimalMapper;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterAdoptionService implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AdoptionMapper adoptionMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public AdoptionDto beginRealAdoption(Long animalId) {
        log.debug("[beginRealAdoption] :: adoptionId: {}", animalId);
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            if (notWaitForRealAdoption(animal)) {
                return adoptionMapper.toDto(registerRealAdoption(animal));
            } else {
                log.info("Animal awaits for real adoption. Animal id: {}", animalId);
                throw new AdoptionException("Zwierzę oczekuję już na adopcję realną");
            }

        } else throw new AdoptionException("Zwierzę nie istnieje dla wybranego id");
    }

    @Override
    public List<AdoptionDto> approveRealAdoption(List<Long> adoptionIds) {
        log.debug("[approveRealAdoption] :: adoptionIds: {}", adoptionIds);
        List<AdoptionDto> adoptionDtoList = new ArrayList<>();
        adoptionIds.forEach(adoptionId -> {
            Optional<Adoption> adoptionOpt = adoptionRepository.findById(adoptionId);
            adoptionOpt.ifPresent(adoption -> {
                adoption.setAdoptionStatus(AdoptionStatus.REQUIRES_EMAIL_INVITATION);
                adoptionRepository.save(adoption);
                try {
                    //TODO add preference for valudDate
                    adoption.setValidUntil(LocalDate.now().plusWeeks(1));
                    emailService.sendAdoptionInvitation(adoption.getUser().getEmail(),
                            adoption.getAnimal().getShelter().getShelterName(), adoption.getValidUntil().toString(),
                            adoption.getId());
                    adoption.setAdoptionStatus(AdoptionStatus.ACCEPTED);
                } catch (MessageNotSendException e) {
                    log.error("Problem with sending invitation email to the user: {}", adoption.getUser().getEmail());
                    adoption.setValidUntil(null);
                    adoption.setAdoptionStatus(AdoptionStatus.REQUIRES_MANUAL_INVITATION);
                    throw new MessageNotSendException("Problem z wysłaniem maila do użytkownika. Wymagany jest kontakt z ");
                }
                adoptionDtoList.add(adoption.toDto());
                adoptionRepository.save(adoption);
            });
        });
        return adoptionDtoList;
    }

    @Override
    public List<AdoptionDto> acceptManualInvitedAdoption(List<Long> adoptionIds) {
        log.debug("[acceptManualInvitedAdoption] :: adoptionIds: {}", adoptionIds);
        List<AdoptionDto> adoptionDtoList = new ArrayList<>();
        adoptionIds.forEach(adoptionId -> {
            Optional<Adoption> adoptionOpt = adoptionRepository.findById(adoptionId);
            adoptionOpt.ifPresent(adoption -> {
                if (adoption.getAdoptionStatus() == AdoptionStatus.REQUIRES_MANUAL_INVITATION) {
                    adoption.setAdoptionStatus(AdoptionStatus.ACCEPTED);
                    //TODO add preference for valudDate
                    adoption.setValidUntil(LocalDate.now().plusWeeks(1));
                    adoptionDtoList.add(adoption.toDto());
                }
            });
        });
        return adoptionDtoList;
    }

    @Override
    public AdoptionDto declineAdoption(Long adoptionId) {
        log.debug("[declineAdoption] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.DECLINED);
        adoptionRepository.save(adoption);
        try {
            emailService.sendAdoptionCancellation(adoption.getUser().getEmail(), adoption.getUser().getId());
            log.debug("Adopcja o nr. id: {}, została anulowana", adoptionId);
        } catch (MessageNotSendException e) {
            throw new MessageNotSendException("Adopcja (id: " + adoptionId + ") została anulowana, jednak nie można wysłać wiadomości mailowej.");
        }
        return adoption.toDto();
    }

    @Override
    public List<AdoptionDto> getAllForSpecificShleter() {
        List<Adoption> adoptionList = new ArrayList<>();
        User user = getUser();
        log.debug("[getAllForSpecifigShleter] :: shelterId: {}, shelterMail: {}", user.getId(), user.getEmail());
        if (user.getUserType() == UserType.SHELTER) {
            adoptionList = adoptionRepository.findAdoptionByAnimal_ShelterId(user.getId());
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
    public AdoptionDto finalizeAdoption(Long id) {
        log.debug("[finalizeAdoption] :: adoptionId: {}", id);
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.ADOPTED);
        adoptionRepository.save(adoption);
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

    private boolean notWaitForRealAdoption(Animal animal) {
        return animal.getAdoptions().stream().noneMatch(adoption -> adoption.getAdoptionType().equals(AdoptionType.REAL));
    }

    @Async
    @Scheduled(cron = "5 0 * * * ?")   //check every day at 00:05 AM
    @Transactional
    public void deleteExpiredAdoptions() {
        log.debug("adoption scheduler started");
        LocalDate today = LocalDate.now();
        List<Adoption> adoptionList = adoptionRepository.findAll();
        List<Adoption> expiredAdoptions = adoptionList.stream()
                .filter(adoption -> adoption.getValidUntil().isBefore(today))
                .toList();
        for (Adoption adoption : expiredAdoptions) {
            declineAdoption(adoption.getId());
        }
        log.debug("adoption scheduler finished");
    }
}

//TODO shendlować jakoś case gdy juź jest zadaptowany realnie, co zrobic z Virtual ->(np. wyczyscic, gdy juz potwierdz to w schronisku ->
// jakis endpoint zrobic na potwierzenie ze zostal zaadaptowany)
