package shelter.backend.registration.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.UserDto;

import java.net.URI;

@RestController
@Validated
@RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;

    @Value("${shelter.web.security.friendly-origin-url}")
    private String friendlyOriginUrl;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @SecurityRequirements()
    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(registrationService.register(userDto));
    }

    @SecurityRequirements()
    @GetMapping("/confirmation")
    ResponseEntity<Boolean> confirmToken(@RequestParam(name = "token") String token) {
        registrationService.confirmToken(token);
        URI location = URI.create(friendlyOriginUrl + "/confirmation"); //todo migrate react builds to springs's static resources
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }
}
