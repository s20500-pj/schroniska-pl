package shelter.backend.animals.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.mapper.AnimalMapper;
import shelter.backend.rest.model.specification.AnimalSpecification;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShelterAnimalService implements AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final UserRepository userRepository;

    public AnimalDto getAnimalById(Long id) {
        return animalMapper.toDto(animalRepository.findAnimalById(id));
    }

    @Transactional
    public AnimalDto addAnimalToShelter(AnimalDto animalDto) {
        Animal animal = animalMapper.toEntity(animalDto);
        User user = getLoggedUser();
        animal.addShelter(user);
        animal = animalRepository.save(animal);
        user.getAnimals().add(animal);

        if (animalDto.getImage() != null) {
            animal.setImagePath(handleImageUpload(animalDto.getImage()));
        }

        userRepository.save(user);
        return animalMapper.toDto(animal);
    }

    private String handleImageUpload(MultipartFile file) {
        try {
            log.info("File upload started {}", file.getOriginalFilename());
            String fileName = file.getOriginalFilename();
            String filePath = new File("").getAbsolutePath();
            File imageFile = new File(filePath + "/frontend/public/images/" + fileName);
            file.transferTo(imageFile);
            log.info("File successfully saved at {}", "images/" + fileName);
            return "images/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading image";
        }
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

    public List<AnimalDto> search(String searchParams) {
        AnimalSpecification animalSpecification = new AnimalSpecification(parseSearchParams(searchParams));
        return animalMapper.toDtoList(animalRepository.findAll(animalSpecification));
    }

    public List<AnimalDto> getShelterAnimals(String searchParams) {
        Map<String, String> parsedSearchParams = parseSearchParams(searchParams);
        parsedSearchParams.put("shelterId", getLoggedUser().getId().toString());
        AnimalSpecification animalSpecification = new AnimalSpecification(parsedSearchParams);
        return animalMapper.toDtoList(animalRepository.findAll(animalSpecification));
    }

    static public Map<String, String> parseSearchParams(String searchParams) {
        Map<String, String> params = new HashMap<>();

        JSONObject jsonObject = new JSONObject(searchParams);
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.getString(key);
            key = key.replaceAll("^\"|\"$", "");
            params.put(key, value);
        }

        return params;
    }

    private User getLoggedUser() {
        String currentUsername = ClientInterceptor.getCurrentUsername();
        return userRepository.findUserByEmail(currentUsername);
    }
}
