package shelter.backend.animals.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;
import java.util.Map;

public interface AnimalService {
    AnimalDto getAnimalById(Long id);
    AnimalDto addAnimalToShelter(AnimalDto animalDto);
    AnimalDto updateAnimal(AnimalDto animalDto);
    void deleteAnimal(Long animalId, HttpServletRequest request, HttpServletResponse response);
    List<AnimalDto> search(Map<String, String> searchParams);
    List<AnimalDto> getShelterAnimals(@RequestBody Map<String, String> searchParams);
}





