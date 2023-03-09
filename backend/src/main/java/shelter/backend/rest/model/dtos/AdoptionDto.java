package shelter.backend.rest.model.dtos;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;

@Builder
@Data
public class AdoptionDto {

    private Long id;
    private AdoptionType adoptionType;
    private AdoptionStatus adoptionStatus;
    private User user;
    private Animal animal;
}
