package shelter.backend.payment.payu.rest.model.res;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayUAuthToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
}

