package shelter.backend.admin.model;

import shelter.backend.rest.model.converter.UserConverter;
import shelter.backend.rest.model.entity.User;

public class AdminResponseConverter {
    public static AdminShelterResponse toAdminGetShelterResponse(User user){
        return AdminShelterResponse.builder()
                .userDto(UserConverter.dto(user))
                .approvalStatus(user.getApprovalStatus())
                .isDisabled(user.isDisabled())
                .build();
    }
}
