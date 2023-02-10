package shelter.backend.rest.model.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String firstName;
    private String lastName;
    @NotBlank(message = "Adres e-mail jest wymagany")
    @Email(message = "Niewłaściwy adres e-mail")
    private String email;
    @NotBlank(message = "Hasło jest wymagane")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String shelterName;
    @NotNull(message = "Adres jest wymagany")
    @Valid
    @JsonProperty("address")
    private AddressDto address;
}
