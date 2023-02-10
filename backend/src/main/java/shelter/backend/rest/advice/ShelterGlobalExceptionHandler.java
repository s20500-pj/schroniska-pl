package shelter.backend.rest.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shelter.backend.utils.exception.UsernameAlreadyExist;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class ShelterGlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        log.info("Validation error. Exception: ", constraintViolationException);
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage = "";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" ").append(violation.getMessage()));
            errorMessage = builder.toString();
        } else if (constraintViolationException.getMessage() != null) {
            errorMessage = constraintViolationException.getMessage();
        } else {
            errorMessage = "Validation Error";
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> onMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        log.info("Validation error. Exception: ", methodArgumentNotValidException);
        List<FieldError> violations =  methodArgumentNotValidException.getBindingResult().getFieldErrors();
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

    @ExceptionHandler(UsernameAlreadyExist.class)
    public ResponseEntity<String> onUsernameAlreadyExist(UsernameAlreadyExist e){
        log.info("Registration error. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
