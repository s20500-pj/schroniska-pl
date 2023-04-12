package shelter.backend.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String AUTHORIZATION_COOKIE = "Authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(AUTHORIZATION_COOKIE, authorizationCoockieScheme()))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION_COOKIE))
                .info(new Info()
                        .title("Schroniska.pl")
                );
    }

    private SecurityScheme authorizationCoockieScheme() {
        SecurityScheme securityRequirement = new SecurityScheme();
        return securityRequirement
                .name(AUTHORIZATION_COOKIE)
                .type(SecurityScheme.Type.APIKEY)
                .description("Every endpoint's call (except endpoints in Authentication and Registration Controller) requires Authorization Cookie")
                .in(SecurityScheme.In.COOKIE);
    }
}

