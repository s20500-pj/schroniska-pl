package shelter.backend.activity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.enums.ActivityType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.mapper.ActivityMapper;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShelterActivityServiceTest {

    @InjectMocks
    ShelterActivityService activityService;

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  ActivityRepository activityRepository;

    @Mock
    private  AnimalRepository animalRepository;

    @Mock
    private  ActivityMapper activityMapper;

    @Mock
    private  AnimalMapper animalMapper;

    private final  LocalTime defaultTimeOfActivity = LocalTime.of(16, 0);

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(activityService, "defaultTimeOfActivity", defaultTimeOfActivity);
    }

    @Test
    void registerActivity() {
        //
        ActivityRegisterReq activityRegisterReq = new ActivityRegisterReq();
        activityRegisterReq.setActivityDate(LocalDate.now());
        activityRegisterReq.setActivityType(ActivityType.WALKING);
        activityRegisterReq.setAnimalId(1L);
        Animal animal = new Animal();
        animal.setAnimalStatus(AnimalStatus.READY_FOR_ADOPTION);
        //
        when(animalRepository.findAnimalById(any())).thenReturn(animal);
        //
    }


    @Test
    void deleteActivity() {
    }

    @Test
    void getActivities() {
    }

    @Test
    void getActivityById() {
    }

    @Test
    void getAnimalsWithoutActivityAtDate() {
    }

    @Test
    void deleteExpiredActivities() {
    }
}