package dtos;

//DTO para transferir datos de un caballo hacia la capa de controladores.
public class HorseDTO {

    private String name;
    private double baseSpeed;
    private double stamina;
    private double energy;
    private double distanceTraveled;

    //Constructor por defecto.
    public HorseDTO() {
    }

    //Constructor con todos los campos.
    public HorseDTO(String name, double baseSpeed, double stamina, double energy, double distanceTraveled) {
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.stamina = stamina;
        this.energy = energy;
        this.distanceTraveled = distanceTraveled;
    }

    //Método para obtener el nombre del caballo.
    public String getName() {
        return name;
    }

    //Método para establecer el nombre del caballo.
    public void setName(String name) {
        this.name = name;
    }

    //Método para obtener la velocidad base del caballo.
    public double getBaseSpeed() {
        return baseSpeed;
    }

    //Método para establecer la velocidad base del caballo.
    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    //Método para obtener la resistencia del caballo.
    public double getStamina() {
        return stamina;
    }

    //Método para establecer la resistencia del caballo.
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    //Método para obtener la energia actual del caballo.
    public double getEnergy() {
        return energy;
    }

    //Método para establecer la energia actual del caballo.
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    //Método para obtener la distancia recorrida del caballo.
        public double getDistanceTraveled() {
        return distanceTraveled;
    }

    //Método para establecer la distancia recorrida del caballo.
    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
}
