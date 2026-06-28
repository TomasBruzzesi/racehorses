package schemas;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

//Clase abstracta que representa un caballo en el dominio del sistema.
@Entity
@Table(name = "horses")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "horse_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "base_speed", nullable = false)
    private double baseSpeed;

    @Column(nullable = false)
    private double stamina;

    @Transient
    private double energy;

    @Transient
    private double distanceTraveled;

    @Transient
    protected double raceForm;

    protected Horse() {
    }

    //Constructor con todos los campos.
    public Horse(String name, double baseSpeed, double stamina) {
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.stamina = stamina;
        this.energy = stamina;
        this.distanceTraveled = 0;
    }

    //Método para avanzar el caballo segun su tipo y estado actual.
    public abstract void advance();

    //Método para depletar la energia del caballo despues de cada tick de carrera.
    public abstract void depleteEnergy();

    //Método para indicar si el caballo ya completo la distancia total de la pista.
    public boolean hasFinished(Track track) {
        if (track == null) {
            return false;
        }
        return track.isCompleted(distanceTraveled);
    }

    //Método para restaurar el estado del caballo para una nueva carrera.
    public void reset() {
        distanceTraveled = 0;
        energy = stamina;
        raceForm = 0.94 + Math.random() * 0.12;
    }

    //Método para obtener el id del caballo.
    public int getId() {
        return id;
    }

    //Método para establecer el id del caballo.
    public void setId(int id) {
        this.id = id;
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

    //Método para obtener la stamina del caballo.
    public double getStamina() {
        return stamina;
    }

    //Método para establecer la stamina del caballo.
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    //Método para obtener la energia del caballo.
    public double getEnergy() {
        return energy;
    }

    //Método para establecer la energia del caballo.
    public void setEnergy(double energy) {
        this.energy = Math.max(0, energy);
    }

    //Método para obtener la distancia recorrida del caballo.
    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    //Método para establecer la distancia recorrida del caballo.
    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = Math.max(0, distanceTraveled);
    }

    //Método para obtener la proporcion de energia restante entre 0.0 y 1.0
    protected double getEnergyRatio() {
        if (stamina <= 0) {
            return 0;
        }
        return Math.min(1.0, energy / stamina);
    }

    //Método para obtener la curva suave de rendimiento: baja de forma progresiva, sin frenazo abrupto.
    protected double getSmoothEnergyFactor(double minFactor) {
        double ratio = getEnergyRatio();
        return minFactor + (1.0 - minFactor) * Math.sqrt(ratio);
    }

    //Método para obtener el factor de rendimiento estandar para la mayoria de los tipos.
    protected double getEnergyFactor() {
        return getSmoothEnergyFactor(0.42);
    }

    //Método para obtener el desgaste proporcional a la stamina para que la energia baje de forma gradual.
    protected double proportionalDrain(double staminaFraction) {
        return stamina * staminaFraction;
    }

    //Método para sumar metros al recorrido actual del caballo.
    protected void addDistance(double meters) {
        if (meters > 0) {
            distanceTraveled += meters;
        }
    }
}
