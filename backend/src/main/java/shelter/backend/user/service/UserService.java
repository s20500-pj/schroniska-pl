package shelter.backend.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> search(@RequestBody Map<String, String> searchParams);
    UserDto getUserInfo();
    UserDto update(UserDto userDto);
    void delete(Long id, HttpServletRequest request, HttpServletResponse response);
}
