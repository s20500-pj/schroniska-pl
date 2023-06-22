package shelter.backend.rest.model.enums;

public enum Purpose {
    VIRTUAL_ADOPTION("ADOPCJA WIRTUALNA");

    String purposeName;

    Purpose(String purposeName) {
        this.purposeName = purposeName;
    }

    public String getPurposeName() {
        return purposeName;
    }
}
