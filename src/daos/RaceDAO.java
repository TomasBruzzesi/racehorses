package daos;

import java.util.ArrayList;
import java.util.List;

import schemas.Race;

/**
 * Acceso a datos de carreras finalizadas en memoria.
 * No persiste historial en SQL; solo mantiene las carreras de la sesion actual.
 */
public class RaceDAO {

    private final List<Race> completedRaces;

    public RaceDAO() {
        this.completedRaces = new ArrayList<>();
    }

    /**
     * Registra una carrera finalizada en memoria.
     *
     * @param race carrera completada
     */
    public void insert(Race race) {
        if (race != null) {
            completedRaces.add(race);
        }
    }
}
