package shelter.backend.rest.model.dtos;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;

@Builder
@Data
public class AdoptionDto {

    private Long id;
    private AdoptionType adoptionType;
    private AdoptionStatus adoptionStatus;
    private UserDto user;
    private AnimalDto animal;
}
