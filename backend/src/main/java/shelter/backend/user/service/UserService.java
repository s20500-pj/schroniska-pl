package shelter.backend.user.service;

import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> search(String searchParams);
    UserDto getUserById();
    UserDto update(UserDto userDto);
    void delete(Long id);
}
