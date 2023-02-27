package shelter.backend.admin.model;

import shelter.backend.rest.model.entity.User;

public class AdminResponseConverter {
    public static AdminShelterResponseDto toAdminGetShelterResponse(User user) {
        return AdminShelterResponseDto.builder()
                .userDto(user.toSimpleDto())
                .approvalStatus(user.getApprovalStatus())
                .isDisabled(user.isDisabled())
                .build();
    }
}
