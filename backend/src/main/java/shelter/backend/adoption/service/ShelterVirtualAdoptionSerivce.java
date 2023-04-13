package shelter.backend.adoption.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
@Service
@Slf4j
public class ShelterVirtualAdoptionSerivce extends ShelterAdoptionService implements VirtualAdoptionService{
    public ShelterVirtualAdoptionSerivce(AdoptionRepository adoptionRepository, AnimalRepository animalRepository, UserRepository userRepository, AdoptionMapper adoptionMapper, EmailService emailService) {
        super(adoptionRepository, animalRepository, userRepository, adoptionMapper, emailService);
    }

}
