package shelter.backend.animals.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;

public interface AnimalService {
    AnimalDto getAnimalById(Long id);
    AnimalDto addAnimalToShelter(AnimalDto animalDto);
    AnimalDto updateAnimal(AnimalDto animalDto);
    void deleteAnimal(Long animalId, HttpServletRequest request, HttpServletResponse response);
    List<AnimalDto> search(String searchParams);
    List<AnimalDto> getShelterAnimals(String searchParams);
}





