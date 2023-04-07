package shelter.backend.activity.service;

import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.rest.model.dtos.ActivityDto;

public interface ActivityService {
    ActivityDto registerActivity(ActivityRegisterReq activityRegisterReq);

    void deleteActivity(Long id);

}
