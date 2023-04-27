package shelter.backend.rest.model.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayUClientCredentialsDto {

    private Long id;

    private String clientId;

    private String clientSecret;

    private String merchantPosId;

    private Long shelterId;
}
