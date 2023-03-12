package shelter.backend.shelter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.UserDto;

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

    @PostMapping(value = "/searchShelters", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDto>> searchShelters(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterService.searchShelters(searchParams));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDto>> enableShelters(@RequestBody @Valid List<Long> shelterIds) {
        return ResponseEntity.ok(registrationService.enableShelterAccounts(shelterIds));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(shelterService.update(userDto));
    }
}