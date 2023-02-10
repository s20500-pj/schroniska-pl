package shelter.backend.registration.service;

import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;

public interface RegistrationUserService {
    List<UserDto> registerUser(UserDto userDto);
}
