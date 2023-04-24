package shelter.backend.adoption.rest.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto2;

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

    @PreAuthorize("hasRole('SHELTER') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/getAdoptions")
    ResponseEntity<List<AdoptionDto2>> search(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(shelterAdoptionService.getAdoptions(searchParams));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @GetMapping("/delete/{adoptionId}")
    ResponseEntity<Void> delete(@PathVariable @NotNull Long adoptionId) {
        shelterAdoptionService.delete(adoptionId);
        return ResponseEntity.noContent().build();
    }
}
