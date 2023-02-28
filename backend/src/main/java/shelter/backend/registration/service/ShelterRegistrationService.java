package shelter.backend.registration.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shelter.backend.admin.model.AdminResponseConverter;
import shelter.backend.admin.model.AdminShelterRequestDto;
import shelter.backend.admin.model.AdminShelterResponseDto;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.ERole;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.storage.repository.AddressRepository;
import shelter.backend.storage.repository.RoleRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.rest.model.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ShelterRegistrationService implements RegistrationService {


    private final EmailService shelterEmailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ApprovalProvider approvalProvider;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    public ShelterRegistrationService(EmailService shelterEmailService, UserRepository userRepository, RoleRepository roleRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder, TokenService tokenService, ApprovalProvider approvalProvider) {
        this.shelterEmailService = shelterEmailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.approvalProvider = approvalProvider;
        this.userValidator = new UserValidator(userRepository);
        this.userMapper = new UserMapper(userRepository, addressRepository);
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        log.debug("User Registration started for username: {}", userDto.getEmail());
        return register(userDto);
    }

    @Override
    public UserDto registerShelter(UserDto userDto) {
        log.debug("Shelter Registration started for username: {}", userDto.getEmail());
        return register(userDto);
    }

    public UserDto register(UserDto userDto) {
        userValidator.throwIfNotValid(userDto);
        User user = persistTheUser(userDto);
        log.info("1st stage of registration for user: {} completed successfully", user.getEmail());
        sendConfirmationEmail(user);
        return userMapper.toDto(user);
    }

    private void sendConfirmationEmail(User user) {
        Token token = tokenService.generateToken(user.hashCode(), user.getEmail());
        log.debug("Confirmation token {} created for user: {}", token.getId(), user.getEmail());
        if (isShelter(user)) {
            shelterEmailService.sendShelterConfirmationEmail(user.getEmail(), token.getId());
            user.setApprovalStatus(ApprovalStatus.EMAIL_NIEPOTWIERDZONY);//TODO na angielski
            userRepository.save(user);
        } else {
            shelterEmailService.sendUserConfirmationEmail(user.getEmail(), token.getId());
        }
    }

    private boolean isShelter(User user) {
        return user.getUserType().equals(UserType.SHELTER);
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
            }
        } catch (Exception e) {
            log.error("Excepetion occured during validating shelter details. ", e);
        }
    }

    @Override
    public List<AdminShelterResponseDto> enableShelterAccounts(List<AdminShelterRequestDto> shelterList) { //TODO zmiana response na liste enablowanych shelterów
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

    private User persistTheUser(UserDto userDto) {
        User newUser = userMapper.toEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setDisabled(true);
        Role role = roleRepository.findByName(ERole.ROLE_USER);
        newUser.getRoles().add(role);
        return userRepository.save(newUser);
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
