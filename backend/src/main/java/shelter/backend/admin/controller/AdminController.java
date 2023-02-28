package shelter.backend.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shelter.backend.admin.service.AdminService;
import shelter.backend.registration.service.RegistrationService;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.utils.constants.ShelterPathConstants;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = ShelterPathConstants.ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final RegistrationService registrationService;
    private final AdminService adminService;

    public AdminController(RegistrationService registrationService, AdminService adminService) {
        this.registrationService = registrationService;
        this.adminService = adminService;
    }

    @GetMapping("/getShelters")
    ResponseEntity<List<UserDto>> getShelters() {
        List<UserDto> list = adminService.getShelters();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/enableShelters")
    ResponseEntity<List<UserDto>> enableShelters(@RequestBody List<Long> shelterIdList) {
        List<UserDto> list = registrationService.enableShelterAccounts(shelterIdList);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/searchUsers")
    ResponseEntity<List<UserDto>> search(@RequestBody Map<String, String> searchParams) {
        List<UserDto> all = adminService.search(searchParams);
        return ResponseEntity.ok(all);
    }
}
