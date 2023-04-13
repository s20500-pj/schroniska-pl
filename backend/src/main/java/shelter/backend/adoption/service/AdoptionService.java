package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.enums.AdoptionType;

import java.util.List;
import java.util.Map;

public interface AdoptionService {
    List<AdoptionDto2> getAll(AdoptionType adoptionType);
    /*
     * list of adoptions for specific user
     * */

    List<AdoptionDto2> getUserAdoptions(String adoptionType);

    List<AdoptionDto2> search(Map<String, String> searchParams);
    /*
     * finish the adoption process succesfully. After the user adopts the animal.
     * */

    AdoptionDto2 getAdoptionById(Long id);

    void delete(Long id);
}
