package shelter.backend.activity.rest.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import shelter.backend.rest.model.enums.ActivityType;

import java.time.LocalDate;

@Data
public class ActivityRegisterReq {

    @NotBlank(message = "Brak id zwierzaka")
    private Long animalId;
    @NotBlank(message = "Brak rodzaju aktywności")
    private ActivityType activityType;
    @NotBlank(message = "Brak daty aktywności")
    private LocalDate activityDate;

}
