package shelter.backend.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.List;

import static shelter.backend.animals.service.ShelterAnimalService.parseSearchParams;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> search(String searchParams) {
        UserSpecification userSpecification = new UserSpecification(parseSearchParams(searchParams));
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }

    public UserDto getUserById() {
        User user = getUser();
        return userMapper.toDto(userRepository.findUserById(user.getId()));
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public void delete(Long id) {
        String username = ClientInterceptor.getCurrentUsername();
        User currentUser = userRepository.findUserByEmail(username);
        User userToDelete = userRepository.findUserById(id);
        if (userToDelete != null) {
            if (currentUser.getUserType() == UserType.ADMIN || (currentUser.equals(userToDelete))) // if not admin check if user can perform delete(only delete himself)
            {
                userRepository.delete(userToDelete);
            }
        }
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);
    }

}
