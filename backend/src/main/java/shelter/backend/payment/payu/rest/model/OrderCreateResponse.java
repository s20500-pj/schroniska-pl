package shelter.backend.payment.payu.rest.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderCreateResponse {

  private String orderId;

  private String extOrderId;

  private String redirectUri;

  private Status status;

  @Data
  public static class Status {
    public static final String STATUS_CODE_SUCCESS = "SUCCESS";
    private String statusCode;
  }
}
