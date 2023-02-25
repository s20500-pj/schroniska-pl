package shelter.backend.configuration.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import shelter.backend.rest.model.dtos.UserDto;

import java.util.regex.Pattern;

public class KRSValidator implements ConstraintValidator<KRSConstraint, UserDto> {
    private final Pattern krsPattern = Pattern.compile("^\\d{10}$");
    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext constraintValidatorContext) {
        if (user.getAddress() == null || StringUtils.isBlank(user.getAddress().getKRS_number())){
            return false;
        }
        String krs = user.getAddress().getKRS_number();
        return krsPattern.matcher(krs).matches();
    }
}
