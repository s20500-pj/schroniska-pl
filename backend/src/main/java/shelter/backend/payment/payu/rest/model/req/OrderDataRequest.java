package shelter.backend.payment.payu.rest.model.req;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import shelter.backend.rest.model.enums.Purpose;

@Component
@RequestScope
@Data
public class OrderDataRequest {

    private String ipAddress;
    private String name;
    private String unitPrice;
    private String quantity;
    private Purpose purpose;
    private String description;
    private String currency;
    private Long amount;
    private String buyerEmail;
    private String extOrderId;
}
