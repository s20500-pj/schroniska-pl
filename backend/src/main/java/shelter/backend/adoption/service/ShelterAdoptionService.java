package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.exception.AdoptionException;
import shelter.backend.utils.exception.MessageNotSendException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterAdoptionService implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalMapper animalMapper;
    private final AdoptionMapper adoptionMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public AnimalDto beginRealAdoption(Long animalId) {
        log.debug("[beginRealAdoption] :: adoptionId: {}", animalId);
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            if (notWaitForRealAdoption(animal)) {
                registerRealAdoption(animal);
                return animalMapper.toDto(animal);
            } else {
                log.info("Animal awaits for real adoption. Animal id: {}", animalId);
                throw new AdoptionException("Zwierzę oczekuję na adopcję realną");
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
                            adoption.getAnimal().getShelter().getShelterName(), adoption.getValidUntil().toString());
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
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.DECLINED);
        adoptionRepository.save(adoption);
        try { //TODO
            emailService.sendAdoptionCancellation();
        } catch (MessageNotSendException e) {

        }
        return adoption.toDto();
    }

    @Override
    public List<AdoptionDto> getAll() {
        List<Adoption> adoptionList = adoptionRepository.findAll();
        return adoptionMapper.toDtoList(adoptionList);
    }

    @Override
    public List<AdoptionDto> getUserAdoptions(Long userId) {
        List<Adoption> adoptionList = adoptionRepository.findAdoptionByUserId(userId);
        return adoptionMapper.toDtoList(adoptionList);
    }

    private void registerRealAdoption(Animal animal) {
        User user = getUser();
        Adoption adoption = Adoption.builder()
                .adoptionType(AdoptionType.REAL)
                .adoptionStatus(AdoptionStatus.REQUEST_REVIEW)
                .animal(animal)
                .user(user)
                .build();
        adoptionRepository.save(adoption);
        animal.getAdoptions().add(adoption);
        animalRepository.save(animal);
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        User user = userRepository.findUserByEmail(username);
        return user;
    }

    private boolean notWaitForRealAdoption(Animal animal) {
        return animal.getAdoptions().stream().noneMatch(adoption -> adoption.getAdoptionType().equals(AdoptionType.REAL));
    }
}

//TODO scheduler do usuwania olanych adopcji

//TODO shendlować jakoś case gdy juź jest zadaptowany realnie, co zrobic z Virtual ->(np. wyczyscic, gdy juz potwierdz to w schronisku ->
// jakis endpoint zrobic na potwierzenie ze zostal zaadaptowany)
