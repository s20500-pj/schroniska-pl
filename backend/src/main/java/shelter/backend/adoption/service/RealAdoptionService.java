package shelter.backend.adoption.service;

import shelter.backend.rest.model.dtos.AdoptionDto2;

public interface RealAdoptionService extends AdoptionService {

    AdoptionDto2 beginRealAdoption(Long animalId);

    /*
     * aproove adoption in Shelter panel and send invitation email
     * */
    AdoptionDto2 sendInvitationRealAdoption(Long adoptionId);

    /*
     * in case when invitation email is not send. Shelter contact with user to invite him and accepts the adoption
     * */
    AdoptionDto2 acceptManualInvitedAdoption(Long adoptionId);

    /*
     * when user visits the shelter to continue adoption
     * */
    AdoptionDto2 confirmVisit(Long adoptionId);

    AdoptionDto2 declineRealAdoption(Long adoptionId, boolean declineAll);

    AdoptionDto2 finalizeRealAdoption(Long id);

    AdoptionDto2 extendTimeForAdoption(Long adoptionId, Long plusWeeks);

}
