package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.enums.AnimalStatus;

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
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String information;
    private String species;
    private LocalDate age;
    private AnimalStatus animalStatus;

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
        this.age = dto.getAge();
        this.animalStatus = dto.getAnimalStatus();
        return this;
    }

    public AnimalDto toSimpleDto() {
        return AnimalDto.builder()
                .id(id)
                .name(name)
                .information(information)
                .species(species)
                .age(age)
                .animalStatus(animalStatus)
                .shelter(Objects.nonNull(shelter) ? shelter.toSimpleDto() : null)
                .build();
    }

    public void addShelter(User user) {
        user.getAnimals().add(this);
        this.shelter = user;
    }
}
