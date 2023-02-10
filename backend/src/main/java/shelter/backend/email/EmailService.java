package shelter.backend.email;

public interface EmailService{
    void sendUserConfirmationEmail(String email, String token);
    void sendShelterConfirmationEmail(String email, String token);
}
