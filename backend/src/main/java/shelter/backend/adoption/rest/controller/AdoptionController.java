package shelter.backend.adoption.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.enums.AdoptionType;

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
    ResponseEntity<AdoptionDto> beginRealAdoption(@PathVariable @NotNull Long animalId) {
        return ResponseEntity.ok(shelterAdoptionService.beginRealAdoption(animalId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/inviteRealAdoption/{adoptionId}")
    ResponseEntity<AdoptionDto> sendInvitationRealAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.sendInvitationRealAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/acceptManualInvited/{adoptionId}")
    ResponseEntity<AdoptionDto> acceptManualInvitedAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.acceptManualInvitedAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/confirmVisit/{adoptionId}")
    ResponseEntity<AdoptionDto> confirmVisit(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.confirmVisit(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/extendTimeForAdoption/{adoptionId}/{extendBy}")
    ResponseEntity<AdoptionDto> extendTimeForAdoption(@PathVariable @NotNull Long adoptionId, @PathVariable @NotNull Long extendBy) {
        return ResponseEntity.ok(shelterAdoptionService.extendTimeForAdoption(adoptionId, extendBy));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/real/complete/{adoptionId}")
    ResponseEntity<AdoptionDto> complete(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.finalizeRealAdoption(adoptionId));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('USER')")
    @GetMapping("/real/decline/{adoptionId}")
    ResponseEntity<AdoptionDto> declineAdoption(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.declineRealAdoption(adoptionId, false));
    }
    ///////

    ///////VIRTUAL

    ///////

    ///////BOTH
    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/{adoptionId}")
    ResponseEntity<AdoptionDto2> getAdoptionById(@PathVariable Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.getAdoptionById(adoptionId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserAdoptions/{adoptionType}")
    ResponseEntity<List<AdoptionDto2>> getUserAdoptions(@PathVariable String adoptionType) {
        return ResponseEntity.ok(shelterAdoptionService.getUserAdoptions(adoptionType));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @PostMapping("/search")
    ResponseEntity<List<AdoptionDto>> search(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterAdoptionService.search(searchParams));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/getAll/{adoptionType}")
    ResponseEntity<List<AdoptionDto2>> getAll(@PathVariable @NotNull AdoptionType adoptionType) {
        return ResponseEntity.ok(shelterAdoptionService.getAll(adoptionType));
    }

    //FIXME maaybe decouple virtual controller from real? we'll see...
}
