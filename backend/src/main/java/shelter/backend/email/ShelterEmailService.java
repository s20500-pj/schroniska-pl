package shelter.backend.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shelter.backend.utils.constants.ShelterPathConstants;
import shelter.backend.utils.exception.MessageNotSendException;

import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
public class ShelterEmailService implements EmailService{

    private final JavaMailSender emailSender;

    private final MessageSource messageSource;

    private final String CONFIRMATION_PROPERTIES = "confirmation.mail";

    @Value("${shelter.redis.token.expiration.minutes}")
    private String expTime;

    @Value("${spring.mail.username}")
    private String from;

    public ShelterEmailService(JavaMailSender emailSender, MessageSource messageSource) {
        this.emailSender = emailSender;
        this.messageSource = messageSource;
    }

    @Override
    public void sendUserConfirmationEmail(String email, String token) {
        final String propertyName = CONFIRMATION_PROPERTIES + ".user";
        sendConfirmationEmail(email, token, propertyName);
    }

    @Override
    public void sendShelterConfirmationEmail(String email, String token) {
        final String propertyName = CONFIRMATION_PROPERTIES + ".shelter";
        sendConfirmationEmail(email, token, propertyName);
    }

    private void sendConfirmationEmail(String email, String token, String propertyName) {
        Objects.requireNonNull(email);
        log.debug("Sending confirmation email to {} token {} ...", email, token);
        String[] params = {ShelterPathConstants.CONFIRMATION, token, expTime};
        final String text = messageSource.getMessage(propertyName, params, Locale.getDefault());
        final String subject = messageSource.getMessage("confirmation.mail.subject", null, Locale.getDefault());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            log.info("Sent confirmation email to {}", email);
        }
        catch (Exception e){
            log.error("Problem encountered while sending email", e);
            throw new MessageNotSendException("Problem z rejestracją schroniska. Prosimy spróbować zarejestrować się później");
        }
    }
}
