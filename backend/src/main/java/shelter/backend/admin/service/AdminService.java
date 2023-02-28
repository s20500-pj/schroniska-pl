package shelter.backend.admin.service;

import shelter.backend.admin.model.AdminShelterResponseDto;
import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

public interface AdminService {
    List<AdminShelterResponseDto> getShelters();

    List<UserDto> search(Map<String, String> searchParams);
}
