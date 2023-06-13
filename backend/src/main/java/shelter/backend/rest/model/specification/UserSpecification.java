package shelter.backend.rest.model.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.utils.constants.SpecificationConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String SHELTER_NAME = "shelterName";
    private static final String IS_DISABLED = "isDisabled";
    private static final String APPROVAL_STATUS = "approvalStatus";
    public static final String USER_TYPE = "userType";
    private static final String CITY = "city";
    private static final String STREET = "street";

    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<User, Address> addressJoin = root.join("address");
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key) {
                case FIRST_NAME ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FIRST_NAME)), "%" + value.toLowerCase() + "%"));
                case LAST_NAME ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(LAST_NAME)), "%" + value.toLowerCase() + "%"));
                case EMAIL ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(EMAIL)), "%" + value.toLowerCase() + "%"));
                case SHELTER_NAME ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(SHELTER_NAME)), "%" + value.toLowerCase() + "%"));
                case IS_DISABLED ->
                        predicates.add(criteriaBuilder.equal(root.get(IS_DISABLED), Boolean.valueOf(value)));
                case APPROVAL_STATUS ->
                        predicates.add(criteriaBuilder.equal(root.get(APPROVAL_STATUS), ApprovalStatus.valueOf(value)));
                case USER_TYPE -> predicates.add(criteriaBuilder.equal(root.get(USER_TYPE), UserType.valueOf(value)));
                case CITY ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get(CITY)), "%" + value.toLowerCase() + "%"));
                case STREET ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get(STREET)), "%" + value.toLowerCase() + "%"));
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