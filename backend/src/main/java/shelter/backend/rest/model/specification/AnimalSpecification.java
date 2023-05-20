package shelter.backend.rest.model.specification;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.Age;
import shelter.backend.rest.model.enums.AnimalStatus;
import shelter.backend.rest.model.enums.Sex;
import shelter.backend.rest.model.enums.Species;
import shelter.backend.utils.constants.SpecificationConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final String CITY = "city";
    public static final String ADOPTED = "adopted";

    private final Map<String, String> searchParams;

    @Override
    public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Animal, User> userJoin = root.join("shelter");
        Join<User, Address> addressJoin = userJoin.join("address");
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key) {
                case NAME ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(NAME)), "%" + value.toLowerCase() + "%"));
                case SPECIES -> predicates.add(criteriaBuilder.equal(root.get(SPECIES), Species.valueOf(value)));
                case SEX -> predicates.add(criteriaBuilder.equal(root.get(SEX), Sex.valueOf(value)));
                case AGE -> predicates.add(criteriaBuilder.equal(root.get(AGE), Age.valueOf(value)));
                case ANIMAL_STATUS ->
                        predicates.add(criteriaBuilder.equal(root.get(ANIMAL_STATUS), AnimalStatus.valueOf(value)));
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
                case CITY ->
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(addressJoin.get(CITY)), "%" + value.toLowerCase() + "%"));
                case SpecificationConstants.SHELTER_ID ->
                        predicates.add(criteriaBuilder.equal(userJoin.get(SpecificationConstants.ID), value));
                case SpecificationConstants.ACTIVITY_TIME -> {
                    Join<Animal, Activity> activityLeftJoin = root.join("activities", JoinType.LEFT);
                    if (StringUtils.isNotBlank(value)) {
                        LocalDateTime localDateTimeValue = LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
                        predicates.add(criteriaBuilder.or
                                (criteriaBuilder.notEqual(activityLeftJoin.get(SpecificationConstants.ACTIVITY_TIME), localDateTimeValue),
                                        criteriaBuilder.isNull(activityLeftJoin)));
                    } else {
                        predicates.add((criteriaBuilder.isNull(activityLeftJoin)));
                    }
                }
                case ADOPTED -> {
                    if (value.equals("false")) {
                        predicates.add(criteriaBuilder.notEqual(root.get(ANIMAL_STATUS), AnimalStatus.ADOPTED));
                    } else if (value.equals("true")) {
                        predicates.add(criteriaBuilder.equal(root.get(ANIMAL_STATUS), AnimalStatus.ADOPTED));
                    }
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
