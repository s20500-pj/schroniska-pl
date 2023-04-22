package shelter.backend.activity.service;

import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto2;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    ActivityDto2 registerActivity(ActivityRegisterReq activityRegisterReq);

    void deleteActivity(Long id);

    List<ActivityDto2> getAll();

    List<ActivityDto2> search(Map<String, String> searchParams);

    List<ActivityDto2> getActivityByDate(LocalDate date);
}
