package dtos;

/**
 * DTO para transferir datos de un caballo hacia la capa de controladores.
 */
public class HorseDTO {

    private String name;
    private double baseSpeed;
    private double stamina;
    private double energy;
    private double distanceTraveled;

    /**
     * Constructor por defecto.
     */
    public HorseDTO() {
    }

    /**
     * @param name              nombre del caballo
     * @param baseSpeed         velocidad base
     * @param stamina           resistencia
     * @param energy            energia actual
     * @param distanceTraveled  distancia recorrida
     */
    public HorseDTO(String name, double baseSpeed, double stamina, double energy, double distanceTraveled) {
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.stamina = stamina;
        this.energy = energy;
        this.distanceTraveled = distanceTraveled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
}
