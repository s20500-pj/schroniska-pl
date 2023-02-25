package shelter.backend.registration.service;

import shelter.backend.admin.model.AdminShelterRequest;
import shelter.backend.admin.model.AdminShelterResponse;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;

import java.util.List;

public interface RegistrationShelterService {
    List<UserDto> registerShelter(UserDto userDto);

    void checkApprovalStatus(User User);

    List<AdminShelterResponse> enableShelterAccounts(List<AdminShelterRequest> userDtoList);
}
