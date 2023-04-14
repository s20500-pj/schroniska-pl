package shelter.backend.payment.payu.rest.model;

import lombok.Data;

@Data
public class PayUAuthToken {

    private String accessToken;

    private String tokenType;

    private int expiresIn;

    private String grantType;

    private String error;

    private String errorDescription;
}

