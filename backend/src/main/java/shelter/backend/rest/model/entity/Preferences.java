package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "preferences")
@Getter
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime timeOfWalking;

    private Long realAdoptionTTL;

    @OneToOne
    @JoinColumn(name = "shelter_id", nullable = true)
    private User shelter;
}


/*na froncie po zatwierdzeniu emaila(lub po zatwierzeniu przez admimna) ustawic preferencje godziny o ktorej sie wyprowadza psa
 * przy rejestrowaniu aktywnosci sprawdzic czy ta godzina jest czy jest nullem. jesli nullem to dac log.warning i wybrac jakas domyslna na poczet projetku*/