package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class Adres{
    private String ulica;
    private String nrDomu;
    private String miejscowosc;
    private String kodPocztowy;
    private String poczta;
    private String kraj;
}
