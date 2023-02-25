package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UmowaStatut{
    private ArrayList<InformacjaOZawarciuZmianieUmowyStatutu> informacjaOZawarciuZmianieUmowyStatutu;
}
