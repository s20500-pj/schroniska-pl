package shelter.backend.email;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.utils.exception.MessageNotSendException;

import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
public class ShelterEmailService implements EmailService {

    private final JavaMailSender emailSender;

    private final MessageSource messageSource;

    private final String ADOPTION_PROPERTIES = "shelter.mail.adoption";


    @Value("${spring.mail.username}")
    private String from;

    public ShelterEmailService(JavaMailSender emailSender, MessageSource messageSource) {
        this.emailSender = emailSender;
        this.messageSource = messageSource;
    }

    @Override
    public void sendConfirmationEmail(String email, String token, String expirationTime, UserType userType) {
        final String CONFIRMATION_PROPERTIES = "mail.confirmation";
        final String messageProperty = switch (userType) {
            case PERSON -> "user." + CONFIRMATION_PROPERTIES;
            case SHELTER -> "shelter." + CONFIRMATION_PROPERTIES;
            default -> throw new IllegalStateException("Unexpected value: " + userType);
        };
        final String subjectProperty = "confirmation.mail.subject";
        String[] params = {"/confirmation", token, expirationTime};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    @Override
    public void sendAdoptionInvitation(String email, String shelterName, String adoptionValidDate, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".invitation";
        final String subjectProperty = ADOPTION_PROPERTIES + "invitation.subject";
        String[] params = {shelterName, adoptionValidDate, String.valueOf(id)};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    @Override
    public void sendAdoptionCancellation(String email, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".cancellation";
        final String subjectProperty = ADOPTION_PROPERTIES + "cancellation.subject";
        String[] params = {String.valueOf(id)};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    @Override
    public void sendAdoptionSuspension(String email, String shelterName, long id) {
        final String messageProperty = ADOPTION_PROPERTIES + ".suspension";
        final String subjectProperty = ADOPTION_PROPERTIES + "suspension.subject";
        String[] params = {shelterName, String.valueOf(id)};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    @Override
    public void sendVirtualAdoptionConfirmationAdopted(String email, String shelterName, String animalName, String adoptionPeriod) {
        final String messageProperty = ADOPTION_PROPERTIES + ".virtual.adopted";
        final String subjectProperty = ADOPTION_PROPERTIES + ".virtual.adopted.subject";
        String[] params = {animalName, shelterName, adoptionPeriod};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    @Override
    public void sendShelterApprovalConfirmation(String email, String shelterName) {
        final String messageProperty = "shelter.mail.shelter.registration.confirmation";
        final String subjectProperty = messageProperty + ".subject";
        String[] params = {shelterName};
        sendEmail(email, subjectProperty, messageProperty, params);
    }

    private void sendEmail(String email, String subjectPropertyName, String messagePropertyName, String[] params) {
        Objects.requireNonNull(email);
        log.debug("Sending email to {} ...", email);
        final String subject = messageSource.getMessage(subjectPropertyName, null, Locale.getDefault());
        final String text = messageSource.getMessage(messagePropertyName, params, Locale.getDefault());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            log.debug("Email to {}", email);
        } catch (Exception e) {
            log.error("Problem encountered while sending email", e);
            throw new MessageNotSendException(e.getMessage());
        }
    }
}
