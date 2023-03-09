package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.mapper.DtoEntityMapper;
import shelter.backend.storage.repository.AnimalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AnimalMapper implements DtoEntityMapper<Animal, AnimalDto> {

    private final AnimalRepository animalRepository;

    @Override
    public Animal toEntity(AnimalDto animalDto) {
        return Optional.ofNullable(animalDto.getId())
                .flatMap(animalRepository::findById)
                .map(entity -> entity.toEntity(animalDto))
                .orElseGet(() -> new Animal().toEntity(animalDto));
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
