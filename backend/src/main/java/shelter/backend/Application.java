package shelter.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}


/*
TODO:
   1. ! provide MVC exception handling
   2. * Swagger for front-end team -> not working
   3. !!! authentication success/failure
   4. !!! logout process
   5. !!! configure the email address. current is not working. MailAuthenticationException is thrown.
   6. !!! payment module
   7. ! postman collection
   8. * use map struct instead of converters
   9. !!! what to do with not accepted shelters, implement delete methods
*/
