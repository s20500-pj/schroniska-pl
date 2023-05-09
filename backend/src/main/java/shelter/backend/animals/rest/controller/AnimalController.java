package shelter.backend.animals.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shelter.backend.animals.service.AnimalService;
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

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping(value = "/add")
    public ResponseEntity<AnimalDto> addAnimalToShelter(@RequestPart("animal") AnimalDto animalDto,
                                                        @RequestParam(name = "image", required = false) MultipartFile image) {
        if (image != null) {
            animalDto.setImage(image);
        }
        return ResponseEntity.ok(animalService.addAnimalToShelter(animalDto));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PutMapping("/update")
    ResponseEntity<AnimalDto> updateAnimal(@RequestPart("animal") AnimalDto animalDto,
                                           @RequestParam(name = "image", required = false) MultipartFile image) {
        if (image != null) {
            animalDto.setImage(image);
        }
        return ResponseEntity.ok(animalService.updateAnimal(animalDto));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @DeleteMapping("/delete/{animalId}")
    ResponseEntity<Void> deleteAnimal(@PathVariable @NotNull Long animalId, HttpServletRequest request, HttpServletResponse response) {
        animalService.deleteAnimal(animalId, request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AnimalDto>> search(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(animalService.search(searchParams));
    }

    @PreAuthorize("hasRole('SHELTER')")
    @PostMapping(value = "/getShelterAnimals", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AnimalDto>> getShelterAnimals(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(animalService.getShelterAnimals(searchParams));
    }
}
