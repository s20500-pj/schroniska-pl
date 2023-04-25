package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto2;

import java.util.List;
import java.util.Map;

public interface AdoptionService {

    List<AdoptionDto2> getAdoptions(Map<String, String> searchParams);

    AdoptionDto2 getAdoptionById(Long id);

    void delete(Long id);
}
