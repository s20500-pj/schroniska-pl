package shelter.backend.animals;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shelter.backend.login.JwtUtils;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

@RequiredArgsConstructor
@Service
public class AnimalService {
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
        User user = userRepository.findUserByEmail(jwtUtils.extractUsername(token));
        animal.addShelter(user);
        return animalMapper.toDto(animalRepository.save(animal));
    }

    public AnimalDto updateAnimal(AnimalDto animalDto) {
        return animalMapper.toDto(animalRepository.save(animalMapper.toEntity(animalDto)));
    }
}
