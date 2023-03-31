package shelter.backend.rest.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionDto {

    private Long id;
    private AdoptionType adoptionType;
    private AdoptionStatus adoptionStatus;
    private UserDto user;
    private AnimalDto animal;
}
