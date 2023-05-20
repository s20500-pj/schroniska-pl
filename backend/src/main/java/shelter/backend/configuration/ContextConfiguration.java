package shelter.backend.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import shelter.backend.email.EmailService;
import shelter.backend.registration.service.ApprovalProvider;
import shelter.backend.rest.mockService.ApprovalProviderMock;
import shelter.backend.rest.mockService.EmailServiceMock;

@Configuration
@ConditionalOnProperty(name = "enable.mock.service", havingValue = "true")
public class ContextConfiguration {

    @Bean
    @ConditionalOnProperty(name = "enable.mock.krs", havingValue = "true")
    @Primary
    public ApprovalProvider approvalProviderMock() {
        return new ApprovalProviderMock();
    }

    @Bean
    @ConditionalOnProperty(name = "enable.mock.email", havingValue = "true")
    @Primary
    public EmailService emailServiceMock() {
        return new EmailServiceMock();
    }
}
