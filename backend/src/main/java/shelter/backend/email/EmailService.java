package shelter.backend.email;

import shelter.backend.rest.model.enums.UserType;

public interface EmailService{
    void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType);
    void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id);
    void sendAdoptionCancellation(String email, long id);
    void sendAdoptionSuspension(String email, String shelterName, long id);
}
