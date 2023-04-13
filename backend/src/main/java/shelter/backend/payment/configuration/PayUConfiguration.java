//package shelter.backend.payment.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//import shelter.backend.payment.service.PayUAuthenticationService;
//
//import java.util.Collections;
//
//public class PayUConfiguration {
//
//    private final PayUAuthenticationService payUAuthenticationService;
//
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
//}
//}
