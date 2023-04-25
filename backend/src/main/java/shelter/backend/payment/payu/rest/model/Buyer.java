package shelter.backend.payment.payu.rest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Buyer {
    private String customerId;
    private String extCustomerId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String nin;
    private String language;
    private String delivery;

}
