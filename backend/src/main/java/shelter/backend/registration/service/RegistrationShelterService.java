package shelter.backend.registration.service;

import shelter.backend.admin.model.AdminShelterRequestDto;
import shelter.backend.admin.model.AdminShelterResponseDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;

import java.util.List;

public interface RegistrationShelterService {
    UserDto registerShelter(UserDto userDto);

    void checkApprovalStatus(User User);

    List<AdminShelterResponseDto> enableShelterAccounts(List<AdminShelterRequestDto> userDtoList);
}
