package shelter.backend.payment.payu.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PayUConfiguration {

    @Bean
    public RestTemplate defaultRestTemplate(){
        return new RestTemplate();
    }

}
