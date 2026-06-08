package schemas;

/**
 * Clase abstracta que representa un caballo en el dominio del sistema.
 * Define los atributos comunes y el comportamiento base de la carrera.
 */
public abstract class Horse {

    private int id;
    private String name;
    private double baseSpeed;
    private double stamina;
    private double energy;
    private double distanceTraveled;
    protected double raceForm;

    /**
     * Constructor por defecto.
     */
    public Horse() {
    }

    public Horse(String name, double baseSpeed, double stamina) {
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.stamina = stamina;
        this.energy = stamina;
        this.distanceTraveled = 0;
    }

    /**
     * Avanza el caballo segun su tipo y estado actual.
     * Cada subclase define su propia logica de avance.
     */
    public abstract void advance();

    /**
     * Reduce la energia del caballo despues de cada tick de carrera.
     * Cada subclase define su propia logica de agotamiento.
     */
    public abstract void depleteEnergy();

    /**
     * Indica si el caballo ya completo la distancia total de la pista.
     * @param track pista sobre la que corre el caballo
     * @return true si alcanzo o supero la meta
     */
    public boolean hasFinished(Track track) {
        if (track == null) {
            return false;
        }
        return track.isCompleted(distanceTraveled);
    }

    /**
     * Restaura el estado del caballo para una nueva carrera.
     */
    public void reset() {
        distanceTraveled = 0;
        energy = stamina;
        raceForm = 0.94 + Math.random() * 0.12;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.energy = Math.max(0, energy);
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = Math.max(0, distanceTraveled);
    }

    /**
     * @return proporcion de energia restante entre 0.0 y 1.0
     */
    protected double getEnergyRatio() {
        if (stamina <= 0) {
            return 0;
        }
        return Math.min(1.0, energy / stamina);
    }

    /**
     * Curva suave de rendimiento: baja de forma progresiva, sin frenazo abrupto.
     *
     * @param minFactor ritmo minimo relativo al agotarse (ej. 0.42 = 42% del ritmo)
     */
    protected double getSmoothEnergyFactor(double minFactor) {
        double ratio = getEnergyRatio();
        return minFactor + (1.0 - minFactor) * Math.sqrt(ratio);
    }

    /**
     * Factor de rendimiento estandar para la mayoria de los tipos.
     */
    protected double getEnergyFactor() {
        return getSmoothEnergyFactor(0.42);
    }

    /**
     * Desgaste proporcional a la stamina para que la energia baje de forma gradual.
     */
    protected double proportionalDrain(double staminaFraction) {
        return stamina * staminaFraction;
    }

    /**
     * Suma metros al recorrido actual del caballo.
     * @param meters distancia a agregar
     */
    protected void addDistance(double meters) {
        if (meters > 0) {
            distanceTraveled += meters;
        }
    }
}
