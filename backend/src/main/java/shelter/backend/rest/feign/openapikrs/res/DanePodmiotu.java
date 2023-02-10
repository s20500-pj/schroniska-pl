package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class DanePodmiotu{
    private String formaPrawna;
    private Identyfikatory identyfikatory;
    private String nazwa;
    private DaneOWczesniejszejRejestracji daneOWczesniejszejRejestracji;
    private boolean czyProwadziDzialalnoscZInnymiPodmiotami;
    private boolean czyPosiadaStatusOPP;
}
