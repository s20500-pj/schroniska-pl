package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import shelter.backend.rest.model.dtos.ActivityDto;
import shelter.backend.rest.model.dtos.ActivityDto2;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.enums.ActivityType;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "activities")
@Getter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Setter
    private LocalDateTime activityTime;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    public Activity toEntity(ActivityDto dto) {
        this.id = dto.getId();
        this.activityType = dto.getActivityType();
        this.activityTime = dto.getActivityTime();
        return this;
    }

    public ActivityDto toDto() {
        return ActivityDto.builder()
                .id(id)
                .activityType(activityType)
                .activityTime(activityTime)
                .user(Objects.nonNull(user) ? user.toSimpleDto() : null)
                .animalId(animal.getId())
                .build();
    }

    public ActivityDto2 toDto2(AnimalDto animalDto) {
        return ActivityDto2.builder()
                .id(id)
                .activityType(activityType)
                .activityTime(activityTime)
                .user(Objects.nonNull(user) ? user.toSimpleDto() : null)
                .animalDto(animalDto)
                .build();
    }

    public void addAnimal(Animal animal) {
        animal.getActivities().add(this);
        this.animal = animal;
    }
}
