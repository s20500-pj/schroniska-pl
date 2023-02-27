package shelter.backend.utils.exception;

public class RequiredFieldException extends RuntimeException {
    public RequiredFieldException(String fieldName) {
        super(String.format("Brak wymaganego pola: %s", fieldName));
    }
}
