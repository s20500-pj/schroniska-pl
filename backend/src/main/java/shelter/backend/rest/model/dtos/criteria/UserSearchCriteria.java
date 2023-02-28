package shelter.backend.rest.model.dtos.criteria;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserSearchCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private String shelterName;
    private String isDisabled;
    private String approvalStatus;
    private String userType;
}
