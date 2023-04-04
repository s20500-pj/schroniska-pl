package shelter.backend.rest.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    @NotBlank(message = "Ulica jest wymagana")
    private String street;
    @NotBlank(message = "Miasto jest wymagane")
    private String city;
    @NotBlank(message = "Kod pocztowy jest wymagany")
    private String postalCode;
    @NotBlank(message = "Numer budynku jest wymagany")
    private String buildingNumber;
    private String flatNumber;
    @NotBlank(message = "Numer telefonu jest wymagany")
    private String phone;
    private String krsNumber;
}
