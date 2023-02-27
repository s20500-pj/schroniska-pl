package shelter.backend.registration.service;

import shelter.backend.rest.model.dtos.UserDto;

public interface RegistrationService extends RegistrationUserService, RegistrationShelterService {
    UserDto register(UserDto userDto);

    boolean confirmToken(String confirmationToken);
}
