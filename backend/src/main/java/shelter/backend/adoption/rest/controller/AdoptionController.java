package shelter.backend.adoption.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.AdoptionService;
import shelter.backend.rest.model.dtos.AnimalDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class AdoptionController {

    private final AdoptionService shelterAdoptionService;

    @PostMapping("/real/{animalId}")
    ResponseEntity<AnimalDto> addAnimalToShelter(@PathVariable Long animalId) {
        return ResponseEntity.ok(shelterAdoptionService.beginRealAdoption(animalId));
    }
}
