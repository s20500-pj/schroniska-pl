package shelter.backend.admin.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shelter.backend.admin.model.AdminShelterRequest;
import shelter.backend.admin.model.AdminShelterResponse;
import shelter.backend.admin.service.AdminService;
import shelter.backend.configuration.security.validator.KRSConstraint;
import shelter.backend.configuration.security.validator.ShelterNameConstraint;
import shelter.backend.registration.service.RegistrationShelterService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.utils.constants.ShelterPathConstants;

import java.util.List;

@RestController
@RequestMapping(value = ShelterPathConstants.ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {

    private final RegistrationShelterService registrationShelterService;
    private final AdminService adminService;

    public AdminController(RegistrationShelterService registrationShelterService, AdminService adminService) {
        this.registrationShelterService = registrationShelterService;
        this.adminService = adminService;
    }

    @GetMapping("/getShelters")
    ResponseEntity<List<AdminShelterResponse>> getShelters() {
        List<AdminShelterResponse> list = adminService.getShelters();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/enableShelters")
    ResponseEntity<List<AdminShelterResponse>> enableShelters(@RequestBody @Valid List<AdminShelterRequest> shelterList) {
        List<AdminShelterResponse> list = registrationShelterService.enableShelterAccounts(shelterList);
        return ResponseEntity.ok(list);
    }
}
