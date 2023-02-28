package shelter.backend.admin.service;

import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

public interface AdminService {
    List<UserDto> getShelters();

    List<UserDto> search(Map<String, String> searchParams);
}
