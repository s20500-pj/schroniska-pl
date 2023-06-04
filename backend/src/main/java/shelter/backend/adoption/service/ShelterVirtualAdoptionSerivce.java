package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.payment.payu.service.PaymentService;
import shelter.backend.payment.payu.rest.model.req.OrderDataRequest;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.Purpose;
import shelter.backend.rest.model.mapper.AdoptionMapper;
import shelter.backend.storage.repository.AdoptionRepository;
import shelter.backend.storage.repository.AnimalRepository;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.constants.ShelterConstants;
import shelter.backend.utils.exception.AdoptionException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class ShelterVirtualAdoptionSerivce extends ShelterAdoptionService implements VirtualAdoptionService {


    private final PaymentService paymentService;

    private final OrderDataRequest orderData;

    private final MessageSource messageSource;

    private static final Long ADOPTION_PRICE_DENOMINATOR = 30L; //TODO add to preference

    private static final String ORDER_NAME_PROPERTY = "payment.order.virtual.adoption.name";

    private static final String ORDER_DESCRIPTION_PROPERTY = "payment.order.virtual.adoption.description";

    @Value("${payment.payu.validUntil}")
    private Long pendingAdoptionValidUntil;

    public ShelterVirtualAdoptionSerivce(AdoptionRepository adoptionRepository, AnimalRepository animalRepository,
                                         UserRepository userRepository, AdoptionMapper adoptionMapper,
                                         EmailService emailService, PaymentService paymentService, OrderDataRequest orderData,
                                         MessageSource messageSource) {
        super(adoptionRepository, animalRepository, userRepository, adoptionMapper, emailService);
        this.paymentService = paymentService;
        this.orderData = orderData;
        this.messageSource = messageSource;
    }

    @Override
    public String beginVirtualAdoption(Long animalId, Long amount) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Zwięrzę o podanym ID nie istnieje"));
        if (!notAlreadyAdopted(animal)) {
            log.info("Animal awaits for real adoption. Animal id: {}", animalId);
            throw new AdoptionException("Zwierzę oczekuję już na adopcję realną");
        }
        User currentUser = getUser();
        log.debug("[beginVirtualAdoption] :: animal: {}, user: {}, ", animalId, currentUser);
        checkIfAmountIsMultiple(amount);
        int period = calculateAdoptionPeriod(amount);
        preparePaymentOrderData(amount, currentUser, animal, period);
        String redirect_uri = paymentService.commencePayment(currentUser, animal.getShelter());
        if (StringUtils.isNotBlank(redirect_uri)) {
            log.info("redirect URI to begin payment transaction: {}, user: {}, animalId: {}, animalName: {}",
                    redirect_uri, currentUser.getEmail(), animal.getId(), animal.getName());
            Adoption adoption = Adoption.builder()
                    .adoptionType(AdoptionType.VIRTUAL)
                    .adoptionStatus(AdoptionStatus.VIRTUAL_PENDING)
                    .validUntil(LocalDate.now().plusDays(Duration.ofSeconds(pendingAdoptionValidUntil).toDays()))
                    .animal(animal)
                    .user(currentUser)
                    .build();
            adoptionRepository.save(adoption);
            paymentService.updatePaymentOrderWithEntityServiceId(adoption.getId());
            return redirect_uri;
        } else {
            log.error("Problem occurred during processing the payment. redirect_uri: {}", redirect_uri);
            throw new AdoptionException("Problem z przetworzeniem płatności");
        }
    }

    @Override
    public void finalizeVirtualAdoption(Long id, Long amount) {
        log.debug("[finalizeVirtualAdoption] :: for virtual adoption id: {}", id);
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje wirtualna adopcja dla wybranego id"));
        log.info("[finalizeVirtualAdoption] :: finalize virtual adoptionID: {}, username: {}, animalId: {}", adoption.getId(), adoption.getUser().getEmail(), adoption.getAnimal().getId());
        int months = calculateAdoptionPeriod(amount / 100);
        adoption.setAdoptionStatus(AdoptionStatus.VIRTUAL_ADOPTED);
        LocalDate adoptionTime = LocalDate.now().plusMonths(months);
        adoption.setValidUntil(adoptionTime);
        adoptionRepository.save(adoption);
        emailService.sendVirtualAdoptionConfirmationAdopted(adoption.getUser().getEmail(),
                adoption.getAnimal().getShelter().getShelterName(), adoption.getAnimal().getName(),
                adoptionTime.toString(), adoption.getUser().getFirstName());
        log.info("Animal adopted virtually. Adoption: {}", adoption);
    }

    private void checkIfAmountIsMultiple(Long amount) {
        if (amount < ADOPTION_PRICE_DENOMINATOR) {
            throw new AdoptionException("Niepoprawna kwota");
        }
    }

    private void preparePaymentOrderData(Long amount, User user, Animal animal, long period) {
        orderData.setAmount(amount * 100);
        orderData.setBuyerEmail(user.getEmail());
        orderData.setName(messageSource.getMessage(ORDER_NAME_PROPERTY, null, Locale.getDefault()));
        String[] params = {String.valueOf(animal.getId()), String.valueOf(user.getId()), String.valueOf(amount), String.valueOf(period)};
        orderData.setDescription(messageSource.getMessage(ORDER_DESCRIPTION_PROPERTY, params, Locale.getDefault()));
        orderData.setCurrency(ShelterConstants.PLN_CURRENCY);
        orderData.setPurpose(Purpose.VIRTUAL_ADOPTION);
        orderData.setQuantity("1");
        orderData.setUnitPrice(String.valueOf(ADOPTION_PRICE_DENOMINATOR));
    }

    private static int calculateAdoptionPeriod(Long amount) {
        return (int) (amount / ADOPTION_PRICE_DENOMINATOR);
    }


    @Scheduled(cron = "0 15 1 * * ?")   //check every day at 01:15 AM
    public void deleteExpiredRealAdoptions() {
        log.debug("virtual adoption scheduler started");
        LocalDate today = LocalDate.now();
        List<Adoption> adoptionList = adoptionRepository.findAdoptionByAdoptionType(AdoptionType.VIRTUAL);
        List<Adoption> expiredAdoptions = adoptionList.stream()
                .filter(adoption -> adoption.getValidUntil() != null)
                .filter(adoption -> adoption.getValidUntil().isBefore(today))
                .toList();
        for (Adoption adoption : expiredAdoptions) {
            delete(adoption.getId());
            if (adoption.getAdoptionStatus() == AdoptionStatus.VIRTUAL_ADOPTED) {
                emailService.sendVirtualAdoptionTimeout(adoption.getUser().getEmail(), adoption.getAnimal().getName(),
                        adoption.getAnimal().getShelter().getShelterName(), adoption.getUser().getFirstName());
            }
        }
        log.debug("virtual adoption scheduler finished");
    }
}
