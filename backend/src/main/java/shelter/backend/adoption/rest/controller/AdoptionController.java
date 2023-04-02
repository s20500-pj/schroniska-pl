package shelter.backend.adoption.rest.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Adoption;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class AdoptionController {

    private final AdoptionService shelterAdoptionService;
    ///////REAL
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/real/{animalId}")
    ResponseEntity<AnimalDto> beginRealAdoption(@PathVariable Long animalId) {
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
        return ResponseEntity.ok(shelterAdoptionService.getAll());
    }
    //TODO ADD ADOPTED CONTROLLER (finishadoption)
    ///////
    ///////VIRTUAL

    ///////
    ///////BOTH
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserAdoptions/{userId}")
    ResponseEntity<List<AdoptionDto>> getUserAdoptions(@PathVariable Long userId) {
        return ResponseEntity.ok(shelterAdoptionService.getUserAdoptions(userId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @DeleteMapping("/decline/{adoptionId}")
    ResponseEntity<AdoptionDto> declineAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.declineAdoption(adoptionId));
    }
    ///////

    //FIXME maaybe decouple virtual controller from real? we'll see...
}
