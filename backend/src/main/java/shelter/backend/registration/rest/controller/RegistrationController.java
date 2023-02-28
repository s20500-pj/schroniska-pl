package shelter.backend.registration.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.registration.service.RegistrationShelterService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.utils.constants.ShelterPathConstants;

@RestController
@CrossOrigin("http://localhost:3000")
@Validated
@RequestMapping(value = ShelterPathConstants.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationShelterService registrationShelterService;

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationShelterService registrationShelterService, RegistrationService registrationService) {
        this.registrationShelterService = registrationShelterService;
        this.registrationService = registrationService;
    }

    @PostMapping("/registerUser")
    ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(registrationShelterService.registerShelter(userDto));
    }

    @PostMapping("/registerShelter") //TODO delete one request
    ResponseEntity<UserDto> registerShelter(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(registrationShelterService.registerShelter(userDto));
    }

    @GetMapping("/confirmation")
    Boolean confirmToken(@RequestParam(name = "token") String token) {
        return registrationService.confirmToken(token);
    }
}
