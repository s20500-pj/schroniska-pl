package shelter.backend.rest.model.enums;

public enum ApprovalStatus {
    EMAIL_NOT_CONFIRMED,
    PENDING, //waiting for KRS check
    REJECTED, //KRS rejected
    CONFIRMED, //KRS confirmed
    COMPLETED
}