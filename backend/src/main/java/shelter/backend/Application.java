package shelter.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}


/*
TODO:
   1. ! provide MVC exception handling
   2. * Swagger for front-end team -> not working
   5. !!! configure the email address. current is not working. MailAuthenticationException is thrown.
   6. !!! payment module-mock
   7. ! postman collection
   8. * use map struct instead of converters
   9. dodać dane testowe do H2, role, użytkowników itd
*/
