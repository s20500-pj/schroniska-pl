package shelter.backend.registration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.AddressDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.PayUClientCredentialsMapper;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.storage.repository.AddressRepository;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.RoleRepository;
import shelter.backend.storage.repository.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterRegistrationServiceTest {

    @InjectMocks
    ShelterRegistrationService registrationService;

    @Mock
    private EmailService shelterEmailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PayUClientCredentialsRepository payUClientCredentialsRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private ApprovalProvider approvalProvider;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserMapper userMapper = new UserMapper(userRepository, addressRepository);
    @Mock
    private PayUClientCredentialsMapper payUClientCredentialsMapper;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void steup() {
        user = User.builder()
                .email("test@test.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new Address())
                .firstName("firstName")
                .lastName("lastName")
                .roles(new HashSet<>())
                .build();

        userDto = UserDto.builder()
                .email("test@test.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new AddressDto())
                .firstName("firstName")
                .lastName("lastName")
                .build();
    }


    @Test
    void shouldRegisterDisabledUser() {
        //
        doNothing().when(userValidator).throwIfNotValid(any());
        when(userMapper.toEntity(any())).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(UserDto.builder()
                .email("test@test.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new AddressDto())
                .firstName("firstName")
                .lastName("lastName")
                .isDisabled(true)
                .build());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(User.builder()
                .id(1L)
                .email(user.getEmail())
                .password(user.getPassword())
                .userType(user.getUserType())
                .address(user.getAddress())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .isDisabled(true)
                .build());
        when(tokenService.generateToken(anyInt(), any())).thenReturn(new Token("1", UUID.randomUUID().toString(), user.getEmail(), new Date()));
        doNothing().when(shelterEmailService).sendConfirmationEmail(any(), any(), any(), any());
        //
        //
        UserDto result = registrationService.register(userDto);
        verify(shelterEmailService, times(1)).sendConfirmationEmail(any(), any(), any(), any());
        Assertions.assertTrue(result.isDisabled());
    }

    @Test
    void confirmToken() {
    }

    @Test
    void checkApprovalStatus() {
    }

    @Test
    void enableShelterAccounts() {
    }

    @Test
    void deleteUnusedTokensUnconfirmedUsers() {
    }
}