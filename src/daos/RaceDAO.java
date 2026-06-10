package daos;

import java.util.ArrayList;
import java.util.List;

import schemas.Race;

//Acceso a datos de carreras finalizadas en memoria.
public class RaceDAO {

    private final List<Race> completedRaces;

    public RaceDAO() {
        this.completedRaces = new ArrayList<>();
    }

    //Método para registrar una carrera finalizada en memoria.
    public void insert(Race race) {
        if (race != null) {
            completedRaces.add(race);
        }
    }
}
