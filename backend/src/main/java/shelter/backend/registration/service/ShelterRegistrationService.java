package shelter.backend.registration.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.PayUClientCredentialsDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.ERole;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.PayUClientCredentialsMapper;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.RoleRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.exception.MessageNotSendException;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterRegistrationService implements RegistrationService {


    private final EmailService shelterEmailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PayUClientCredentialsRepository payUClientCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ApprovalProvider approvalProvider;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PayUClientCredentialsMapper payUClientCredentialsMapper;
    private final StringEncryptor shelterEncryptor;
    @Value("${shelter.redis.token.expiration.minutes}")
    private String expTime;

    public UserDto register(UserDto userDto) {
        log.debug("Registration started for username: {}", userDto.getEmail());
        userValidator.throwIfNotValid(userDto);
        User user = persistTheUser(userDto);
        log.info("1st stage of registration for user: {} completed successfully", user.getEmail());
        sendConfirmationEmail(user);
        return userMapper.toDto(user);
    }

    private void sendConfirmationEmail(User user) {
        Token token = tokenService.generateToken(user.hashCode(), user.getEmail());
        log.debug("Confirmation token {} created for user: {}", token.getId(), user.getEmail());
        try {
            if (isShelter(user)) {
                shelterEmailService.sendConfirmationEmail(user.getEmail(), token.getId(), expTime, user.getUserType(), user.getShelterName());
                user.setApprovalStatus(ApprovalStatus.EMAIL_NOT_CONFIRMED);
                userRepository.save(user);
            } else {
                shelterEmailService.sendConfirmationEmail(user.getEmail(), token.getId(), expTime, user.getUserType(), user.getFirstName());
            }
        } catch (MessageNotSendException e) {
            userRepository.deleteById(user.getId());
            throw new MessageNotSendException("Problem z rejestracją schroniska. Prosimy spróbować zarejestrować się później");
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
            user.setApprovalStatus(ApprovalStatus.PENDING);
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
            boolean isApproved = approvalProvider.validateShelterDetails(user.getAddress().getKrsNumber(), user.getShelterName());
            if (isApproved) {
                user.setApprovalStatus(ApprovalStatus.CONFIRMED);
            } else {
                user.setApprovalStatus(ApprovalStatus.REJECTED);
            }
        } catch (Exception e) {
            log.error("Excepetion occured during validating shelter details. ", e);
        }
    }

    @Transactional
    @Override
    public UserDto enableShelterAccounts(PayUClientCredentialsDto request) {
        log.debug("[enableShelterAccounts] :: ShelterId  {}", request.getShelterId());
        User user = userRepository.findById(request.getShelterId())
                .orElseThrow(() -> new EntityNotFoundException("Schronisko o podanym ID nie istnieje"));
        if (user.isDisabled()) {
            PayUClientCredentials payUClientCredentials = payUClientCredentialsMapper.toEntity(request);
            payUClientCredentialsRepository.save(payUClientCredentials);
            user.setDisabled(false);
            user.setIban(null);
            user.setApprovalStatus(ApprovalStatus.COMPLETED);
            userRepository.save(user);
            try {
                shelterEmailService.sendShelterApprovalConfirmation(user.getEmail(), user.getShelterName());
            } catch (MessageNotSendException e) {
                log.error("Nie można wysłać maila. schronisko zatwierdzone.");
            }
        } else {
            log.debug("Shelter for username {} already enabled", user.getEmail());
        }
        log.info("Shelter: {}, for username: {} accepted by admin", user.getShelterName(), user.getEmail());
        return userMapper.toDto(user);
    }

    private void removeExpTokenAndAssocUser(Token token, String username) {
        User user = userRepository.findUserByEmail(username);
        if (user != null && user.isDisabled()) {
            userRepository.deleteUserByEmail(username);
            log.debug("[SCHEDULER] :: Username: {} deleted", username);
        }
        tokenService.deleteToken(token);
        log.debug("[SCHEDULER] :: Token: {} deleted", token);
    }

    private User persistTheUser(UserDto userDto) {
        User newUser = userMapper.toEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setDisabled(true);
        ERole roleName;
        if (isShelter(newUser)) {
            newUser.setIban(shelterEncryptor.encrypt(userDto.getIban()));
            roleName = ERole.ROLE_SHELTER;
        } else {
            roleName = ERole.ROLE_USER;
        }
        Role role = roleRepository.findByName(roleName);
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
