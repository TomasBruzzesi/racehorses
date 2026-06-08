package system;

import java.util.ArrayList;
import java.util.List;

import schemas.Horse;
import schemas.Player;

/**
 * Singleton que centraliza el estado del sistema de carreras.
 */

public class RaceSystem {

    private static RaceSystem instance;

    private List<Player> players;
    private List<Horse> horses;

    private RaceSystem() {
        this.players = new ArrayList<>();
        this.horses = new ArrayList<>();
    }

    /**
     * @return instancia unica del sistema de carreras
     */
    public static RaceSystem getInstance() {
        if (instance == null) {
            instance = new RaceSystem();
        }
        return instance;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Horse> getHorses() {
        return horses;
    }
}
