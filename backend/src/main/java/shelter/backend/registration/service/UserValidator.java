package shelter.backend.registration.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.constants.FieldNameConstants;
import shelter.backend.utils.exception.RequiredFieldException;
import shelter.backend.utils.exception.UsernameAlreadyExist;
import shelter.backend.utils.exception.ValidFieldException;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserValidator implements Validator<UserDto> {

    public static final Pattern KRS_PATTERN = Pattern.compile("^\\d{10}$");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final UserRepository userRepository;

    @Override
    public void throwIfNotValid(UserDto userDto) {
        validateEmail(userDto);
        checkIfUserAlreadyExists(userDto);
        checkRequiredFields(userDto);
    }

    private void checkIfUserAlreadyExists(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail());
        if (user != null) {
            log.info("Username: {} can't be registered bacause it's already taken", userDto.getEmail());
            throw new UsernameAlreadyExist("Nie można zarejestrować użytkownika. Podany adres e-mail jest już zajęty");
        }
    }

    private void checkRequiredFields(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getUserType().toString())) {
            throw new RequiredFieldException((FieldNameConstants.USER_TYPE));
        }
        switch (userDto.getUserType()) {
            case PERSON:
                if (StringUtils.isBlank(userDto.getFirstName())) {
                    throw new RequiredFieldException((FieldNameConstants.FIRST_NAME));
                } else if (StringUtils.isBlank(userDto.getLastName())) {
                    throw new RequiredFieldException((FieldNameConstants.LAST_NAME));
                }
                break;
            case SHELTER:
                if (StringUtils.isBlank(userDto.getShelterName())) {
                    throw new RequiredFieldException((FieldNameConstants.SHELTER_NAME));
                } else if (userDto.getAddress() == null || StringUtils.isBlank(userDto.getAddress().getKrsNumber())) {
                    throw new RequiredFieldException((FieldNameConstants.KRS_NUMBER));
                } else if (userDto.getIban() == null) {
                    throw new RequiredFieldException((FieldNameConstants.IBAN));
                } else if (!IBANValidator.DEFAULT_IBAN_VALIDATOR.isValid(String.valueOf(userDto.getIban()))) {
                    throw new ValidFieldException((FieldNameConstants.IBAN));
                } else if (!KRS_PATTERN.matcher(userDto.getAddress().getKrsNumber()).matches()) {
                    throw new ValidFieldException((FieldNameConstants.KRS_NUMBER));
                }
                break;
        }
    }

    private void validateEmail(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getEmail())) {
            throw new RequiredFieldException((FieldNameConstants.EMAIL));
        }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(userDto.getEmail()).matches()) {
            throw new ValidFieldException((FieldNameConstants.EMAIL));
        }
    }
}
