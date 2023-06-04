package shelter.backend.adoption.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import shelter.backend.ClientInterceptorTestBase;
import shelter.backend.email.EmailService;
import shelter.backend.payment.payu.rest.model.req.OrderDataRequest;
import shelter.backend.payment.payu.service.PaymentService;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterVirtualAdoptionSerivceTest extends ClientInterceptorTestBase {

    @InjectMocks
    private ShelterVirtualAdoptionSerivce shelterVirtualAdoptionSerivce;

    @Mock
    private PaymentService paymentService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AdoptionRepository adoptionRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    OrderDataRequest orderDataRequest;

    @Mock
    EmailService emailService;

    @Test
    void beginVirtualAdoption() {
        //
        Animal animal = Animal.builder()
                .id(1L)
                .animalStatus(AnimalStatus.READY_FOR_ADOPTION)
                .adoptions(new ArrayList<>())
                .build();
        ReflectionTestUtils.setField(shelterVirtualAdoptionSerivce, "pendingAdoptionValidUntil", 172800L);
        when(animalRepository.findById(any())).thenReturn(Optional.of(animal));
        when(paymentService.commencePayment(any(), any())).thenReturn("http://payu.pay.com");
        when(adoptionRepository.save(any())).thenReturn(Adoption.builder().id(1L).build());
        when(paymentService.updatePaymentOrderWithEntityServiceId(any())).thenReturn(true);
        //
        String result = shelterVirtualAdoptionSerivce.beginVirtualAdoption(1L, 30L);
        //
        Assertions.assertEquals(result, "http://payu.pay.com");
    }

    @Test
    void finalizeVirtualAdoption() {
        //
        Adoption adoption = Adoption.builder()
                .id(1L)
                .user(user)
                .animal(Animal.builder()
                        .id(1L)
                        .shelter(User.builder().build()).build())
                .build();
        when(adoptionRepository.findById(any())).thenReturn(Optional.of(adoption));
        //
        shelterVirtualAdoptionSerivce.finalizeVirtualAdoption(adoption.getId(), 3000L);
        //
        verify(adoptionRepository, times(1)).save(adoption);
        verify(emailService, times(1))
                .sendVirtualAdoptionConfirmationAdopted(anyString(), any(), any(), anyString(), any());
    }

    @Test
    void deleteExpiredRealAdoptions() {
        //
        when(adoptionRepository.findAdoptionByAdoptionType(AdoptionType.VIRTUAL))
                .thenReturn(List.of(Adoption.builder().validUntil(LocalDate.now().minusDays(1)).build()));
        //
        shelterVirtualAdoptionSerivce.deleteExpiredRealAdoptions();
        //
        verify(adoptionRepository, times(1)).deleteById(any());
    }
}

