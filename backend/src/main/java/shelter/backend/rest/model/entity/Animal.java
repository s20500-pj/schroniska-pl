package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "animals")
@Getter
@Setter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String information;
    private String species;
    private Sex sex;
    private Age age;
    private LocalDate birthDate;
    private AnimalStatus animalStatus;
    private boolean sterilized;
    private boolean vaccinated;
    private boolean kidsFriendly;
    private boolean couchPotato; //kanapowiec
    private boolean needsActiveness;
    private boolean catsFriendly;
    private boolean dogsFriendly;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User shelter;

    @OneToMany(mappedBy = "animal")
    private List<Adoption> adoptions = new ArrayList<>();

    public Animal toEntity(AnimalDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.information = dto.getInformation();
        this.species = dto.getSpecies();
        this.sex = dto.getSex();
        this.age = dto.getAge();
        this.birthDate = dto.getBirthDate();
        this.animalStatus = dto.getAnimalStatus();
        this.sterilized = dto.isSterilized();
        this.vaccinated = dto.isVaccinated();
        this.kidsFriendly = dto.isKidsFriendly();
        this.couchPotato = dto.isCouchPotato();
        this.needsActiveness = dto.isNeedsActiveness();
        this.catsFriendly = dto.isCatsFriendly();
        this.dogsFriendly = dto.isDogsFriendly();
        return this;
    }

    public AnimalDto toSimpleDto() {
        return AnimalDto.builder()
                .id(id)
                .name(name)
                .information(information)
                .species(species)
                .sex(sex)
                .age(age)
                .birthDate(birthDate)
                .animalStatus(animalStatus)
                .sterilized(sterilized)
                .vaccinated(vaccinated)
                .kidsFriendly(kidsFriendly)
                .couchPotato(couchPotato)
                .needsActiveness(needsActiveness)
                .catsFriendly(catsFriendly)
                .dogsFriendly(dogsFriendly)
                .shelter(Objects.nonNull(shelter) ? shelter.toSimpleDto() : null)
                .build();
    }

    public void addShelter(User user) {
        user.getAnimals().add(this);
        this.shelter = user;
    }
}
