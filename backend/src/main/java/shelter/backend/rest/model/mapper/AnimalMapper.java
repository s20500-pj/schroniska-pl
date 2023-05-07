package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.storage.repository.AnimalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalMapper implements DtoEntityMapper<Animal, AnimalDto> {

    private final AnimalRepository animalRepository;

    @Override
    public Animal toEntity(AnimalDto animalDto) {
        return Optional.ofNullable(animalDto.getId())
                .flatMap(animalRepository::findById)
                .map(entity -> entity.toEntity(animalDto))
                .orElseGet(() -> new Animal().toEntity(animalDto));
    }

    public Animal updateEntity(Animal animal, AnimalDto animalDto) {
        if (animalDto.getName() != null) animal.setName(animalDto.getName());
        if (animalDto.getInformation() != null) animal.setInformation(animalDto.getInformation());
        if (animalDto.getSpecies() != null) animal.setSpecies(animalDto.getSpecies());
        if (animalDto.getSex() != null) animal.setSex(animalDto.getSex());
        if (animalDto.getAge() != null) animal.setAge(animalDto.getAge());
        if (animalDto.getBirthDate() != null) animal.setBirthDate(animalDto.getBirthDate());
        if (animalDto.getAnimalStatus() != null) animal.setAnimalStatus(animalDto.getAnimalStatus());
        if (animalDto.getSterilized() != null) animal.setSterilized(animalDto.getSterilized());
        if (animalDto.getVaccinated() != null) animal.setVaccinated(animalDto.getVaccinated());
        if (animalDto.getKidsFriendly() != null) animal.setKidsFriendly(animalDto.getKidsFriendly());
        if (animalDto.getCouchPotato() != null) animal.setCouchPotato(animalDto.getCouchPotato());
        if (animalDto.getNeedsActiveness() != null) animal.setNeedsActiveness(animalDto.getNeedsActiveness());
        if (animalDto.getCatsFriendly() != null) animal.setCatsFriendly(animalDto.getCatsFriendly());
        if (animalDto.getDogsFriendly() != null) animal.setDogsFriendly(animalDto.getDogsFriendly());
        if (animalDto.getImagePath() != null) animal.setImagePath(animalDto.getImagePath());

        return animal;
    }

    @Override
    public AnimalDto toDto(Animal animal) {
        return animal.toSimpleDto();
    }

    public List<AnimalDto> toDtoList(List<Animal> animals) {
        List<AnimalDto> animalDtos = new ArrayList<>();
        animals.forEach(animal -> animalDtos.add(animal.toSimpleDto()));
        return animalDtos;
    }
}
