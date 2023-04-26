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
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.constants.SpecificationConstants;

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
    //fixme fix this weird searchParams everywhere in project
    public List<UserDto> searchShelters(Map<String, String> searchParams) {
        //fixme update this for admin
        User currentUser = getUser();
        if (currentUser != null && currentUser.getUserType() != UserType.ADMIN){
            searchParams.put("isDisabled", "false");
            searchParams.remove("approvalStatus");
        }
        searchParams.put("userType", "SHELTER");
        UserSpecification userSpecification = new UserSpecification(searchParams);
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);
    }

}
