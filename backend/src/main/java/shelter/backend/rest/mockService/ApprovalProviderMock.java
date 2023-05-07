package shelter.backend.rest.mockService;

import shelter.backend.registration.service.ApprovalProvider;
import shelter.backend.rest.model.entity.Address;


public class ApprovalProviderMock implements ApprovalProvider {
    @Override
    public boolean validateShelterDetails(String krs, String companyName) {
        return true;
    }
}
