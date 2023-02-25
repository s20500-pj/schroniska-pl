package shelter.backend.rest.model.enums;
/*
* ApprovalStatus used to trace shelter status
* EMAIL_NIEPOTWIERDZONY - awaits for email confirmation from user's side
* OCZEKUJE - awaits for ApprovalProvider check
* ODRZUCONY - declined by ApprovalProvider
* POTWIERDZONY - accepted by ApprovalProvider
*/
public enum ApprovalStatus {
    EMAIL_NIEPOTWIERDZONY,
    OCZEKUJE,
    ODRZUCONY,
    POTWIERDZONY

}
