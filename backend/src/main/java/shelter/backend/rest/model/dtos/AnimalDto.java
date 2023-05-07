package shelter.backend.rest.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;
import shelter.backend.rest.model.enums.Species;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDto {
    private Long id;
    private String name;
    private String information;
    private Species species;
    private Sex sex;
    private Age age;
    private LocalDate birthDate;
    private AnimalStatus animalStatus;
    private UserDto shelter;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdoptionDto> adoptions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ActivityDto> activities;
    private Boolean sterilized;
    private Boolean vaccinated;
    private Boolean kidsFriendly;
    private Boolean couchPotato; //kanapowiec
    private Boolean needsActiveness;
    private Boolean catsFriendly;
    private Boolean dogsFriendly;
    private MultipartFile image;
    private String imagePath;
}
