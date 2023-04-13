package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto;

import java.util.List;
import java.util.Map;

public interface AdoptionService {
    //////REAL
    AdoptionDto beginRealAdoption(Long animalId);

    /*
    * aproove adoption in Shelter panel and send invitation email
    * */
    List<AdoptionDto> sendInvitationRealAdoption(List<Long> adoptionIds);

    /*
     * in case when invitation email is not send. Shelter contact with user to invite him and accepts the adoption
     * */
    List<AdoptionDto> acceptManualInvitedAdoption(List<Long> adoptionIds);

    /*
    * when user visits the shelter to continue adoption
    * */
    AdoptionDto confirmVisit(Long adoptionId);

    AdoptionDto declineRealAdoption(Long adoptionId, boolean declineAll);

    AdoptionDto finalizeRealAdoption(Long id);
    //////

    //////VIRTUAL
    //////

    /////BOTH
    AdoptionDto extendTimeForAdoption(Long adoptionId, Long plusWeeks);

    List<AdoptionDto> getAll();
    /*
     * list of adoptions for specific user
     * */

    List<AdoptionDto> getUserAdoptions(String adoptionType);

    List<AdoptionDto> search(Map<String, String> searchParams);
    /*
     * finish the adoption process succesfully. After the user adopts the animal.
     * */

    AdoptionDto getAdoptionById(Long id);
    /////
}
