package shelter.backend.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shelter.backend.login.service.AuthenticationService;
import shelter.backend.rest.model.dtos.AddressDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.storage.repository.ActivityRepository;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShelterUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private AdoptionRepository adoptionRepository;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private ShelterUserService shelterUserService;

    private User user;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("john@doe.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new Address())
                .firstName("John")
                .lastName("Doe")
                .roles(new HashSet<>())
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .email("john@doe.com")
                .password("password")
                .userType(UserType.PERSON)
                .address(new AddressDto())
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    public void search() {
        //
        Map<String, String> searchParams = Map.of("firstName", "John");
        List<User> users = new ArrayList<>();
        users.add(user);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);
        when(userRepository.findAll(any())).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(userDtos);
        //
        List<UserDto> result = shelterUserService.search(searchParams);
        //
        assertEquals(userDtos, result);
    }

    @Test
    public void getUserInfo() {
        //
        String email = "john@doe.com";
        mockStatic(ClientInterceptor.class);
        when(ClientInterceptor.getCurrentUsername()).thenReturn(email);
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(userDto);
        //
        UserDto result = shelterUserService.getUserInfo();
        //
        assertEquals(userDto, result);
        Mockito.framework().clearInlineMock(ClientInterceptor.class);
    }

    @Test
    public void update() {
        //
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        //
        UserDto result = shelterUserService.update(userDto);
        //
        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
        assertEquals(userDto, result);
    }

    @Test
    public void delete_self_success() {
        //
        String email = "john@doe.com";
        mockStatic(ClientInterceptor.class);
        when(ClientInterceptor.getCurrentUsername()).thenReturn(email);
        when(userRepository.findUserByEmail(eq("john@doe.com"))).thenReturn(user);
        when(userRepository.findUserById(eq(1L))).thenReturn(user);
        //
        shelterUserService.delete(1L, request, response);
        //
        verify(userRepository, times(1)).delete(eq(user));
        Mockito.framework().clearInlineMock(ClientInterceptor.class);
    }
}

