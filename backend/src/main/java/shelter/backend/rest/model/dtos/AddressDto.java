package shelter.backend.rest.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    @NotBlank(message = "Ulica jest wymagana")
    private String street;
    @NotBlank(message = "Miasto jest wymagane")
    private String city;
    @NotBlank(message = "kod pocztowy jest wymagany")
    private String postal_code;
    @NotBlank(message = "Numer budynku jest wymagany")
    private String  building_number;
    private String flat_number;
    @NotBlank(message = "Numer telefonu jest wymagany")
    private String phone;
    private String KRS_number;
}
