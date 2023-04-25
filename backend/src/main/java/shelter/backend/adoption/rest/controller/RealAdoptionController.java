package shelter.backend.adoption.rest.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.RealAdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto2;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class RealAdoptionController {

    private final RealAdoptionService realAdoptionService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/real/{animalId}")
    ResponseEntity<AdoptionDto2> beginRealAdoption(@PathVariable @NotNull Long animalId) {
        return ResponseEntity.ok(realAdoptionService.beginRealAdoption(animalId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/inviteRealAdoption/{adoptionId}")
    ResponseEntity<AdoptionDto2> sendInvitationRealAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(realAdoptionService.sendInvitationRealAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/acceptManualInvited/{adoptionId}")
    ResponseEntity<AdoptionDto2> acceptManualInvitedAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(realAdoptionService.acceptManualInvitedAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/confirmVisit/{adoptionId}")
    ResponseEntity<AdoptionDto2> confirmVisit(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(realAdoptionService.confirmVisit(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/extendTimeForAdoption/{adoptionId}/{extendBy}")
    ResponseEntity<AdoptionDto2> extendTimeForAdoption(@PathVariable @NotNull Long adoptionId, @PathVariable @NotNull Long extendBy) {
        return ResponseEntity.ok(realAdoptionService.extendTimeForAdoption(adoptionId, extendBy));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/complete/{adoptionId}")
    ResponseEntity<AdoptionDto2> complete(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(realAdoptionService.finalizeRealAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('USER')")
    @GetMapping("/real/decline/{adoptionId}")
    ResponseEntity<AdoptionDto2> declineAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(realAdoptionService.declineRealAdoption(adoptionId, false));
    }
}
