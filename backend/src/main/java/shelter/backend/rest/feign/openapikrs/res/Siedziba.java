package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class Siedziba{
    private String kraj;
    private String wojewodztwo;
    private String powiat;
    private String gmina;
    private String miejscowosc;
}
