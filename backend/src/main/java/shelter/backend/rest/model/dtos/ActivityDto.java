package shelter.backend.rest.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.enums.ActivityType;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {
    private Long id;
    private ActivityType activityType;
    private LocalDateTime activityTime;
    private UserDto user;
    private Long animalId;
}
