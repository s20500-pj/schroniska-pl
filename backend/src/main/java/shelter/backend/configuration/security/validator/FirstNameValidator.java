package shelter.backend.configuration.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import shelter.backend.rest.model.dtos.UserDto;

public class FirstNameValidator implements ConstraintValidator<FirstNameConstraint, UserDto> {
    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isNotBlank(user.getFirstName());
    }
}
