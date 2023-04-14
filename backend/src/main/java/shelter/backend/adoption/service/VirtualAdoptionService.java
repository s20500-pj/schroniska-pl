package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto;

public interface VirtualAdoptionService extends AdoptionService {

    AdoptionDto beginVirtualAdoption(long animalId, long amount);

}
