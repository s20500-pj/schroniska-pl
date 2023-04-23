package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.rest.model.specification.AdoptionSpecification;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;
import shelter.backend.utils.constants.SpecificationConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class ShelterAdoptionService implements AdoptionService {

    protected final AdoptionRepository adoptionRepository;

    protected final AnimalRepository animalRepository;

    protected final UserRepository userRepository;

    protected final AdoptionMapper adoptionMapper;

    protected final EmailService emailService;

    @Override
    public List<AdoptionDto2> getAll(AdoptionType adoptionType) {
        List<Adoption> adoptionList;
        User user = getUser();
        log.debug("[getAll] :: userId: {}, userName: {}", user.getId(), user.getEmail());
        if (user.getUserType() == UserType.SHELTER) {
            adoptionList = adoptionRepository.findAdoptionByAnimal_ShelterIdAndAdoptionType(user.getId(), adoptionType);
        } else {
            adoptionList = adoptionRepository.findAll();
        }
        return adoptionMapper.toDto2List(adoptionList);
    }

    @Override
    public List<AdoptionDto2> getUserAdoptions(String adoptionType) {
        User user = getUser();
        List<Adoption> adoptionList = new ArrayList<>();
        log.debug("[getUserAdoptions] :: userId: {}, userMail: {}", user.getId(), user.getEmail());
        if (adoptionType.equals(AdoptionType.REAL.name())) {
            adoptionList = adoptionRepository.findAdoptionByUserIdAndAdoptionType(user.getId(), AdoptionType.REAL);
        } else if (adoptionType.equals(AdoptionType.VIRTUAL.name())) {
            adoptionList = adoptionRepository.findAdoptionByUserIdAndAdoptionType(user.getId(), AdoptionType.VIRTUAL);
        }
        return adoptionMapper.toDto2List(adoptionList);
    }

    @Override
    public List<AdoptionDto2> search(Map<String, String> searchParams) {
        User currentUser = getUser();
        log.debug("[search] :: searchParams: {}, userId: {}", searchParams, currentUser.getId());
        if (currentUser.getUserType() == UserType.SHELTER) {
            searchParams.put(SpecificationConstants.SHELTER_ID, currentUser.getId().toString());
        }
        AdoptionSpecification adoptionSpecification = new AdoptionSpecification(searchParams);
        return adoptionMapper.toDto2List(adoptionRepository.findAll(adoptionSpecification));
    }

    @Override
    public AdoptionDto2 getAdoptionById(Long id) {
        log.debug("[finalizeAdoption] :: adoptionId: {}, userName: {}", id, getUser().getEmail());
        Adoption adoption = adoptionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Adopcja o podanym ID nie isnieje"));
        return adoption.toDto2(adoption.getAnimal().toSimpleDto());
    }

    protected User getUser() {
        String username = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(username);
    }

    protected boolean notAlreadyAdopted(Animal animal) {
        return animal.getAdoptions().stream().noneMatch(adoption -> adoption.getAdoptionStatus() == AdoptionStatus.ADOPTED ||
                adoption.getAdoptionStatus() == AdoptionStatus.VISITED) ||
                animal.getAnimalStatus() == AnimalStatus.DEAD ||
                animal.getAnimalStatus() == AnimalStatus.DELETED ||
                animal.getAnimalStatus() == AnimalStatus.UNKNOWN;
    }

    @Override
    public void delete(Long id) {
        adoptionRepository.deleteById(id);
    }
}

