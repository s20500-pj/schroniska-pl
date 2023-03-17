package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "adoptions")
@Getter
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AdoptionType adoptionType;
    private AdoptionStatus adoptionStatus;
    //powiązanie one way, do zapisu usera
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    public Adoption toEntity(AdoptionDto dto) {
        this.id = dto.getId();
        this.adoptionType = dto.getAdoptionType();
        this.adoptionStatus = dto.getAdoptionStatus();
        return this;
    }

    public AdoptionDto toDto() {
        return AdoptionDto.builder()
                .id(id)
                .adoptionType(adoptionType)
                .adoptionStatus(adoptionStatus)
                .user(Objects.nonNull(user) ? user.toSimpleDto() : null)
                .animal(Objects.nonNull(animal) ? animal.toSimpleDto() : null)
                .build();
    }

    public void addAnimal(Animal animal) {
        animal.getAdoptions().add(this);
        this.animal = animal;
    }
}
