package shelter.backend.rest.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shelter.backend.rest.model.dtos.AnimalDto;
import shelter.backend.rest.model.dtos.PayUClientCredentialsDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "payuclientcredentials")
public class PayUClientCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String clientSecret;

    private String merchantPosId;

    @OneToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private User shelter;

    public PayUClientCredentials toEntity(PayUClientCredentialsDto dto, User shelter) {
        this.id = dto.getId();
        this.clientId = dto.getClientId();
        this.clientSecret = dto.getClientSecret();
        this.merchantPosId = dto.getMerchantPosId();
        this.shelter = shelter;
        return this;
    }
}
