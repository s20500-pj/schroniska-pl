package shelter.backend.registration.service;

import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;

import java.util.List;

public interface RegistrationService {
    UserDto register(UserDto userDto);

    boolean confirmToken(String confirmationToken);

    void checkApprovalStatus(User User);

    List<UserDto> enableShelterAccounts(List<Long> shelterIdList);
}
