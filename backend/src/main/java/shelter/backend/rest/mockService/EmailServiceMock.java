package shelter.backend.rest.mockService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import shelter.backend.email.EmailService;
import shelter.backend.utils.constants.ShelterPathConstants;

import java.util.Locale;

@Slf4j
public class EmailServiceMock implements EmailService {

    private final String CONFIRMATION_PROPERTIES = "confirmation.mail";

    @Autowired
    MessageSource messageSource;

    @Value("${shelter.redis.token.expiration.minutes}")
    private String expTime;

    @Override
    public void sendUserConfirmationEmail(String email, String token) {
        final String propertyName = CONFIRMATION_PROPERTIES + ".user";
        String[] params = {ShelterPathConstants.CONFIRMATION, token, expTime};
        final String text = messageSource.getMessage(propertyName, params, Locale.getDefault());
        log.info(text);
    }

    @Override
    public void sendShelterConfirmationEmail(String email, String token) {
        final String propertyName = CONFIRMATION_PROPERTIES + ".shelter";
        String[] params = {ShelterPathConstants.CONFIRMATION, token, expTime};
        final String text = messageSource.getMessage(propertyName, params, Locale.getDefault());
        log.info(text);
    }
}
