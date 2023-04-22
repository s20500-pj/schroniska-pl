package shelter.backend.shelter.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.UserRepository;

import java.util.List;
import java.util.Map;

import static shelter.backend.animals.service.ShelterAnimalService.parseSearchParams;

@RequiredArgsConstructor
@Service
public class DefaultShelterService implements ShelterService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public static final String USER_TYPE = "userType";

    public UserDto getShelterById(Long shelterId) {
        User shelter = userRepository.findUserById(shelterId);
        if (shelter == null || !shelter.getUserType().equals(UserType.SHELTER)) {
            throw new EntityNotFoundException("Schronisko o podanym id nie istnieje");
        }
        return userMapper.toDto(shelter);
    }

    public List<UserDto> searchShelters(String searchParams) {
        UserSpecification userSpecification = new UserSpecification(parseSearchParams(searchParams));
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }
}
