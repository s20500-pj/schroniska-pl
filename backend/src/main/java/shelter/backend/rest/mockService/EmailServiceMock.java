package shelter.backend.rest.mockService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.enums.UserType;

import java.util.Locale;

@Slf4j
public class EmailServiceMock implements EmailService {

    private final String ADOPTION_PROPERTIES = "shelter.mail.adoption";

    private final String PAYMENT_PROPERTIES = "shelter.mail.payment";

    @Autowired
    MessageSource messageSource;

    @Override
    public void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType, String name) {
        String CONFIRMATION_PROPERTIES = "mail.confirmation";
        final String messageProperty = switch (userType) {
            case PERSON -> "user." + CONFIRMATION_PROPERTIES;
            case SHELTER -> "shelter." + CONFIRMATION_PROPERTIES;
            default -> throw new IllegalStateException("Unexpected value: " + userType);
        };
        String[] params = {name, "/confirmation", token, expirationTime};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id, String animalName, String firstName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".invitation";
        String[] params = {firstName, shelterName, adoptionValidDate, String.valueOf(id), animalName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionCancellation(String email, long id, String animalName, String firstName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".cancellation";
        String[] params = {firstName, String.valueOf(id), animalName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionSuspension(String email, String shelterName, long id, String animalName, String userFirstName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".suspension";
        String[] params = {userFirstName, shelterName, String.valueOf(id), animalName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendVirtualAdoptionConfirmationAdopted(String email, String shelterName, String animalName, String adoptionPeriod, String userFirstName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".virtual.adopted";
        String[] params = {userFirstName, animalName, shelterName, adoptionPeriod};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendShelterApprovalConfirmation(String email, String shelterName) {
        final String messageProperty = "shelter.mail.shelter.registration.confirmation";
        String[] params = {shelterName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendVirtualAdoptionTimeout(String email, String animalName, String shelterName, String userFirstName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".virtual.timeout";
        String[] params = {userFirstName, animalName, shelterName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendActivityCancellation(String email, String animalName, String activityDate, String userFirstName) {
        final String messageProperty = "shelter.mail.activity.cancellation";
        String[] params = {userFirstName, animalName, activityDate};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendPaymentInfo(String email, String purpose, String amount, String shelterName, String orderId) {
        final String messageProperty = PAYMENT_PROPERTIES + ".info";
        String[] params = {StringUtils.substringBefore(email, "@"), purpose, amount, shelterName, orderId};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendPaymentFailure(String email, String purpose, String amount, String shelterName, String orderId) {
        final String messageProperty = PAYMENT_PROPERTIES + ".failure";
        String[] params = {StringUtils.substringBefore(email, "@"), purpose, amount, shelterName, orderId};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }
}
