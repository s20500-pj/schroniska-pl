package shelter.backend.rest.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shelter.backend.utils.exception.ActivityException;
import shelter.backend.utils.exception.AdoptionException;
import shelter.backend.utils.exception.MessageNotSendException;
import shelter.backend.utils.exception.RequiredFieldException;
import shelter.backend.utils.exception.UsernameAlreadyExist;
import shelter.backend.utils.exception.ValidFieldException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ShelterGlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> onAuthenticationException(
            AuthenticationException e) {
        log.info("Authentication exception occurred. Exception: ", e);
        return new ResponseEntity<>("Unauthorized path", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> onMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        log.info("Validation error. Exception: ", methodArgumentNotValidException);
        List<FieldError> violations = methodArgumentNotValidException.getBindingResult().getFieldErrors();
        String errorMessage = "";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" ").append(violation.getDefaultMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = methodArgumentNotValidException.getMessage();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidFieldException.class)
    public ResponseEntity<String> onValidFieldException(
            ValidFieldException e) {
        log.info("Validation error. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExist.class)
    public ResponseEntity<String> onUsernameAlreadyExist(UsernameAlreadyExist e) {
        log.info("Registration error occurred. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<String> onRequiredFieldException(RequiredFieldException e) {
        log.info("Registration exception occurred. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageNotSendException.class)
    public ResponseEntity<String> onMessageNotSendException(MessageNotSendException e) {
        log.info("Problem with sending email. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AdoptionException.class)
    public ResponseEntity<String> onAdoptionException(AdoptionException e) {
        log.info("Exception occurred during adoption process. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActivityException.class)
    public ResponseEntity<String> onActivityException(ActivityException e) {
        log.info("Exception occurred during activity process. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> onEntityNotFoundException(EntityNotFoundException e) {
        log.info("Entity not found. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
