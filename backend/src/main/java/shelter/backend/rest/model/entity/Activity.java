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
import shelter.backend.rest.model.dtos.ActivityDto;
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

    public void addAnimal(Animal animal) {
        animal.getActivities().add(this);
        this.animal = animal;
    }
}
