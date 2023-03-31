package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AnimalDto;

public interface AdoptionService {
    AnimalDto beginRealAdoption(Long animalId);
}
