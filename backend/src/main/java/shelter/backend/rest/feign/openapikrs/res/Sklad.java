package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class Sklad{
    private Nazwisko nazwisko;
    private Imiona imiona;
    private Identyfikator identyfikator;
    private String funkcjaWOrganie;
    private boolean czyZawieszona;
}
