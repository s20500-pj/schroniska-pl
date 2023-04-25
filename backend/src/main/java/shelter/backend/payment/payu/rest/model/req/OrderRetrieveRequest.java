package shelter.backend.payment.payu.rest.model.req;

import lombok.Data;

@Data
public class OrderRetrieveRequest {
    private String orderId;
}
