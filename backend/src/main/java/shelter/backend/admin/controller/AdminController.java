package shelter.backend.admin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.admin.service.AdminService;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.UserDto;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final RegistrationService registrationService;
    private final AdminService adminService;

    @GetMapping("/user/all")
    ResponseEntity<List<UserDto>> getShelters() {
        return ResponseEntity.ok(adminService.getShelters());
    }

    @GetMapping("/user/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PostMapping("/user/search")
    ResponseEntity<List<UserDto>> search(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(adminService.search(searchParams));
    }

    @PostMapping("/shelter/enable")
    ResponseEntity<List<UserDto>> enableShelters(@RequestBody @Valid List<Long> shelterIds) {
        return ResponseEntity.ok(registrationService.enableShelterAccounts(shelterIds));
    }
}
