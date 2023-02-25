package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Dzial1{
    private DanePodmiotu danePodmiotu;
    private SiedzibaIAdres siedzibaIAdres;
    private UmowaStatut umowaStatut;
    private PozostaleInformacje pozostaleInformacje;
    private ArrayList<WspolnicySpzoo> wspolnicySpzoo;
    private Kapital kapital;
}
