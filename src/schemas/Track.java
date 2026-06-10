package schemas;

//Representa la pista sobre la que se desarrolla una carrera.
public class Track {

    private double totalDistance;

    //Constructor por defecto.
    public Track() {
    }

    //Constructor con todos los campos.
    public Track(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    //Método para obtener la distancia total de la pista.
    public double getDistance() {
        return totalDistance;
    }

    //Método para verificar si una distancia recorrida alcanzo o supero la meta.
    public boolean isCompleted(double distance) {
        if (totalDistance <= 0) {
            return false;
        }
        return distance >= totalDistance;
    }

    //Método para establecer la distancia total de la pista.
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = Math.max(0, totalDistance);
    }
}
