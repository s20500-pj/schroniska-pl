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
import shelter.backend.admin.model.AdminShelterRequestDto;
import shelter.backend.admin.model.AdminShelterResponseDto;
import shelter.backend.admin.service.AdminService;
import shelter.backend.registration.service.RegistrationShelterService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.utils.constants.ShelterPathConstants;

import java.util.List;
import java.util.Map;

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
    ResponseEntity<List<AdminShelterResponseDto>> getShelters() {
        List<AdminShelterResponseDto> list = adminService.getShelters();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/enableShelters")
    ResponseEntity<List<AdminShelterResponseDto>> enableShelters(@RequestBody @Valid List<AdminShelterRequestDto> shelterList) {
        List<AdminShelterResponseDto> list = registrationShelterService.enableShelterAccounts(shelterList);
        return ResponseEntity.ok(list);
    } //TODO do zmiany, będziemy przyjmowac listę id i zwracać listę UserDto, bez sensu jest AdminShelterResponseDto jak te pola są w UserDto

    @PostMapping("/searchUsers")
    ResponseEntity<List<UserDto>> search(@RequestBody Map<String, String> searchParams) {
        List<UserDto> all = adminService.search(searchParams);
        return ResponseEntity.ok(all);
    }
}
