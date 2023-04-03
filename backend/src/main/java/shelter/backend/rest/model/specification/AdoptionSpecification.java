package shelter.backend.rest.model.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.enums.AdoptionStatus;
import shelter.backend.rest.model.enums.AdoptionType;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AdoptionSpecification implements Specification<Adoption> {

    private static final String ADOPTIONTYPE = "adoptionType";
    private static final String ADOPTIONSTATUS = "adoptionStatus";
    private static final String SORT_BY = "sortBy";


    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<Adoption> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//TODO podowawać wszędzie toLowerCase
            switch (key) {
                case ADOPTIONTYPE -> predicates.add(criteriaBuilder.equal(root.get(ADOPTIONTYPE), AdoptionType.valueOf(value)));
                case ADOPTIONSTATUS -> predicates.add(criteriaBuilder.equal(root.get(ADOPTIONSTATUS), AdoptionStatus.valueOf(value)));
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
