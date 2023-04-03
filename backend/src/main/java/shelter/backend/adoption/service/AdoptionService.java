package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AnimalDto;

import java.util.List;

public interface AdoptionService {
    AnimalDto beginRealAdoption(Long animalId);

    List<AdoptionDto> approveRealAdoption(List<Long> adoptionIds);

    List<AdoptionDto> acceptManualInvitedAdoption(List<Long> adoptionIds);

    AdoptionDto declineAdoption(Long adoptionId);

    List<AdoptionDto> getAllForSpecifigShleter();

    List<AdoptionDto> getUserAdoptions();
}
