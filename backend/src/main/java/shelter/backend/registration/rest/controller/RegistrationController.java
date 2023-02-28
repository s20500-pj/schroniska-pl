package shelter.backend.registration.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.utils.constants.ShelterPathConstants;

@RestController
@CrossOrigin("http://localhost:3000")
@Validated
@RequestMapping(value = ShelterPathConstants.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(registrationService.register(userDto));
    }

    @GetMapping("/confirmation")
    Boolean confirmToken(@RequestParam(name = "token") String token) {
        return registrationService.confirmToken(token);
    }
}
