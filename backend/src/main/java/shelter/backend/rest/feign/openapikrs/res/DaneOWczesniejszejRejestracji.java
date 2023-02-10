package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class DaneOWczesniejszejRejestracji{
    private String nazwaPoprzedniegoRejestru;
    private String numerWPoprzednimRejestrze;
    private String sadProwadzacyRejestr;
}
