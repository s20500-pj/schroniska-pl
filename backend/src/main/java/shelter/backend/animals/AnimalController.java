package shelter.backend.animals;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shelter.backend.rest.model.dtos.AnimalDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/animal")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/animal/{id}")
    ResponseEntity<AnimalDto> getAnimalById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    @PostMapping("/animal/add")
    ResponseEntity<AnimalDto> addAnimalToShelter(@RequestBody AnimalDto animalDto){
        return ResponseEntity.ok(animalService.addAnimalToShelter(animalDto));
    }

    @PutMapping("/animal/update")
    ResponseEntity<AnimalDto> updateAnimal(@RequestBody AnimalDto animalDto){
        return ResponseEntity.ok(animalService.updateAnimal(animalDto));
    }
}
