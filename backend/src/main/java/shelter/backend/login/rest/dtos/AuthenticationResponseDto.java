package shelter.backend.login.rest.dtos;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.enums.UserType;

import java.util.Set;

@Builder
@Data
public class AuthenticationResponseDto {
    private Long userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String shelterName;
    private UserType userType;
    private String authToken;
    private Set<Role> roles;
}
