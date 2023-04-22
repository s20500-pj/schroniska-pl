package shelter.backend.rest.model.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.enums.ActivityType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ActivitySpecification implements Specification<Activity> {

    private static final String ACTIVITYTYPE = "activityType";
    private static final String ACTIVITYTIME = "activityTime";
    private static final String SORT_BY = "sortBy";

    private final LocalTime defaultTimeOfActivity = LocalTime.of(16, 0);


    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case ACTIVITYTYPE -> predicates.add(criteriaBuilder.equal(root.get(ACTIVITYTYPE), ActivityType.valueOf(value)));
                case ACTIVITYTIME -> predicates.add(criteriaBuilder.equal(root.get(ACTIVITYTIME), value));
            }
        }

        if (searchParams.containsKey(SORT_BY)) {
            String sortByField = searchParams.get(SORT_BY);
            Order order = criteriaBuilder.asc(root.get(sortByField));
            query.orderBy(order);
        }

        return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
