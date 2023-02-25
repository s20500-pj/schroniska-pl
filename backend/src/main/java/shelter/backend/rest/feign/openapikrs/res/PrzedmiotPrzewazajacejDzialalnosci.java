package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class PrzedmiotPrzewazajacejDzialalnosci{
    private String opis;
    private String kodDzial;
    private String kodKlasa;
    private String kodPodklasa;
}
