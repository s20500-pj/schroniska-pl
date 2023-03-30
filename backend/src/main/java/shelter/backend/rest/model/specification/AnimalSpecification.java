package shelter.backend.rest.model.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AnimalSpecification implements Specification<Animal> {

    private static final String NAME = "name";
    private static final String SPECIES = "species";
    private static final String SEX = "sex";
    private static final String AGE = "age";
    private static final String ANIMAL_STATUS = "animalStatus";
    private static final String STERILIZED = "sterilized";
    private static final String VACCINATED = "vaccinated";
    private static final String KIDS_FRIENDLY = "kidsFriendly";
    private static final String COUCH_POTATO = "couchPotato";
    private static final String NEEDS_ACTIVENESS = "needsActiveness";
    private static final String CATS_FRIENDLY = "catsFriendly";
    private static final String DOGS_FRIENDLY = "dogsFriendly";
    private static final String SORT_BY = "sortBy";


    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//TODO podowawać wszędzie toLowerCase
            switch (key) {
                case NAME -> predicates.add(criteriaBuilder.like(root.get(NAME), "%" + value + "%"));
                case SPECIES -> predicates.add(criteriaBuilder.like(root.get(SPECIES), "%" + value + "%"));
                case SEX -> predicates.add(criteriaBuilder.equal(root.get(SEX), Sex.valueOf(value)));
                case AGE -> predicates.add(criteriaBuilder.equal(root.get(AGE), Age.valueOf(value)));
                case ANIMAL_STATUS -> predicates.add(criteriaBuilder.equal(root.get(AGE), AnimalStatus.valueOf(value)));
                case STERILIZED -> predicates.add(criteriaBuilder.equal(root.get(STERILIZED), Boolean.valueOf(value)));
                case VACCINATED -> predicates.add(criteriaBuilder.equal(root.get(VACCINATED), Boolean.valueOf(value)));
                case KIDS_FRIENDLY ->
                        predicates.add(criteriaBuilder.equal(root.get(KIDS_FRIENDLY), Boolean.valueOf(value)));
                case COUCH_POTATO ->
                        predicates.add(criteriaBuilder.equal(root.get(COUCH_POTATO), Boolean.valueOf(value)));
                case NEEDS_ACTIVENESS ->
                        predicates.add(criteriaBuilder.equal(root.get(NEEDS_ACTIVENESS), Boolean.valueOf(value)));
                case CATS_FRIENDLY ->
                        predicates.add(criteriaBuilder.equal(root.get(CATS_FRIENDLY), Boolean.valueOf(value)));
                case DOGS_FRIENDLY ->
                        predicates.add(criteriaBuilder.equal(root.get(DOGS_FRIENDLY), Boolean.valueOf(value)));
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