package shelter.backend.registration.service;

import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;

public interface ApprovalProvider {

    boolean validateShelterDetails(String krs, String companyName);

}
