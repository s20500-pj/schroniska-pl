package shelter.backend.activity.service;

import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    ActivityDto2 registerActivity(ActivityRegisterReq activityRegisterReq);

    void deleteActivity(Long id);

    List<ActivityDto2> getActivities(Map<String,String> searchParams);

    ActivityDto2 getActivityById(Long id);

    List<AnimalDto> getAnimalsWithoutActivityAtDate(LocalDate localDate);
}
