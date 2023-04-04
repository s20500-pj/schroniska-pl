package shelter.backend.user.service;

import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> search(Map<String, String> searchParams);
    UserDto getUserById(Long userId);
    UserDto update(UserDto userDto);
    void delete();
}
