package shelter.backend.utils.exception;

public class ValidFieldException extends RuntimeException {
    public ValidFieldException(String fieldName) {
        super(String.format("Wprowadź poprawne dane do pola: %s", fieldName));
    }
}
