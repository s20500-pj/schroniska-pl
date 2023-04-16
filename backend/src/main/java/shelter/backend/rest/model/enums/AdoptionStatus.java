package shelter.backend.rest.model.enums;

public enum AdoptionStatus {

    //REAL
    REQUEST_REVIEW,
    REQUIRES_MANUAL_INVITATION, // email not sent, shelter need to contact the user
    PENDING, // suspend during manual invitation
    SHELTER_INVITED, //invited by shelter
    PENDING_SHELTER_INVITED, // suspend during shelter invitation
    VISITED, // shelter visited by the user
    DECLINED,
    ADOPTED,
    //VIRTUAL
    VIRTUAL_PENDING,
    VIRTUAL_DECLINED,
    VIRTUAL_ADOPTED
}
