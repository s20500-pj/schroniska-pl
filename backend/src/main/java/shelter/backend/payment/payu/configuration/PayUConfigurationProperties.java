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
    private String grantType;
    private String callbackPath;
    private String notifyPath;


    //    @Bean("payuApiRestTemplate")
//    public RestTemplate payuRestTemplate() {
//        final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//        restTemplate.setInterceptors(Collections.singletonList((httpRequest, bytes, clientHttpRequestExecution) -> {
//            final PayUAuthToken payUAuthToken = payUAuthenticationService.authenticate();
//            final HttpHeaders headers = httpRequest.getHeaders();
//            headers.add("Authorization", payUAuthToken.getTokenType() + " " + payUAuthToken.getAccessToken());
//            if (!headers.containsKey("Content-Type")) {
//                headers.add("Content-Type", "application/json");
//            }
//            return clientHttpRequestExecution.execute(httpRequest, bytes);
//        }));
//
//        return restTemplate;
//    }

}
