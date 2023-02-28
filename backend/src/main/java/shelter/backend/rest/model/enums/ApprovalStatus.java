package shelter.backend.rest.model.enums;

/*
 * ApprovalStatus used to trace shelter status
 * EMAIL_NOT_CONFIRMED - awaits for email confirmation from user's side
 * PENDING - awaits for ApprovalProvider check
 * REJECTED - declined by ApprovalProvider
 * CONFIRMED - accepted by ApprovalProvider
 */
public enum ApprovalStatus {
    EMAIL_NOT_CONFIRMED,
    PENDING,
    REJECTED,
    CONFIRMED
}