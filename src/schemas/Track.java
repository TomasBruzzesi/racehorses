package schemas;

/**
 * Representa la pista sobre la que se desarrolla una carrera.
 */
public class Track {

    private double totalDistance;

    /**
     * Constructor por defecto.
     */
    public Track() {
    }

    /**
     * @param totalDistance distancia total de la pista en metros
     */
    public Track(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * @return distancia total de la pista
     */
    public double getDistance() {
        return totalDistance;
    }

    /**
     * Indica si una distancia recorrida alcanzo o supero la meta.
     *
     * @param distance metros recorridos por el caballo
     * @return true si la distancia es mayor o igual a la longitud de la pista
     */
    public boolean isCompleted(double distance) {
        if (totalDistance <= 0) {
            return false;
        }
        return distance >= totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = Math.max(0, totalDistance);
    }
}
