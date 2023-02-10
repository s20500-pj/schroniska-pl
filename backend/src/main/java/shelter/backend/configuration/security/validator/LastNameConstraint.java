package shelter.backend.configuration.security.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = LastNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LastNameConstraint {
    String message() default "Nazwisko jest wymagane.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}