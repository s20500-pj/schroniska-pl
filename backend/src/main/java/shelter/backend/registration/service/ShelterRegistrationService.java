package shelter.backend.registration.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shelter.backend.admin.model.AdminResponseConverter;
import shelter.backend.admin.model.AdminShelterRequest;
import shelter.backend.admin.model.AdminShelterResponse;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.converter.UserConverter;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.ERole;
import shelter.backend.storage.repository.RoleRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.utils.exception.UsernameAlreadyExist;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ShelterRegistrationService implements RegistrationService {


    private final EmailService shelterEmailService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final ApprovalProvider approvalProvider;


    public ShelterRegistrationService(EmailService shelterEmailService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenService tokenService, ApprovalProvider approvalProvider) {
        this.shelterEmailService = shelterEmailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.approvalProvider = approvalProvider;
    }

    @Override
    public List<UserDto> registerUser(UserDto userRegistration) {
        log.debug("User Registration started for username: {}", userRegistration.getEmail());
        register(userRegistration);
        return userRepository.findAll().stream().map(UserConverter::dto).collect(Collectors.toList()); //FIXME change it to VOID after tests
    }

    @Override
    public List<UserDto> registerShelter(UserDto userRegistration) {
        log.debug("Shelter Registration started for username: {}", userRegistration.getEmail());
        register(userRegistration);
        return userRepository.findAll().stream().map(UserConverter::dto).collect(Collectors.toList()); //FIXME change it to VOID after tests
    }

    public void register(UserDto userRegistration) {
        boolean isAlreadyTaken = usernameIsAlreadyTaken(userRegistration);
        if (isAlreadyTaken) {
            log.info("Username: {} can't be registered bacause it's already taken", userRegistration.getEmail());
            throw new UsernameAlreadyExist("Nie można zarejestrować użytkownika. Podany adres e-mail jest już zajęty");
        }
        User user = persistTheUser(userRegistration);
        log.info("1st stage of registration for user: {} completed successfully", user.getEmail());
        sendConfirmationEmail(user);
    }

    private void sendConfirmationEmail(User user) {
        Token token = tokenService.generateToken(user.hashCode(), user.getEmail());
        log.debug("Confirmation token {} created for user: {}", token.getId(), user.getEmail());
        if (isShelter(user)) {
            shelterEmailService.sendShelterConfirmationEmail(user.getEmail(), token.getId());
            user.setApprovalStatus(ApprovalStatus.EMAIL_NIEPOTWIERDZONY);
            userRepository.save(user);
        } else {
            shelterEmailService.sendUserConfirmationEmail(user.getEmail(), token.getId());
        }
    }

    private boolean isShelter(User user) {
        return StringUtils.isNotBlank(user.getShelterName());
    }

    @Override
    @Transactional
    public boolean confirmToken(String confirmationToken) {
        Token token = tokenService.getToken(confirmationToken);
        log.debug("Token: {} retrieved", token.getToken());
        if (tokenService.isExpired(token)) {
            log.info("Token expired for username: {}", token.getUsername());
            removeExpTokenAndAssocUser(token, token.getUsername());
            return false;
        }

        User user = userRepository.findUserByEmail(token.getUsername());

        if (isShelter(user)) {
            user.setApprovalStatus(ApprovalStatus.OCZEKUJE);
            checkApprovalStatus(user);
        } else {
            user.setDisabled(false);
        }
        userRepository.save(user);
        tokenService.deleteToken(token);
        log.info("Token confirmed for username: {}", token.getUsername());
        return true;
    }

    @Override
    public void checkApprovalStatus(User user) {
        try {
            boolean isApproved = approvalProvider.validateShelterDetails(user.getAddress());
            if (isApproved) {
                user.setApprovalStatus(ApprovalStatus.POTWIERDZONY);
            } else {
                user.setApprovalStatus(ApprovalStatus.ODRZUCONY);
                //TODO what to do with ODRZUCONY shelter??
            }
        } catch (Exception e) {
            log.error("Excepetion occured during validating shelter details. ", e);
        }
    }

    @Override
    public List<AdminShelterResponse> enableShelterAccounts(List<AdminShelterRequest> shelterList) {
        shelterList.forEach(shelter -> {
            User user = userRepository.findUserByEmail(shelter.getEmail());
            if (user != null) {
                log.info("Shelter: {}, for username: {} accepted by admin", shelter.getShelterName(), shelter.getEmail());
                user.setDisabled(false);
                userRepository.save(user);
            }
        });
        List<User> disabledShelters = userRepository.findAllByShelterNameIsNotNullAndIsDisabledIsTrue();
        return disabledShelters.stream()
                .map(AdminResponseConverter::toAdminGetShelterResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    private void removeExpTokenAndAssocUser(Token token, String username) {
        User user = userRepository.findUserByEmail(username);
        if (user.isDisabled()) {
            userRepository.deleteUserByEmail(username);
            log.debug("Deleted username: {}", username);
        }
        tokenService.deleteToken(token);
        log.debug("Deletet token: {}", token);
    }

    private boolean usernameIsAlreadyTaken(UserDto userRegistration) {
        User user = userRepository.findUserByEmail(userRegistration.getEmail());
        return user != null;
    }

    private User persistTheUser(UserDto userRegistration) {
        userRegistration.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        User newUser = UserConverter.toEntity(userRegistration);
        Address newAddress = newUser.getAddress();
        newAddress.setUser(newUser);
        newUser.setDisabled(true);
        Role role = roleRepository.findByName(ERole.ROLE_USER);
        newUser.getRoles().add(role);
        userRepository.save(newUser);
        return newUser;
    }

    @Scheduled(fixedDelay = 1800000)   //check every 30min
    @Transactional
    public void deleteUnusedTokensUnconfirmedUsers() {
        log.debug("token scheduler started");
        Iterable<Token> tokenList = tokenService.findAll();
        List<Token> expiredTokens = StreamSupport
                .stream(tokenList.spliterator(), false)
                .filter(Objects::nonNull)
                .filter(tokenService::isExpired)
                .toList();
        for (Token token : expiredTokens) {
            removeExpTokenAndAssocUser(token, token.getUsername());
        }
        log.debug("token scheduler finished");
    }
}
