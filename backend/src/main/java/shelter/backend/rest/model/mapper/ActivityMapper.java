package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.ActivityDto;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.storage.repository.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ActivityMapper implements DtoEntityMapper<Activity, ActivityDto> {

    private final ActivityRepository activityRepository;

    @Override
    public Activity toEntity(ActivityDto activityDto) {
        return Optional.ofNullable(activityDto.getId())
                .flatMap(activityRepository::findById)
                .map(entity -> entity.toEntity(activityDto))
                .orElseGet(() -> new Activity().toEntity(activityDto));
    }

    @Override
    public ActivityDto toDto(Activity activity) {
        return activity.toDto();
    }

    public ActivityDto2 toDto2(Activity activity) {
        AnimalDto animalDto = getAnimalDtoNullifyAdoptionsAndActivities(activity);
        return activity.toDto2(animalDto);
    }

    public List<ActivityDto2> toDto2List(List<Activity> activities) {
        List<ActivityDto2> activityDto2s = new ArrayList<>();
        activities.forEach(activity -> {
            AnimalDto animalDto = getAnimalDtoNullifyAdoptionsAndActivities(activity);
            activityDto2s.add(activity.toDto2(animalDto));
        });
        return activityDto2s;
    }

    private AnimalDto getAnimalDtoNullifyAdoptionsAndActivities(Activity activity) {
        AnimalDto animalDto = activity.getAnimal().toSimpleDto();
        animalDto.setAdoptions(null);
        animalDto.setActivities(null);
        return animalDto;
    }
}



