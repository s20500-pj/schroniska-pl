package shelter.backend.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import shelter.backend.rest.model.dtos.AddressDto;

@Data
public class AdminShelterRequest {
    @NotBlank(message = "Adres e-mail jest wymagany")
    @Email(message = "Niewłaściwy adres e-mail")
    private String email;
    @NotBlank(message = "Nazwa schroniska jest wymagana")
    private String shelterName;
    @NotNull(message = "Adres jest wymagany")
    @Valid
    @JsonProperty("address")
    private AddressDto address;
}
