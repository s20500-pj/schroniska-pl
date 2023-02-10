package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

@Data
public class WspolnicySpzoo{
    private String nazwa;
    private Identyfikator identyfikator;
    private Krs krs;
    private String posiadaneUdzialy;
    private boolean czyPosiadaCaloscUdzialow;
}
