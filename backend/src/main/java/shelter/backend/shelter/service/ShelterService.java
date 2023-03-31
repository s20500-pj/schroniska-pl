package shelter.backend.shelter.service;

import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

public interface ShelterService {
    UserDto getShelterById(Long shelterId);
    List<UserDto> searchShelters(Map<String, String> searchParams);
    UserDto update(UserDto userDto);

}
