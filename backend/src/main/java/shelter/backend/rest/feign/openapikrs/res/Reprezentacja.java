package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Reprezentacja{
    private String nazwaOrganu;
    private String sposobReprezentacji;
    private ArrayList<Sklad> sklad;
}
