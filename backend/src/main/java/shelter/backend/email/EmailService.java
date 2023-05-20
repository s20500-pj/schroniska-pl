package shelter.backend.email;

import shelter.backend.rest.model.enums.UserType;

public interface EmailService{
    void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType);
    void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id);
    void sendAdoptionCancellation(String email, long id);
    void sendAdoptionSuspension(String email, String shelterName, long id);
    void sendVirtualAdoptionConfirmationAdopted(String email, String shelterName, String animalName, String adoptionPeriod);
    void sendShelterApprovalConfirmation(String email, String shelterName);
    void sendVirtualAdoptionTimeout(String email, String animalName, String shelterName);
    void sendActivityCancellation(String email, String animalName);
    void sendPaymentInfo(String email, String purpose, String amount, String shelterName);
    void sendPaymentFailure(String email, String purpose, String amount, String shelterName);
}
