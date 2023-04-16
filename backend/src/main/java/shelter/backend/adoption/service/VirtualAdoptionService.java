package shelter.backend.adoption.service;

public interface VirtualAdoptionService extends AdoptionService {

    String beginVirtualAdoption(long animalId, long amount);

}
