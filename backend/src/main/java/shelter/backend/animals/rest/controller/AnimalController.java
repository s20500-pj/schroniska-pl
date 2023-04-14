package shelter.backend.animals.rest.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shelter.backend.animals.service.AnimalService;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/animal")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("/{id}")
    ResponseEntity<AnimalDto> getAnimalById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    @PostMapping(value = "/add")
    public ResponseEntity<AnimalDto> addAnimalToShelter(
            @RequestPart("animal") AnimalDto animalDto,
            @RequestParam(name = "image", required = false) MultipartFile image) {
        if (image != null) {
            animalDto.setImage(image);
        }
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

    @PostMapping(value = "/search", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<List<AnimalDto>> search(@RequestBody String searchParams) {
        return ResponseEntity.ok(animalService.search(searchParams));
    }

    @PostMapping(value = "/getShelterAnimals", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<List<AnimalDto>> getShelterAnimals(@RequestBody String searchParams) {
        return ResponseEntity.ok(animalService.getShelterAnimals(searchParams));
    }
}
