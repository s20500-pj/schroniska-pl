package shelter.backend.adoption.rest.controller;

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

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/real/{animalId}")
    ResponseEntity<AnimalDto> beginRealAdoption(@PathVariable Long animalId) {
        return ResponseEntity.ok(shelterAdoptionService.beginRealAdoption(animalId));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping("/real/approveAdoption")
    ResponseEntity<List<AdoptionDto>> approveRealAdoption(@RequestBody List<Long> adoptionIds){
        return ResponseEntity.ok(shelterAdoptionService.approveRealAdoption(adoptionIds));
    }

    //FIXME maaybe decouple virtual controller from real? we'll see
}
