package shelter.backend.registration.service;

public interface Validator<D> {

    void throwIfNotValid(D dto);
}
