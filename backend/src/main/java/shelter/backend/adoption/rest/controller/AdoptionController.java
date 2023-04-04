package shelter.backend.adoption.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class AdoptionController {

    private final AdoptionService shelterAdoptionService;

    ///////REAL
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/real/{animalId}")
    ResponseEntity<AdoptionDto> beginRealAdoption(@PathVariable Long animalId) {
        return ResponseEntity.ok(shelterAdoptionService.beginRealAdoption(animalId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/real/approveAdoption")
    ResponseEntity<List<AdoptionDto>> approveRealAdoption(@RequestBody List<Long> adoptionIds) {
        return ResponseEntity.ok(shelterAdoptionService.sendInvitationRealAdoption(adoptionIds));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/real/acceptManualInvited")
    ResponseEntity<List<AdoptionDto>> acceptManualInvitedAdoption(@RequestBody List<Long> adoptionIds) {
        return ResponseEntity.ok(shelterAdoptionService.acceptManualInvitedAdoption(adoptionIds));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/real/confirmVisit/{adoptionId}")
    ResponseEntity<AdoptionDto> confirmVisit(@PathVariable Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.confirmVisit(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/real/extendTimeForAdoption/{adoptionId}/{extendBy}")
    ResponseEntity<AdoptionDto> extendTimeForAdoption(@PathVariable Long adoptionId, @PathVariable Long extendBy) {
        return ResponseEntity.ok(shelterAdoptionService.extendTimeForAdoption(adoptionId, extendBy));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/getAll")
    ResponseEntity<List<AdoptionDto>> getAll() {
        return ResponseEntity.ok(shelterAdoptionService.getAll());
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/real/complete/{adoptionId}")
    ResponseEntity<AdoptionDto> complete(@PathVariable Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.finalizeRealAdoption(adoptionId));
    }
    ///////

    ///////VIRTUAL

    ///////

    ///////BOTH
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{adoptionId}")
    ResponseEntity<AdoptionDto> getAdoptionById(@PathVariable Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.getAdoptionById(adoptionId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserAdoptions")
    ResponseEntity<List<AdoptionDto>> getUserAdoptions() {
        return ResponseEntity.ok(shelterAdoptionService.getUserAdoptions());
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/decline/{adoptionId}")
    ResponseEntity<AdoptionDto> declineAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.declineRealAdoption(adoptionId, false));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @PostMapping("/search")
    ResponseEntity<List<AdoptionDto>> search(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterAdoptionService.search(searchParams));
    }

    //FIXME maaybe decouple virtual controller from real? we'll see...
}
