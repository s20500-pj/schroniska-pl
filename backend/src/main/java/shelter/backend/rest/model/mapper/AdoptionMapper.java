package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.storage.repository.AdoptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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

    public List<AdoptionDto> toDtoList(List<Adoption> adoptions) {
        List<AdoptionDto> adoptionDtos = new ArrayList<>();
        adoptions.forEach(adoption -> adoptionDtos.add(adoption.toDto()));
        return adoptionDtos;
    }
}