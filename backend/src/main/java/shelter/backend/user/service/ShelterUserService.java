package shelter.backend.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shelter.backend.login.JwtUtils;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ShelterUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    public List<UserDto> search(Map<String, String> searchParams) {
        UserSpecification userSpecification = new UserSpecification(searchParams);
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }

    public UserDto getUserById(Long userId) {
        return userMapper.toDto(userRepository.findUserById(userId));
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public void delete() {
        String token = ClientInterceptor.getBearerTokenHeader();
        User user = userRepository.findUserByEmail(jwtUtils.extractUsername(token.substring(7)));
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
