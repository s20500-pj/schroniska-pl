package shelter.backend.rest.feign.openapikrs.res;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PrzedmiotDzialalnosci{
    private ArrayList<PrzedmiotPrzewazajacejDzialalnosci> przedmiotPrzewazajacejDzialalnosci;
    private ArrayList<PrzedmiotPozostalejDzialalnosci> przedmiotPozostalejDzialalnosci;
}
