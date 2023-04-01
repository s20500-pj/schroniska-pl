package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;

import java.time.LocalDate;
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
    @Setter
    private AdoptionStatus adoptionStatus;
    @Setter
    private LocalDate validUntil;
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
                .validUntil(validUntil)
                .user(Objects.nonNull(user) ? user.toSimpleDto() : null)
                .animal(Objects.nonNull(animal) ? animal.toSimpleDto() : null)
                .build();
    }

    public void addAnimal(Animal animal) {
        animal.getAdoptions().add(this);
        this.animal = animal;
    }
}
