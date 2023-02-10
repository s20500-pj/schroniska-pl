package shelter.backend.utils.exception;

public class TokenNotFound extends RuntimeException {
    public TokenNotFound() {
    }

    public TokenNotFound(String message) {
        super(message);
    }
}
