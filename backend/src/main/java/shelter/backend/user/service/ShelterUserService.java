package shelter.backend.user.service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shelter.backend.login.service.AuthenticationService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
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
    private final AuthenticationService authenticationService;
    private final PayUClientCredentialsRepository payUClientCredentialsRepository;

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

    public void delete(Long id, HttpServletRequest request, HttpServletResponse response) {
        String username = ClientInterceptor.getCurrentUsername();
        User currentUser = userRepository.findUserByEmail(username);
        User userToDelete = userRepository.findUserById(id);
        if (userToDelete != null) {
            if (currentUser.getUserType() == UserType.ADMIN || isLoggedUserSameAsToDelete(currentUser, userToDelete)) // if not admin check if user can perform delete(only delete himself)
            {
                if (userToDelete.getUserType() == UserType.SHELTER) {
                    //rm associated payuDetails
                    payUClientCredentialsRepository.findByShelter_Id(userToDelete.getId())
                            .ifPresent(payUClientCredentialsRepository::delete);
                    log.debug("Username deleted, {}, {}", userToDelete.getEmail(), userToDelete.getUserType());
                }
                userRepository.delete(userToDelete);
                if (isLoggedUserSameAsToDelete(currentUser, userToDelete)) {
                    authenticationService.clearCookies(request, response);
                }
            }
        } else {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o podanym id");
        }
    }

    private static boolean isLoggedUserSameAsToDelete(User currentUser, User userToDelete) {
        return currentUser.equals(userToDelete);
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);

    }

}
