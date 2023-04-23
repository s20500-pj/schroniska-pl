package shelter.backend.adoption.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/{adoptionId}")
    ResponseEntity<AdoptionDto2> getAdoptionById(@PathVariable @NotNull Long adoptionId) {
        return ResponseEntity.ok(shelterAdoptionService.getAdoptionById(adoptionId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getUserAdoptions/{adoptionType}")
    ResponseEntity<List<AdoptionDto2>> getUserAdoptions(@PathVariable @NotNull String adoptionType) {
        return ResponseEntity.ok(shelterAdoptionService.getUserAdoptions(adoptionType));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @PostMapping("/search")
    ResponseEntity<List<AdoptionDto2>> search(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterAdoptionService.search(searchParams));
    }

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN')")
    @GetMapping("/getAll/{adoptionType}")
    ResponseEntity<List<AdoptionDto2>> getAll(@PathVariable @NotNull AdoptionType adoptionType) {
        return ResponseEntity.ok(shelterAdoptionService.getAll(adoptionType));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/delete/{adoptionId}")
    ResponseEntity<Void> delete(@PathVariable @NotNull Long adoptionId) {
        shelterAdoptionService.delete(adoptionId);
        return ResponseEntity.noContent().build();
    }
}
