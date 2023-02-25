package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class NaglowekA{
    private String rejestr;
    private String numerKRS;
    private String dataCzasOdpisu;
    private String stanZDnia;
    private String dataRejestracjiWKRS;
    private int numerOstatniegoWpisu;
    private String dataOstatniegoWpisu;
    private String sygnaturaAktSprawyDotyczacejOstatniegoWpisu;
    private String oznaczenieSaduDokonujacegoOstatniegoWpisu;
    private int stanPozycji;
}
