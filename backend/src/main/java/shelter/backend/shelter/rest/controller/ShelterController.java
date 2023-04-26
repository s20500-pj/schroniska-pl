package shelter.backend.shelter.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.PayUClientCredentialsDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.shelter.service.DefaultShelterService;
import shelter.backend.shelter.service.ShelterService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/shelter")
public class ShelterController {

    private final RegistrationService registrationService;
    private final ShelterService shelterService;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getShelterById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }

    @PostMapping(value = "/searchShelters", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDto>> searchShelters(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterService.searchShelters(searchParams));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> enableShelters(@RequestBody PayUClientCredentialsDto payUClientCredentialsDto) {
        return ResponseEntity.ok(registrationService.enableShelterAccounts(payUClientCredentialsDto));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(shelterService.update(userDto));
    }
}