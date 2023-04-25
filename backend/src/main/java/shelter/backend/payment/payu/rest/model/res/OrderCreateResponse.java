package shelter.backend.payment.payu.rest.model.res;

import lombok.Data;

@Data
public class OrderCreateResponse {

    private String orderId;

    private String extOrderId;

    private String redirectUri;

    private Status status;

}
