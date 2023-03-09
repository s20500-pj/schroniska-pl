package shelter.backend.rest.model.dtos;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.enums.AnimalStatus;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class AnimalDto {
    private Long id;
    private String name;
    private String information;
    private String species;
    private LocalDate age;
    private AnimalStatus animalStatus;
    private UserDto shelter;
    private List<Adoption> adoptions;
}
