package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.exception.AdoptionException;
import shelter.backend.utils.exception.MessageNotSendException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ShelterRealAdoptionService extends ShelterAdoptionService implements RealAdoptionService {

    private final ActivityRepository activityRepository;

    private static final long VALID_UNTIL = 2L; //TODO add this to Preferences

    public ShelterRealAdoptionService(AdoptionRepository adoptionRepository, AnimalRepository animalRepository,
                                      UserRepository userRepository, AdoptionMapper adoptionMapper,
                                      EmailService emailService, ActivityRepository activityRepository) {
        super(adoptionRepository, animalRepository, userRepository, adoptionMapper, emailService);
        this.activityRepository = activityRepository;
    }

    @Override
    @Transactional
    public AdoptionDto2 beginRealAdoption(Long animalId) {
        log.debug("[beginRealAdoption] :: animalId: {}", animalId);
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            if (notAlreadyAdopted(animal)) {
                return adoptionMapper.toDto2(registerRealAdoption(animal));
            } else {
                log.info("Animal awaits for real adoption. Animal id: {}", animalId);
                throw new AdoptionException("Zwierzę oczekuję już na adopcję realną");
            }

        } else throw new AdoptionException("Zwierzę nie istnieje dla wybranego id");
    }

    @Override
    public AdoptionDto2 sendInvitationRealAdoption(Long adoptionId) {
        log.debug("[sendInvitationRealAdoption] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId).orElseThrow(() -> new AdoptionException("Nie znaleziono adopcji o podanym id"));
        sendInvitationEmail(adoption);
        adoptionRepository.save(adoption);

        if (adoption.getAdoptionStatus().equals(AdoptionStatus.REQUIRES_MANUAL_INVITATION)) {
            String userMail = adoption.getUser().getEmail();
            throw new MessageNotSendException("Problem z wysłaniem maila. Wymagany jest kontakt z użytkownikiem: " + userMail);
        } else {
            return adoption.toDto2(adoption.getAnimal().toSimpleDto());
        }
    }

    private void sendInvitationEmail(Adoption adoption) {
        try {
            //TODO add preference for valudDate
            adoption.setValidUntil(LocalDate.now().plusWeeks(VALID_UNTIL));
            emailService.sendAdoptionInvitation(adoption.getUser().getEmail(),
                    adoption.getAnimal().getShelter().getShelterName(), adoption.getValidUntil().toString(),
                    adoption.getId(), adoption.getAnimal().getName(), adoption.getUser().getFirstName());
            adoption.setAdoptionStatus(AdoptionStatus.SHELTER_INVITED);
        } catch (MessageNotSendException e) {
            log.warn("Problem with sending invitation email to the user: {}", adoption.getUser().getEmail());
            adoption.setValidUntil(null);
            adoption.setAdoptionStatus(AdoptionStatus.REQUIRES_MANUAL_INVITATION);
        }
    }

    @Override
    public AdoptionDto2 acceptManualInvitedAdoption(Long adoptionId) {
        log.debug("[acceptManualInvitedAdoption] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId).orElseThrow(() -> new AdoptionException("Nie znaleziono adopcji o podanym id"));
        if (adoption.getAdoptionStatus() == AdoptionStatus.REQUIRES_MANUAL_INVITATION) {
            adoption.setAdoptionStatus(AdoptionStatus.SHELTER_INVITED);
            //TODO add preference for valudDate
            adoption.setValidUntil(LocalDate.now().plusWeeks(VALID_UNTIL));
        }
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
    }

    @Override
    @Transactional
    public AdoptionDto2 confirmVisit(Long adoptionId) {
        log.debug("[confirmVisit] :: adoptionId: {}", adoptionId);
        Adoption adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setAdoptionStatus(AdoptionStatus.VISITED);
        adoption.setValidUntil(null);
        adoptionRepository.save(adoption);
        updateOtherAdoptionsToPendingStatus(adoption);
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
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
                                adoptionUpdate.getId(), adoption.getAnimal().getName(), adoption.getUser().getFirstName());
                    } catch (MessageNotSendException e) {
                        log.warn("Adoption suspension mail can't be send. Reason: {}", e.getMessage());
                    }
                });
    }

    @Override
    public AdoptionDto2 extendTimeForAdoption(Long adoptionId, Long plusWeeks) {
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
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
    }

    @Override
    public AdoptionDto2 declineRealAdoption(Long adoptionId, boolean declineAll) {
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
        adoption.setValidUntil(null);
        adoption.setAdoptionStatus(AdoptionStatus.DECLINED);
        adoptionRepository.save(adoption);
        try {
            emailService.sendAdoptionCancellation(adoption.getUser().getEmail(), adoption.getId(), adoption.getAnimal().getName(), adoption.getUser().getFirstName());
            log.info("Adoption id: {}, has been cancelled", adoptionId);
        } catch (MessageNotSendException e) {
            log.warn("Adoption cancellation email can't be send. Reason: {}", e.getMessage());
            throw new MessageNotSendException("Adopcja (id: " + adoptionId + ") została anulowana, jednak nie można wysłać wiadomości mailowej.");
        }
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
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
    @Transactional
    public AdoptionDto2 finalizeRealAdoption(Long id) {
        log.debug("[finalizeAdoption] :: adoptionId: {}", id);
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        adoption.setValidUntil(null);
        adoption.setAdoptionStatus(AdoptionStatus.ADOPTED);
        Animal animal = adoption.getAnimal();
        animal.setAnimalStatus(AnimalStatus.ADOPTED);
        List<Activity> animalActivities = animal.getActivities();
        animal.setActivities(null);
        if (!animalActivities.isEmpty()) {
            activityRepository.deleteAll(animalActivities);
            animalActivities.forEach((activity) -> emailService.sendActivityCancellation(activity.getUser().getEmail(),
                    activity.getAnimal().getName(), activity.getActivityTime().toLocalDate().toString(),
                    activity.getUser().getFirstName()));
        }
        animalRepository.save(animal);
        adoptionRepository.save(adoption);
        animal.getAdoptions().stream().filter(adoptionUpdate -> !adoptionUpdate.getId().equals(id))//don't cancel current adoption!
                .filter(adoptionUpdate -> adoptionUpdate.getAdoptionType() == AdoptionType.REAL)
                .forEach(adoptionUpdate -> declineRealAdoption(adoptionUpdate.getId(), true));
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
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

    @Scheduled(cron = "0 5 0 * * ?")   //check every day at 00:05 AM
    @Transactional
    public void deleteExpiredRealAdoptions() {
        log.debug("real adoption scheduler started");
        LocalDate today = LocalDate.now();
        List<Adoption> adoptionList = adoptionRepository.findAdoptionByAdoptionType(AdoptionType.REAL);
        List<Adoption> expiredAdoptions = adoptionList.stream()
                .filter(adoption -> adoption.getValidUntil() != null)
                .filter(adoption -> adoption.getValidUntil().isBefore(today))
                .toList();
        for (Adoption adoption : expiredAdoptions) {
            declineRealAdoption(adoption.getId(), true);
        }
        log.debug("real adoption scheduler finished");
    }
}

