package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.storage.repository.AdoptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AdoptionMapper implements DtoEntityMapper<Adoption, AdoptionDto> {

    private final AdoptionRepository adoptionRepository;

    @Override
    public Adoption toEntity(AdoptionDto adoptionDto) {
        return Optional.ofNullable(adoptionDto.getId())
                .flatMap(adoptionRepository::findById)
                .map(entity -> entity.toEntity(adoptionDto))
                .orElseGet(() -> new Adoption().toEntity(adoptionDto));
    }

    @Override
    public AdoptionDto toDto(Adoption adoption) {
        return adoption.toDto();
    }

    public AdoptionDto2 toDto2(Adoption adoption) {
        AnimalDto animalDto = getAnimalDtoNullifyAdoptionsAndActivities(adoption);
        return adoption.toDto2(animalDto);
    }

    public List<AdoptionDto2> toDto2List(List<Adoption> adoptions) {
        List<AdoptionDto2> adoptionDto2s = new ArrayList<>();
        adoptions.forEach(adoption -> {
            AnimalDto animalDto = getAnimalDtoNullifyAdoptionsAndActivities(adoption);
            adoptionDto2s.add(adoption.toDto2(animalDto));
        });
        return adoptionDto2s;
    }

    private AnimalDto getAnimalDtoNullifyAdoptionsAndActivities(Adoption adoption) {
        AnimalDto animalDto = adoption.getAnimal().toSimpleDto();
        animalDto.setAdoptions(null);
        animalDto.setActivities(null);
        return animalDto;
    }
}

