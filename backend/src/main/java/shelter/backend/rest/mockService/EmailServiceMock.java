package shelter.backend.rest.mockService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.enums.UserType;

import java.util.Locale;

@Slf4j
public class EmailServiceMock implements EmailService {

    private final String CONFIRMATION_PROPERTIES = "mail.confirmation";
    private final String ADOPTION_PROPERTIES = "shelter.mail.adoption";

    @Autowired
    MessageSource messageSource;

    @Override
    public void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType) {
        final String messageProperty = switch (userType) {
            case PERSON -> "user." + CONFIRMATION_PROPERTIES;
            case SHELTER -> "shelter." + CONFIRMATION_PROPERTIES;
            case ADMIN -> null;
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
}
