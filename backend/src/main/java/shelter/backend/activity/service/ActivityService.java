package shelter.backend.activity.service;

import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.time.LocalDate;
import java.util.List;

public interface ActivityService {
    ActivityDto2 registerActivity(ActivityRegisterReq activityRegisterReq);

    void deleteActivity(Long id);

    List<ActivityDto2> getAll();

    List<ActivityDto2> getUserActivities(Long id);

    List<ActivityDto2> getActivities(String searchParams);

    ActivityDto2 getActivityById(Long id);

    List<AnimalDto> getAnimalsWithoutActivityAtDate(LocalDate localDate);
}
