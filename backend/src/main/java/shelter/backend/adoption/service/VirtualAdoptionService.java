package shelter.backend.adoption.service;

public interface VirtualAdoptionService extends AdoptionService {

    String beginVirtualAdoption(Long animalId, Long amount);

    void finalizeVirtualAdoption(Long id, Long amount);
}
