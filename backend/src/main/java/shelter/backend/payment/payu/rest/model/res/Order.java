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
    private String description;
    private String currencyCode;
    private Long totalAmount;
    private OrderStatus status;
//    private List<Product> products;
}
