package shelter.backend.activity.service;

import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    ActivityDto registerActivity(ActivityRegisterReq activityRegisterReq);

    void deleteActivity(Long id);

    List<ActivityDto> getAll();

    List<ActivityDto> search(Map<String, String> searchParams);
}
