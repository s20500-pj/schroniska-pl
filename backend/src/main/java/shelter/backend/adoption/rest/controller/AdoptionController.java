package shelter.backend.adoption.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AnimalDto;

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
        return ResponseEntity.ok(shelterAdoptionService.approveRealAdoption(adoptionIds));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/real/acceptManualInvited")
    ResponseEntity<List<AdoptionDto>> acceptManualInvitedAdoption(@RequestBody List<Long> adoptionIds) {
        return ResponseEntity.ok(shelterAdoptionService.acceptManualInvitedAdoption(adoptionIds));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/getAll")
    ResponseEntity<List<AdoptionDto>> getAll() {
        return ResponseEntity.ok(shelterAdoptionService.getAllForSpecificShleter());
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/real/complete/{id}")
    ResponseEntity<AdoptionDto> complete(@PathVariable Long id) {
        return ResponseEntity.ok(shelterAdoptionService.finalizeAdoption(id));
    }
    ///////

    ///////VIRTUAL

    ///////

    ///////BOTH
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    ResponseEntity<AdoptionDto> getAdoptionById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterAdoptionService.getAdoptionById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserAdoptions")
    ResponseEntity<List<AdoptionDto>> getUserAdoptions() {
        return ResponseEntity.ok(shelterAdoptionService.getUserAdoptions());
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/decline/{adoptionId}")
    ResponseEntity<AdoptionDto> declineAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.declineAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @PostMapping("/search")
    ResponseEntity<List<AdoptionDto>> search(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterAdoptionService.search(searchParams));
    }

    //FIXME maaybe decouple virtual controller from real? we'll see...
}
