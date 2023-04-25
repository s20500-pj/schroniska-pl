package shelter.backend.payment.payu.rest.model.res;

import lombok.Data;

@Data
public class Status {
    public static final String STATUS_CODE_SUCCESS = "SUCCESS";
    private String statusCode;
    private String statusDesc;
}
