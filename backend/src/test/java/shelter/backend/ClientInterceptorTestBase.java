package shelter.backend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class ClientInterceptorTestBase {

    @Mock
    protected UserRepository userRepository;

    protected User user;

    @BeforeAll
    static public void interceptUserName(){

        String email = "john@doe.com";
        mockStatic(ClientInterceptor.class);
        when(ClientInterceptor.getCurrentUsername()).thenReturn(email);

    }

    @AfterAll
    static public void destroyUserName(){

        Mockito.framework().clearInlineMock(ClientInterceptor.class);
    }

    @BeforeEach
    public void getCurrentUser() {

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

        lenient().when(userRepository.findUserByEmail(any())).thenReturn(user);
    }

}

