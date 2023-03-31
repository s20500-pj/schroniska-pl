package shelter.backend.animals.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shelter.backend.login.JwtUtils;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.rest.model.specification.AnimalSpecification;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ShelterAnimalService implements AnimalService{
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AnimalDto getAnimalById(Long id) {
        return animalMapper.toDto(animalRepository.findAnimalById(id));
    }

    public AnimalDto addAnimalToShelter(AnimalDto animalDto) {
        String token = ClientInterceptor.getBearerTokenHeader();
        Animal animal = animalMapper.toEntity(animalDto);
        User user = userRepository.findUserByEmail(jwtUtils.extractUsername(token.substring(7)));
        animal.addShelter(user);
        animal = animalRepository.save(animal);
        user.getAnimals().add(animal);
        userRepository.save(user);
        return animalMapper.toDto(animal);
    }

    public AnimalDto updateAnimal(AnimalDto animalDto) {
        return animalMapper.toDto(animalRepository.save(animalMapper.toEntity(animalDto)));
    }

    public void deleteAnimal(Long animalId) {
        Animal animal = animalRepository.findAnimalById(animalId);
        if (animal != null) {
            animal.setAnimalStatus(AnimalStatus.DELETED);
            animalRepository.save(animal);
        } else {
            throw new EntityNotFoundException("Zwierzę o podanym id nie istenieje");
        }
    }

    public List<AnimalDto> search(Map<String, String> searchParams) {
        AnimalSpecification animalSpecification = new AnimalSpecification(searchParams);
        return animalMapper.toDtoList(animalRepository.findAll(animalSpecification));
    }
}
