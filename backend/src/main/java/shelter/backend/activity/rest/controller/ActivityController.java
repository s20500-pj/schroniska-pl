package shelter.backend.activity.rest.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.activity.rest.req.ActivityRegisterReq;
import shelter.backend.activity.service.ActivityService;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    private final ActivityService activityService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    ResponseEntity<ActivityDto2> registerActivity(@RequestBody ActivityRegisterReq activityRegisterReq) {
        return ResponseEntity.ok(activityService.registerActivity(activityRegisterReq));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('USER')")
    @DeleteMapping("/delete/{activityId}")
    ResponseEntity<Void> registerActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<ActivityDto2> getActivityById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/getAll")
    ResponseEntity<List<ActivityDto2>> getAll() {
        return ResponseEntity.ok(activityService.getAll());
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getUserActivities")
    ResponseEntity<List<ActivityDto2>> getUserActivities(@RequestParam(name = "id", required = false) Long id) {
        return ResponseEntity.ok(activityService.getUserActivities(id));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/getActivityByDate/{date}")
    ResponseEntity<List<ActivityDto2>> getActivityByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ActivityDto2> activities = activityService.getActivityByDate(date);
        return activities != null ? ResponseEntity.ok(activities) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/getAnimalsNoActivity/{date}")
    ResponseEntity<List<AnimalDto>> getAnimalsNoActivity(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<AnimalDto> animals = activityService.getAnimalsWithoutActivityAtDate(date);
        return animals != null ? ResponseEntity.ok(animals) : ResponseEntity.notFound().build();
    }

}

