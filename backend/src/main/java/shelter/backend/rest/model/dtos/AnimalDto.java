package shelter.backend.rest.model.dtos;

import lombok.Builder;
import lombok.Data;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class AnimalDto {
    private Long id;
    private String name;
    private String information;
    private String species;
    private Sex sex;
    private Age age;
    private LocalDate birthDate;
    private AnimalStatus animalStatus;
    private UserDto shelter;
    private List<Adoption> adoptions;
    private boolean sterilized;
    private boolean vaccinated;
    private boolean kidsFriendly;
    private boolean couchPotato; //kanapowiec
    private boolean needsActiveness;
    private boolean catsFriendly;
    private boolean dogsFriendly;
}
