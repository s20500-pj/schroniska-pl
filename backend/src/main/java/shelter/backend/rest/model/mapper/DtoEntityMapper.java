package shelter.backend.rest.model.mapper;

public interface DtoEntityMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);
}
