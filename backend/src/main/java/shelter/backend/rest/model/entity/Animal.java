package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;
import shelter.backend.rest.model.enums.Species;

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
    @Enumerated(EnumType.STRING)
    private Species species;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Enumerated(EnumType.STRING)
    private Age age;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private AnimalStatus animalStatus;
    private Boolean sterilized;
    private Boolean vaccinated;
    private Boolean kidsFriendly;
    private Boolean couchPotato; //kanapowiec
    private Boolean needsActiveness;
    private Boolean catsFriendly;
    private Boolean dogsFriendly;
    private Boolean availableForWalk;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User shelter;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<Adoption> adoptions = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

    public Animal toEntity(AnimalDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.information = dto.getInformation();
        this.species = dto.getSpecies();
        this.sex = dto.getSex();
        this.age = dto.getAge();
        this.birthDate = dto.getBirthDate();
        this.animalStatus = dto.getAnimalStatus();
        this.sterilized = dto.getSterilized();
        this.vaccinated = dto.getVaccinated();
        this.kidsFriendly = dto.getKidsFriendly();
        this.couchPotato = dto.getCouchPotato();
        this.needsActiveness = dto.getNeedsActiveness();
        this.catsFriendly = dto.getCatsFriendly();
        this.dogsFriendly = dto.getDogsFriendly();
        this.imagePath = dto.getImagePath();
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
                .adoptions((Objects.nonNull(adoptions) ?  adoptions.stream().map(Adoption::toDto).toList() : null))
                .activities((Objects.nonNull(activities) ?  activities.stream().map(Activity::toDto).toList() : null))
                .shelter(Objects.nonNull(shelter) ? shelter.toSimpleDto() : null)
                .imagePath(imagePath)
                .build();
    }

    public void addShelter(User user) {
        user.getAnimals().add(this);
        this.shelter = user;
    }
}
