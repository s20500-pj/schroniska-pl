package shelter.backend.rest.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import shelter.backend.rest.model.dtos.AdoptionDto;
import shelter.backend.rest.model.dtos.AdoptionDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
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

    @Enumerated(EnumType.STRING)
    private AdoptionType adoptionType;

    @Setter
    @Enumerated(EnumType.STRING)
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
                .animalId(animal.getId())
                .build();
    }

    public AdoptionDto2 toDto2(AnimalDto animalDto) {
        return AdoptionDto2.builder()
                .id(id)
                .adoptionType(adoptionType)
                .adoptionStatus(adoptionStatus)
                .validUntil(validUntil)
                .user(Objects.nonNull(user) ? user.toSimpleDto() : null)
                .animalDto(animalDto)
                .build();
    }

    public void addAnimal(Animal animal) {
        animal.getAdoptions().add(this);
        this.animal = animal;
    }
}
