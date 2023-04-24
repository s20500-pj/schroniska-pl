package shelter.backend.payment.payu.rest.model.req;

import lombok.Builder;
import lombok.Data;
import shelter.backend.payment.payu.rest.model.Buyer;
import shelter.backend.payment.payu.rest.model.Product;

import java.util.List;

@Data
@Builder
public class OrderCreateRequest {

    private String extOrderId;
    private String continueUrl;
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private Buyer buyer;
    private List<Product> products;
}
