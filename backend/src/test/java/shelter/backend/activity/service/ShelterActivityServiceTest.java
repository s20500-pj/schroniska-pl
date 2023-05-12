package shelter.backend.activity.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.ActivityType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.ActivityMapper;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterActivityServiceTest {

    @InjectMocks
    ShelterActivityService activityService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private AnimalMapper animalMapper;

    private final LocalTime defaultTimeOfActivity = LocalTime.of(16, 0);

    private ActivityDto2 activityDto2;

    private Activity activity;

    private User user;

    @BeforeEach
    public void setUp() {

        ReflectionTestUtils.setField(activityService, "defaultTimeOfActivity", defaultTimeOfActivity);

        activityDto2 = ActivityDto2.builder()
                .activityTime(LocalDateTime.of(LocalDate.now(), defaultTimeOfActivity))
                .animalDto(new AnimalDto())
                .activityType(ActivityType.WALKING)
                .id(1L)
                .build();

        user = User.builder()
                .id(1L)
                .email("john@doe.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new Address())
                .firstName("John")
                .lastName("Doe")
                .roles(new HashSet<>())
                .build();

        activity = Activity.builder()
                .activityTime(LocalDateTime.of(LocalDate.now(), defaultTimeOfActivity))
                .activityType(ActivityType.WALKING)
                .id(1L)
                .animal(new Animal())
                .user(user)
                .build();

        String email = "john@doe.com";
        mockStatic(ClientInterceptor.class);
        when(ClientInterceptor.getCurrentUsername()).thenReturn(email);
        lenient().when(userRepository.findUserByEmail(any())).thenReturn(user);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMock(ClientInterceptor.class);
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
        when(animalRepository.findAnimalById(any())).thenReturn(animal);
        when(activityMapper.toDto2(any())).thenReturn(activityDto2);
        //
        ActivityDto2 result = activityService.registerActivity(activityRegisterReq);
        //
        verify(animalRepository, times(1)).save(animal);
        verify(activityRepository, times(1)).save(any());
        Assertions.assertEquals(result, activityDto2);
    }


    @Test
    void deleteActivity() {
        //
        //
        when(activityRepository.findById(any())).thenReturn(Optional.of(activity));
        //
        activityService.deleteActivity(1L);
        verify(activityRepository, times(1)).delete(activity);

    }

    @Test
    void deleteExpiredActivities() {
        //
        Activity activity2 = Activity.builder()
                .id(2L)
                .activityTime(LocalDateTime.of(LocalDate.now().plusDays(-1), defaultTimeOfActivity))
                .activityType(ActivityType.WALKING)
                .user(user)
                .animal(new Animal())
                .build();
        when(activityRepository.findAll()).thenReturn(List.of(activity, activity2));
        //
        activityService.deleteExpiredActivities();
        //
        verify(activityRepository, times(1)).deleteById(2L);
    }
}


