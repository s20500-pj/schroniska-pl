package shelter.backend.adoption.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import shelter.backend.email.EmailService;
import shelter.backend.payment.PaymentService;
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
import shelter.backend.utils.exception.AdoptionException;

@Service
@Slf4j
public class ShelterVirtualAdoptionSerivce extends ShelterAdoptionService implements VirtualAdoptionService {


    private final PaymentService paymentService;

    private static final double DENOMINATOR = 30L; //TODO add to preference

    public ShelterVirtualAdoptionSerivce(AdoptionRepository adoptionRepository, AnimalRepository animalRepository,
                                         UserRepository userRepository, AdoptionMapper adoptionMapper,
                                         EmailService emailService, PaymentService paymentService) {
        super(adoptionRepository, animalRepository, userRepository, adoptionMapper, emailService);
        this.paymentService = paymentService;
    }

    @Override
    public String beginVirtualAdoption(long animalId, long amount) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Zwięrzę o podanym ID nie istnieje"));
        User currentUser = getUser();
        log.debug("[beginVirtualAdoption] :: animal: {}, user: {}, ", animalId, currentUser);
        checkIfAmountIsMultiple(amount);
        String redirect_uri = paymentService.commencePayment(amount, currentUser, animal, Purpose.VIRTUAL_ADOPTION);
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

    private void checkIfAmountIsMultiple(long amount) {
        if (amount <= 0 || amount % DENOMINATOR != 0) {
            throw new AdoptionException("Nie poprawna kwota");
        }
    }
}
