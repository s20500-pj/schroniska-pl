package shelter.backend.configuration.security.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ShelterNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShelterNameConstraint {
    String message() default "Nazwa schroniska jest wymagana.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
