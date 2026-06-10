package horses;

import schemas.Horse;

//Caballo equilibrado: ritmo constante y desgaste moderado.
public class BalancedHorse extends Horse {

    private double balanceFactor;

    //Constructor por defecto.
    public BalancedHorse() {
    }

    //Constructor con todos los campos.
    public BalancedHorse(String name, double baseSpeed, double stamina, double balanceFactor) {
        super(name, baseSpeed, stamina);
        this.balanceFactor = balanceFactor;
    }

    //Método para avanzar el caballo.
    @Override
    public void advance() {
        double effectiveSpeed = getBaseSpeed()
                * getEnergyFactor()
                * balanceFactor
                * raceForm;
        addDistance(effectiveSpeed);
    }

    //Método para depletar la energia del caballo.
    @Override
    public void depleteEnergy() {
        setEnergy(getEnergy() - proportionalDrain(0.024 * balanceFactor));
    }

    //Método para obtener el factor de balance del caballo.
    public double getBalanceFactor() {
        return balanceFactor;
    }

    //Método para establecer el factor de balance del caballo.
    public void setBalanceFactor(double balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
