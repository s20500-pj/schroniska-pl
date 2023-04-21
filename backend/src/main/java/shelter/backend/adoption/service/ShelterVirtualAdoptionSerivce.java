package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.payment.PaymentService;
import shelter.backend.payment.payu.rest.model.OrderDataRequest;
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
        String redirect_uri = paymentService.commencePayment(currentUser, animal);
        if (StringUtils.isNotBlank(redirect_uri)) {
            log.info("redirect URI to begin payment transaction: {}, user: {}, animal: {}", redirect_uri, currentUser, animal);
            Adoption adoption = Adoption.builder()
                    .adoptionType(AdoptionType.VIRTUAL)
                    .adoptionStatus(AdoptionStatus.PENDING)
                    .animal(animal)
                    .user(currentUser)
                    .build();
            adoptionRepository.save(adoption);
            return redirect_uri;
        } else {
            log.error("Problem occurred during processing the payment. redirect_uri: {}", redirect_uri);
            throw new AdoptionException("Problem z przetowrzeniem płatności");
        }
    }

    private void checkIfAmountIsMultiple(Long amount) {
        if (amount < ADOPTION_PRICE_DENOMINATOR) {
            throw new AdoptionException("Niepoprawna kwota");
        }
    }

    private void preparePaymentOrderData(Long amount, User user, Animal animal, long period) {
        orderData.setAmount(amount);
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

}
