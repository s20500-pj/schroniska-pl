package shelter.backend.rest.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shelter.backend.utils.exception.UsernameAlreadyExist;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ShelterGlobalExceptionHandler {

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

    @ExceptionHandler(UsernameAlreadyExist.class)
    public ResponseEntity<String> onUsernameAlreadyExist(UsernameAlreadyExist e) {
        log.info("Registration error. Exception: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
