package shelter.backend.payment.payu.rest.model.res;

import lombok.Data;
import shelter.backend.payment.payu.rest.model.Buyer;
import shelter.backend.payment.payu.rest.model.Product;

import java.util.List;

@Data
public class Order {

    private String orderId;
    private String extOrderId;
    private String orderCreateDate;
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String validityTime;
    private String description;
    private String additionalDescription;
    private String currencyCode;
    private Long totalAmount;
    private OrderStatus status;
    private Buyer buyer;
    private List<Product> products;
}
