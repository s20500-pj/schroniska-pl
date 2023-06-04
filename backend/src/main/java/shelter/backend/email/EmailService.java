package shelter.backend.email;

import shelter.backend.rest.model.enums.UserType;

public interface EmailService{
    void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType, String name);
    void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id, String animalName, String firstName);
    void sendAdoptionCancellation(String email, long id, String animalName, String userFirstName);
    void sendAdoptionSuspension(String email, String shelterName, long id, String animalName, String userFirstName);
    void sendVirtualAdoptionConfirmationAdopted(String email, String shelterName, String animalName, String adoptionPeriod, String userFirstName);
    void sendShelterApprovalConfirmation(String email, String shelterName);
    void sendVirtualAdoptionTimeout(String email, String animalName, String shelterName, String userFirstName);
    void sendActivityCancellation(String email, String animalName, String activityDate, String userFirstName);
    void sendPaymentInfo(String email, String purpose, String amount, String shelterName, String orderId);
    void sendPaymentFailure(String email, String purpose, String amount,String shelterName, String orderId);
}
