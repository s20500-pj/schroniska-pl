package shelter.backend.registration.rest.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shelter.backend.configuration.security.validator.KRSConstraint;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.registration.service.RegistrationShelterService;
import shelter.backend.registration.service.RegistrationUserService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.configuration.security.validator.FirstNameConstraint;
import shelter.backend.configuration.security.validator.LastNameConstraint;
import shelter.backend.configuration.security.validator.ShelterNameConstraint;
import shelter.backend.utils.constants.ShelterPathConstants;

import java.util.List;


@RestController
@Validated
@RequestMapping(value = ShelterPathConstants.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationUserService registrationUserService;
    private final RegistrationShelterService registrationShelterService;

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationUserService registrationUserService, RegistrationShelterService registrationShelterService, RegistrationService registrationService) {
        this.registrationUserService = registrationUserService;
        this.registrationShelterService = registrationShelterService;
        this.registrationService = registrationService;
    }

    @PostMapping("/registerUser")
    ResponseEntity<List<UserDto>> registerUser(@RequestBody @Valid @FirstNameConstraint @LastNameConstraint UserDto userDto){
        List<UserDto> list =  registrationUserService.registerUser(userDto);
        return ResponseEntity.ok(list);
    }


    @PostMapping("/registerShelter")
    ResponseEntity<List<UserDto>> registerShelter(@RequestBody @Valid @ShelterNameConstraint @KRSConstraint UserDto userDto){
        List<UserDto> list = registrationShelterService.registerShelter(userDto);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/confirmation")
    Boolean confirmToken(@RequestParam(name = "token") String token) {
        return registrationService.confirmToken(token);
    }
}
