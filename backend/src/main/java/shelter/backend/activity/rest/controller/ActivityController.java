package shelter.backend.activity.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.activity.service.ActivityService;
import shelter.backend.rest.model.dtos.ActivityDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    private final ActivityService activityService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    ResponseEntity<ActivityDto> registerActivity(@RequestBody ActivityRegisterReq activityRegisterReq) {
        return ResponseEntity.ok(activityService.registerActivity(activityRegisterReq));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('USER')")
    @DeleteMapping("/delete/{activityId}")
    ResponseEntity<Void> registerActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }

}