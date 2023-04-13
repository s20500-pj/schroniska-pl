package shelter.backend.animals.service;

import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;
import java.util.Map;

public interface AnimalService {
    AnimalDto getAnimalById(Long id);
    AnimalDto addAnimalToShelter(AnimalDto animalDto);
    AnimalDto updateAnimal(AnimalDto animalDto);
    void deleteAnimal(Long animalId);
    List<AnimalDto> search(String searchParams);
    List<AnimalDto> getShelterAnimals(String searchParams);
}




