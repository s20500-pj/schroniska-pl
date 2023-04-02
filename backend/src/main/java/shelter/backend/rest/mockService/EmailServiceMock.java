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
        };
        String[] params = {"/confirmation", token, expirationTime};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate) {
        final String messageProperty = ADOPTION_PROPERTIES + ".invitation";
        String[] params = {shelterName, adoptionValidDate};
        final String text = messageSource.getMessage(messageProperty, params, Locale.getDefault());
        log.info(text);
    }
}
