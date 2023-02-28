package shelter.backend.admin.model;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.enums.ApprovalStatus;

@Data
@Builder
public class AdminShelterResponseDto {
    private UserDto userDto;

    private boolean isDisabled;

    private ApprovalStatus approvalStatus;
}
