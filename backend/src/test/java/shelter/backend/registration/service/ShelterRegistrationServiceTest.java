package shelter.backend.registration.service;

import org.apache.commons.lang3.time.DateUtils;
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
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.storage.repository.AddressRepository;
import shelter.backend.storage.repository.RoleRepository;
import shelter.backend.storage.repository.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    RoleRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserMapper userMapper = new UserMapper(userRepository, addressRepository);

    private User user;

    private UserDto userDto;

    private Token token;

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

        token = new Token("1", UUID.randomUUID().toString(), user.getEmail(), new Date());

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
        when(tokenService.generateToken(anyInt(), any())).thenReturn(token);
        doNothing().when(shelterEmailService).sendConfirmationEmail(any(), any(), any(), any(), any());
        //
        //
        UserDto result = registrationService.register(userDto);
        verify(shelterEmailService, times(1)).sendConfirmationEmail(any(), any(), any(), any(), any());
        Assertions.assertTrue(result.isDisabled());
    }

    @Test
    void confirmTokenUserEnabled() {
        //
        when(tokenService.getToken(any())).thenReturn(token);
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        Assertions.assertTrue(registrationService.confirmToken("confirmationToken"));
        verify(userRepository, times(1)).save(user);
        verify(tokenService, times(1)).deleteToken(token);

    }

    @Test
    void deleteUnusedTokensUnconfirmedUsers() {
        //
        Token expired = new Token("2", UUID.randomUUID().toString(), "test2@expired.com", DateUtils.addHours(new Date(), -1));
        //
        when(tokenService.findAll()).thenReturn(List.of(expired, token));
        when(tokenService.isExpired(expired)).thenReturn(true);
        //
        registrationService.deleteUnusedTokensUnconfirmedUsers();
        verify(tokenService, times(1)).deleteToken(expired);
    }
}

