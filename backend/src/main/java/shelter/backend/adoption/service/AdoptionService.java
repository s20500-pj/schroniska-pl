package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto;

import java.util.List;
import java.util.Map;

public interface AdoptionService {
    AdoptionDto beginRealAdoption(Long animalId);

    /*
    * aproove adoption in Shelter panel and send invitation email
    * */
    List<AdoptionDto> approveRealAdoption(List<Long> adoptionIds);


    /*
     * in case when invitation email is not send. Shelter contact with user to invite him and accepts the adoption
     * */
    List<AdoptionDto> acceptManualInvitedAdoption(List<Long> adoptionIds);

    AdoptionDto declineAdoption(Long adoptionId);

    List<AdoptionDto> getAllForSpecificShleter();

    /*
     * list of adoptions for specific user
     * */
    List<AdoptionDto> getUserAdoptions();

    List<AdoptionDto> search(Map<String, String> searchParams);

    /*
     * finish the adoption process succesfully. After the user adopts the animal.
     * */
    AdoptionDto finalizeAdoption(Long id);

    AdoptionDto getAdoptionById(Long id);
}
