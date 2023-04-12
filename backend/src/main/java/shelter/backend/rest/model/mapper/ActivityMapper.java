package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.ActivityDto;
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

    public List<ActivityDto> toDtoList(List<Activity> activities) {
        List<ActivityDto> activityDtos = new ArrayList<>();
        activities.forEach(adoption -> activityDtos.add(adoption.toDto()));
        return activityDtos;
    }
}



