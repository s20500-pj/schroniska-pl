package shelter.backend.rest.model.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.utils.constants.SpecificationConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AdoptionSpecification implements Specification<Adoption> {

    private static final String ADOPTION_TYPE = "adoptionType";

    private static final String ADOPTION_STATUS = "adoptionStatus";


    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<Adoption> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key) {
                case ADOPTION_TYPE -> predicates.add(criteriaBuilder.equal(root.get(ADOPTION_TYPE), AdoptionType.valueOf(value)));
                case ADOPTION_STATUS -> predicates.add(criteriaBuilder.equal(root.get(ADOPTION_STATUS), AdoptionStatus.valueOf(value)));
                case SpecificationConstants.SHELTER_ID -> {
                    Join<Adoption, Animal> animalJoin = root.join("animal");
                    Join<Animal, User> shelterJoin = animalJoin.join("shelter");
                    predicates.add(criteriaBuilder.equal(shelterJoin.get(SpecificationConstants.ID), value));
                }
                case SpecificationConstants.USER_ID -> {
                    Join<Adoption, User> userJoin = root.join("user");
                    predicates.add(criteriaBuilder.equal(userJoin.get(SpecificationConstants.ID), value));
                }
            }
        }

        if (searchParams.containsKey(SpecificationConstants.SORT_BY)) {
            String sortByField = searchParams.get(SpecificationConstants.SORT_BY);
            Order order = criteriaBuilder.asc(root.get(sortByField));
            query.orderBy(order);
        }

            return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
