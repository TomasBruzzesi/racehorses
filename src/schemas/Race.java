package schemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Representa una carrera entre varios caballos sobre una pista.
public class Race {

    private List<Horse> horses;
    private Track track;
    private boolean started;
    private int currentTick;
    private final Map<Horse, Integer> finishTicks = new HashMap<>();

    //Constructor por defecto.
    public Race() {
        this.horses = new ArrayList<>();
    }

    //Constructor con todos los campos.
    public Race(List<Horse> horses, Track track) {
        this.horses = horses != null ? new ArrayList<>(horses) : new ArrayList<>();
        this.track = track;
    }

    //Método para preparar la carrera reseteando el estado de todos los caballos.
    public void start() {
        if (horses == null) {
            horses = new ArrayList<>();
        }
        currentTick = 0;
        finishTicks.clear();
        for (Horse horse : horses) {
            horse.reset();
        }
        started = true;
    }

    //Método para avanzar un instante de la carrera para cada caballo que aun no cruzo la meta.
    public void tick() {
        if (!started || isFinished() || track == null || horses == null) {
            return;
        }
        currentTick++;
        double finishLine = track.getDistance();
        for (Horse horse : horses) {
            if (horse.hasFinished(track)) {
                horse.setDistanceTraveled(finishLine);
                continue;
            }
            horse.advance();
            horse.depleteEnergy();
            if (horse.hasFinished(track)) {
                horse.setDistanceTraveled(finishLine);
                finishTicks.putIfAbsent(horse, currentTick);
            }
        }
    }

    //Método para obtener el caballo que cruzo la meta primero.
    public Horse getWinner() {
        if (horses == null || horses.isEmpty()) {
            return null;
        }

        if (!finishTicks.isEmpty()) {
            Horse winner = null;
            int bestTick = Integer.MAX_VALUE;
            for (Map.Entry<Horse, Integer> entry : finishTicks.entrySet()) {
                if (entry.getValue() < bestTick) {
                    bestTick = entry.getValue();
                    winner = entry.getKey();
                }
            }
            return winner;
        }

        Horse winner = horses.get(0);
        for (Horse horse : horses) {
            if (horse.getDistanceTraveled() > winner.getDistanceTraveled()) {
                winner = horse;
            }
        }
        return winner;
    }

    //Método para obtener la posicion en la carrera del caballo.
    public int getPosition(Horse h) {
        if (h == null || horses == null || !horses.contains(h)) {
            return -1;
        }

        if (finishTicks.containsKey(h)) {
            int position = 1;
            int horseTick = finishTicks.get(h);
            for (Integer otherTick : finishTicks.values()) {
                if (otherTick < horseTick) {
                    position++;
                }
            }
            return position;
        }

        int position = 1;
        double horseDistance = h.getDistanceTraveled();
        for (Horse other : horses) {
            if (other.getDistanceTraveled() > horseDistance) {
                position++;
            }
        }
        return position;
    }

    //Método para verificar si todos los caballos alcanzaron la meta.
    public boolean isFinished() {
        if (track == null || horses == null || horses.isEmpty()) {
            return false;
        }
        for (Horse horse : horses) {
            if (!horse.hasFinished(track)) {
                return false;
            }
        }
        return true;
    }

    //Método para obtener los caballos que participan en la carrera.
    public List<Horse> getHorses() {
        return horses;
    }

    //Método para establecer los caballos que participan en la carrera.
    public void setHorses(List<Horse> horses) {
        this.horses = horses != null ? new ArrayList<>(horses) : new ArrayList<>();
    }

    //Método para obtener la pista sobre la que se corre la carrera.
    public Track getTrack() {
        return track;
    }

    //Método para establecer la pista sobre la que se corre la carrera.
    public void setTrack(Track track) {
        this.track = track;
    }

    //Método para verificar si la carrera ha comenzado.
    public boolean isStarted() {
        return started;
    }
}
