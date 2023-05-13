package shelter.backend.payment.payu.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import shelter.backend.configuration.ContextDelegateAware;
import shelter.backend.payment.payu.configuration.PayUConfigurationProperties;
import shelter.backend.payment.payu.rest.model.req.OrderDataRequest;
import shelter.backend.payment.payu.rest.model.res.OrderCreateResponse;
import shelter.backend.payment.payu.rest.model.res.PayUAuthToken;
import shelter.backend.payment.payu.rest.model.res.Status;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.User;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.PaymentOrderRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayUServiceTest {

    @InjectMocks
    PayUService payUService;
    @Mock
    private PayUClientCredentialsRepository payUClientCredentialsRepository;
    @Mock
    private PaymentOrderRepository paymentOrderRepository;
    @Mock
    private PayUConfigurationProperties payUConfigurationProperties;
    @Mock
    private PaymentAuthorizationService paymentAuthorizationService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OrderDataRequest orderDataRequest;
    @Mock
    private ContextDelegateAware contextDelegateAware;

    @BeforeEach
    public void setUp() {

        ReflectionTestUtils.setField(payUService, "expiresIn", 172800L);
        ReflectionTestUtils.setField(payUService, "serverPort", 8080);

        PayUClientCredentials payUClientCredentials = PayUClientCredentials.builder()
                .clientId("clientId")
                .clientSecret("clientSecret")
                .merchantPosId("merchantId")
                .shelter(User.builder().build())
                .id(1L)
                .build();
        when(payUClientCredentialsRepository.findByShelter_Id(any())).thenReturn(Optional.ofNullable(payUClientCredentials));
        PayUAuthToken payUAuthToken = new PayUAuthToken();
        payUAuthToken.setAccessToken("accessToken");
        payUAuthToken.setTokenType("Basic");
        when(payUConfigurationProperties.getAuthorizationUri()).thenReturn("http://pay.com/authorize");
        when(paymentAuthorizationService.authorize(any())).thenReturn(payUAuthToken);
    }

    @Test
    void commencePayment() {
        //
        String redirectUrl = "http://redirect-test.com";
        when(payUConfigurationProperties.getOrderUrl()).thenReturn("http://payment-order.com");
        when(payUConfigurationProperties.getCallbackPath()).thenReturn("/callBackUrl");
        OrderCreateResponse orderCreateResponse = new OrderCreateResponse();
        Status status = new Status();
        status.setStatusCode(Status.STATUS_CODE_SUCCESS);
        orderCreateResponse.setStatus(status);
        orderCreateResponse.setExtOrderId("extOrderId");
        orderCreateResponse.setOrderId("orderId");
        orderCreateResponse.setRedirectUri(redirectUrl);
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok(orderCreateResponse));
        when(orderDataRequest.getAmount()).thenReturn(30000L);
        //
        String result = payUService.commencePayment(new User(), User.builder().id(1L).build());
        //
        Assertions.assertEquals(result, redirectUrl);
    }
}


