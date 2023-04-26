package shelter.backend.rest.model.specification;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.ActivityType;
import shelter.backend.utils.constants.SpecificationConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ActivitySpecification implements Specification<Activity> {

    private static final String ACTIVITY_TYPE = "activityType";

    private final Map<String, String> searchParams;


    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key) {
                case ACTIVITY_TYPE ->
                        predicates.add(criteriaBuilder.equal(root.get(ACTIVITY_TYPE), ActivityType.valueOf(value)));
                case SpecificationConstants.ACTIVITY_TIME -> {
                    if (StringUtils.isNotBlank(value)) {
                        LocalDateTime localDateTimeValue = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
                        predicates.add(criteriaBuilder.equal(root.get(SpecificationConstants.ACTIVITY_TIME), localDateTimeValue));
                    }
                }
                case SpecificationConstants.SHELTER_ID -> {
                    Join<Activity, Animal> animalJoin = root.join("animal");
                    Join<Animal, User> shelterJoin = animalJoin.join("shelter");
                    predicates.add(criteriaBuilder.equal(shelterJoin.get(SpecificationConstants.ID), value));
                }
                case SpecificationConstants.USER_ID -> {
                    Join<Activity, User> userJoin = root.join("user");
                    predicates.add(criteriaBuilder.equal(userJoin.get(SpecificationConstants.ID), value));
                }
            }

            if (searchParams.containsKey(SpecificationConstants.SORT_BY)) {
                String sortByField = searchParams.get(SpecificationConstants.SORT_BY);
                Order order = criteriaBuilder.asc(root.get(sortByField));
                query.orderBy(order);
            }

        }
        return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

