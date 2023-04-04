package shelter.backend.animals.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.animals.service.AnimalService;
import shelter.backend.animals.service.ShelterAnimalService;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/animal")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/{id}")
    ResponseEntity<AnimalDto> getAnimalById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    @PostMapping("/add")
    ResponseEntity<AnimalDto> addAnimalToShelter(@RequestBody AnimalDto animalDto) {
        return ResponseEntity.ok(animalService.addAnimalToShelter(animalDto));
    }

    @PutMapping("/update")
    ResponseEntity<AnimalDto> updateAnimal(@RequestBody AnimalDto animalDto) {
        return ResponseEntity.ok(animalService.updateAnimal(animalDto));
    }

    @PutMapping("/delete/{animalId}")
    ResponseEntity<Void> deleteAnimal(@PathVariable @NotNull Long animalId) {
        animalService.deleteAnimal(animalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    ResponseEntity<List<AnimalDto>> search(@RequestBody @Valid Map<String, String> searchParams) {
        return ResponseEntity.ok(animalService.search(searchParams));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dupa")
    ResponseEntity<String> dupa(){
        return ResponseEntity.ok("dupa");
    }
}
