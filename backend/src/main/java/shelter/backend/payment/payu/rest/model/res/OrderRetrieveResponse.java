package shelter.backend.payment.payu.rest.model.res;

import lombok.Data;

import java.util.List;
import java.util.Properties;

@Data
public class OrderRetrieveResponse {

    private List<Order> orders;
    private Status status;
    private List<Properties> properties;
}
