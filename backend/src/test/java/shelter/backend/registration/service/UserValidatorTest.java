package shelter.backend.registration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shelter.backend.rest.model.dtos.AddressDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.exception.RequiredFieldException;
import shelter.backend.utils.exception.UsernameAlreadyExist;
import shelter.backend.utils.exception.ValidFieldException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator underTest;

    @Mock
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
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
    void allValid() {
        Assertions.assertDoesNotThrow(() -> underTest.throwIfNotValid(userDto));
    }

    @Test
    void emailNotValid(){
        //
        userDto.setEmail("wrongEmail");
        //
        //
        Assertions.assertThrows(ValidFieldException.class, () -> underTest.throwIfNotValid(userDto));
    }

    @Test
    void firstNameNotValid(){
        //
        userDto.setFirstName("");
        //
        //
        Assertions.assertThrows(RequiredFieldException.class, () -> underTest.throwIfNotValid(userDto));
    }

    @Test
    void userAlreadyExists(){
        //
        when(userRepository.findUserByEmail(userDto.getEmail())).thenReturn(new User());
        //
        //
        Assertions.assertThrows(UsernameAlreadyExist.class, () -> underTest.throwIfNotValid(userDto));
    }

}

