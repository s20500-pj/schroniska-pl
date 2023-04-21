package shelter.backend.rest.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import shelter.backend.rest.model.enums.Purpose;

import java.io.Serializable;


@RedisHash("PaymentOrder")
@AllArgsConstructor
@Getter
public class PaymentOrder implements Serializable {

    private String id;

    @Indexed
    private String extOrderId;

    private String userName;

    private String shelterName;

    private Long amount;

    private Purpose purpose;
}
