package shelter.backend.activity.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.animals.service.ShelterAnimalService;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.ActivityMapper;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.rest.model.specification.ActivitySpecification;
import shelter.backend.rest.model.specification.AnimalSpecification;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.constants.SpecificationConstants;
import shelter.backend.utils.exception.ActivityException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterActivityService implements ActivityService {

    private final UserRepository userRepository;

    private final ActivityRepository activityRepository;

    private final AnimalRepository animalRepository;

    private final ActivityMapper activityMapper;

    private final AnimalMapper animalMapper;

    private final LocalTime defaultTimeOfActivity = LocalTime.of(16, 0); //todo add to Preferences

    private final String ACTIVITY_TIME_FIELD = "activityTime";

    @Override
    @Transactional
    public ActivityDto2 registerActivity(ActivityRegisterReq activityRegisterReq) {
        log.debug("[registerActivity] :: activity: {}, userName: {}", activityRegisterReq, getUser().getEmail());
        Animal animal = animalRepository.findAnimalById(activityRegisterReq.getAnimalId());
        if (animal != null) {
            //TODO maybe also try to use google maps api to count the distance. up tp 50km for instance. consider adding this to Preference
            if (hasFreeTime(animal, activityRegisterReq.getActivityDate())) {
                return activityMapper.toDto2(persistActivity(animal, activityRegisterReq));
            } else {
                log.info("Animal awaits for activity already. Animal id: {}. Activity Request: {}", animal.getId(), activityRegisterReq);
                throw new ActivityException("Podany termin aktywności jest już zajęty");
            }

        } else throw new EntityNotFoundException("Zwierzę nie istnieje dla wybranego id");
    }

    private Activity persistActivity(Animal animal, ActivityRegisterReq activityRegister) {
        User currentUser = getUser();
        Activity activity = Activity.builder()
                .activityType(activityRegister.getActivityType())
                .activityTime(LocalDateTime.of(activityRegister.getActivityDate(), defaultTimeOfActivity))
                .user(currentUser)
                .build();
        activity.addAnimal(animal);
        activityRepository.save(activity);
        animalRepository.save(animal);
        return activity;
    }

    private boolean hasFreeTime(Animal animal, LocalDate activityDate) {
        return animal.getActivities().stream()
                .noneMatch(activity -> activity.getActivityTime().toLocalDate().isEqual(activityDate));
    }

    @Override
    public void deleteActivity(Long activityId) {
        log.debug("[deleteActivity] :: activityId: {}", activityId);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Aktywność o podanym ID nie isnieje"));
        User currentUser = getUser();
        if (currentUser.getUserType() == UserType.PERSON && !isEntitled(currentUser, activityId)) {
            // check if is security breach
            log.warn("User called for activity not assigned to the user! User details: {}, Activity details:{}",
                    currentUser, activity);
            throw new ActivityException("Aktynowść o podanym ID nie isnieje");
        }
        activityRepository.delete(activity);
    }

    @Override
    public List<ActivityDto2> getActivities(String searchParams) {
        log.debug("invoked [getActivityByDate] with searchParams: {}", searchParams);
        Map<String, String> searchParamsMap = ShelterAnimalService.parseSearchParams(searchParams);
        try {
            if (searchParamsMap.containsKey(ACTIVITY_TIME_FIELD)) {
                LocalDate requestedDate = LocalDate.parse(searchParamsMap.get(ACTIVITY_TIME_FIELD), DateTimeFormatter.ISO_DATE);
                LocalDateTime dateToSearch = LocalDateTime.of(requestedDate, defaultTimeOfActivity);
                searchParamsMap.put(ACTIVITY_TIME_FIELD, dateToSearch.toString());
            }
            User user = getUser();
            if (user.getUserType() == UserType.SHELTER) {
                searchParamsMap.put(SpecificationConstants.SHELTER_ID, user.getId().toString());
            } else if (user.getUserType() == UserType.PERSON) {
                searchParamsMap.put(SpecificationConstants.USER_ID, user.getId().toString());
            }

            ActivitySpecification activitySpecification = new ActivitySpecification(searchParamsMap);
            return activityMapper.toDto2List(activityRepository.findAll(activitySpecification));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        //we should secure the case when e.g user sends params not related to him?
    }

    @Override
    public ActivityDto2 getActivityById(Long id) {
        return activityMapper.toDto2(activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aktywność o podanym ID nie isnieje")));
    }

    @Override
    public List<AnimalDto> getAnimalsWithoutActivityAtDate(LocalDate date) {
        log.debug("invoked [getAnimalsWithoutActivityAtDate] with localDate: {}", date);
        String reqDate = "";
        if (date != null) {
            reqDate = LocalDateTime.of(date, defaultTimeOfActivity).toString();
        }
        User shelter = getUser();
        try {
            AnimalSpecification animalSpecification = new AnimalSpecification(Map.of(ACTIVITY_TIME_FIELD, reqDate,
                    SpecificationConstants.SHELTER_ID, shelter.getId().toString()));
            return animalMapper.toDtoList(animalRepository.findAll(animalSpecification));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
//        if (localDate == null) {
//            return animalMapper.toDtoList(animalRepository.findAllAnimalsWithoutActivity(shelter.getId()));
//        }
//        return animalMapper.toDtoList(animalRepository.findAllAnimalsWithoutActivityAtDate(reqDate, shelter.getId()));
    }

    private boolean isEntitled(User currentUser, Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Aktynowść o podanym ID nie isnieje"));
        return activity.getUser().equals(currentUser);
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);
    }

    @Async
    @Scheduled(cron = "0 0 22 * * ?")   //check every day at 22:00 PM
    @Transactional
    public void deleteExpiredActivities() {
        log.debug("activity scheduler started");
        LocalDateTime now = LocalDateTime.now();
        List<Activity> adoptionList = activityRepository.findAll();
        List<Activity> expiredActivites = adoptionList.stream()
                .filter(activity -> activity.getActivityTime() != null)
                .filter(activity -> activity.getActivityTime().isBefore(now))
                .toList();
        for (Activity activity : expiredActivites) {
            deleteActivity(activity.getId());
        }
        log.debug("activity scheduler finished");
    }
}

