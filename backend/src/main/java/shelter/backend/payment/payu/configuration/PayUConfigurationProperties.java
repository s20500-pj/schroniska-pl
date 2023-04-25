package shelter.backend.payment.payu.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payment.payu")
public class PayUConfigurationProperties {

    private String description;
    private String authorizationUri;
    private String merchantPosId;
    private String orderUrl;
    private String orderCheckStatusUrl;
    private String orderNotificationUrl;
    private String grantType;
    private String callbackPath;

}
