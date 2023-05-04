package shelter.backend.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> search(String searchParams);
    UserDto getUserInfo();
    UserDto update(UserDto userDto);
    void delete(Long id, HttpServletRequest request, HttpServletResponse response);
}
