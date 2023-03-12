package shelter.backend.rest.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.UserType;

@Builder
@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String shelterName;
    private boolean isDisabled;
    private ApprovalStatus approvalStatus;
    private UserType userType;
    private String information;
    @Valid
    private AddressDto address;
}
