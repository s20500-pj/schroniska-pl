package shelter.backend.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import shelter.backend.login.service.AuthenticationService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.List;
import java.util.Map;

import static shelter.backend.rest.model.specification.UserSpecification.USER_TYPE;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PayUClientCredentialsRepository payUClientCredentialsRepository;
    private final AuthenticationService authenticationService;
    private final AdoptionRepository adoptionRepository;
    private final ActivityRepository activityRepository;

    public List<UserDto> search(@RequestBody Map<String, String> searchParams) {
        searchParams.put(USER_TYPE, UserType.PERSON.toString());
        UserSpecification userSpecification = new UserSpecification(searchParams);
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }

    public UserDto getUserInfo() {
        User user = getLoggedUser();
        return userMapper.toDto(userRepository.findUserById(user.getId()));
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    @Transactional
    public void delete(Long id, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Delete user by id: {}", id);
        String username = ClientInterceptor.getCurrentUsername();
        User currentUser = userRepository.findUserByEmail(username);
        User userToDelete = userRepository.findUserById(id);

        if (userToDelete == null) {
            throw new EntityNotFoundException("Nie znaleziono użytkownika o podanym id");
        }

        if (isAllowedToDelete(currentUser, userToDelete)) {
            if (userToDelete.getUserType() == UserType.SHELTER) {
                deleteAssociatedPayUClientCredentials(userToDelete);
            }

            deleteUserActivitiesAndAdoptions(userToDelete);
            userRepository.delete(userToDelete);

            if (isLoggedUserSameAsToDelete(currentUser, userToDelete)) {
                authenticationService.clearCookies(request, response);
            }
        }
    }

    private boolean isAllowedToDelete(User currentUser, User userToDelete) {
        return currentUser.getUserType() == UserType.ADMIN || isLoggedUserSameAsToDelete(currentUser, userToDelete);
    }

    private void deleteAssociatedPayUClientCredentials(User userToDelete) {
        payUClientCredentialsRepository.findByShelter_Id(userToDelete.getId())
                .ifPresent(payUClientCredentialsRepository::delete);
    }

    private void deleteUserActivitiesAndAdoptions(User user) {
        List<Activity> activities = activityRepository.findByUser(user);
        activityRepository.deleteAll(activities);
        List<Adoption> adoptions = adoptionRepository.findByUser(user);
        adoptionRepository.deleteAll(adoptions);
    }

    private static boolean isLoggedUserSameAsToDelete(User currentUser, User userToDelete) {
        return currentUser.equals(userToDelete);
    }

    private User getLoggedUser() {
        String currentUsername = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(currentUsername);
    }
}

