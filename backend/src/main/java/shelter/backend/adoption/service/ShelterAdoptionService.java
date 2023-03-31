package shelter.backend.adoption.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.exception.AdoptionException;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterAdoptionService implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalDto beginRealAdoption(Long animalId) {
        log.debug("Begin real adoption for animalId {}", animalId);
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            if (notWaitForRealAdoption(animal)) {
                registerRealAdoption(animal);
                return animalMapper.toDto(animal);
            } else {
                log.info("Animal awaits for real adoption. Animal id: {}", animalId);
                throw new AdoptionException("Zwierzę oczekuję na adopcję realną");
            }

        } else throw new AdoptionException("Zwierzę nie istnieje dla wybranego id");
    }

    private void registerRealAdoption(Animal animal) {
        User user = getUser();
        Adoption adoption = Adoption.builder()
                .adoptionType(AdoptionType.REAL)
                .adoptionStatus(AdoptionStatus.REQUEST_REVIEW)
                .animal(animal)
                .user(user)
                .build();
        adoptionRepository.save(adoption);
        animal.getAdoptions().add(adoption);
        animalRepository.save(animal);
    }

    private User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        User user = userRepository.findUserByEmail(username);
        return user;
    }

    private boolean notWaitForRealAdoption(Animal animal) {
        return animal.getAdoptions().stream().noneMatch(adoption -> adoption.getAdoptionType().equals(AdoptionType.REAL));
    }
}

//TODO shendlować jakoś case gdy juź jest zadaptowany realnie, co zrobic z Virtual ->(np. wyczyscic, gdy juz potwierdz to w schronisku ->
// jakis endpoint zrobic na potwierzenie ze zostal zaadaptowany)
