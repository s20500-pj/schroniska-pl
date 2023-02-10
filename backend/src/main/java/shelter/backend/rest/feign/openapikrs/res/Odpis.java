package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class Odpis{
    private String rodzaj;
    private NaglowekA naglowekA;
    private Dane dane;
}
