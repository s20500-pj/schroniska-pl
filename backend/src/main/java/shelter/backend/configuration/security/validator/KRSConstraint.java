package shelter.backend.configuration.security.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = KRSValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface KRSConstraint {
    String message() default "KRS musi się składać z 10 cyfr.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
