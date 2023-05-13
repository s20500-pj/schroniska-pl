package shelter.backend.rest.mockService;

import lombok.extern.slf4j.Slf4j;
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
    public void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType) {
        String CONFIRMATION_PROPERTIES = "mail.confirmation";
        final String messageProperty = switch (userType) {
            case PERSON -> "user." + CONFIRMATION_PROPERTIES;
            case SHELTER -> "shelter." + CONFIRMATION_PROPERTIES;
            default -> throw new IllegalStateException("Unexpected value: " + userType);
        };
        String[] params = {"/confirmation", token, expirationTime};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".invitation";
        String[] params = {shelterName, adoptionValidDate, String.valueOf(id)};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionCancellation(String email, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".cancellation";
        String[] params = {String.valueOf(id)};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionSuspension(String email, String shelterName, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".suspension";
        String[] params = {shelterName, String.valueOf(id)};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendVirtualAdoptionConfirmationAdopted(String email, String shelterName, String animalName, String adoptionPeriod) {
        final String messageProperty = ADOPTION_PROPERTIES + ".virtual.adopted";
        String[] params = {animalName, shelterName, adoptionPeriod};
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
    public void sendVirtualAdoptionTimeout(String email, String animalName, String shelterName) {
        final String messageProperty = ADOPTION_PROPERTIES + ".virtual.timeout";
        String[] params = {animalName, shelterName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendActivityCancellation(String email, String animalName) {
        final String messageProperty = "shelter.mail.activity.cancellation";
        String[] params = {animalName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendPaymentInfo(String email, String purpose, String amount, String shelterName) {
        final String messageProperty = PAYMENT_PROPERTIES + ".info";
        String[] params = {purpose, amount, shelterName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendPaymentFailure(String email, String purpose, String amount, String shelterName) {
        final String messageProperty = PAYMENT_PROPERTIES + ".failure";
        String[] params = {purpose, amount, shelterName};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }
}
