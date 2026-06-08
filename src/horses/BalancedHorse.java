package horses;

import schemas.Horse;

/**
 * Caballo equilibrado: ritmo constante y desgaste moderado.
 */
public class BalancedHorse extends Horse {

    private double balanceFactor;

    public BalancedHorse() {
    }

    public BalancedHorse(String name, double baseSpeed, double stamina, double balanceFactor) {
        super(name, baseSpeed, stamina);
        this.balanceFactor = balanceFactor;
    }

    @Override
    public void advance() {
        double effectiveSpeed = getBaseSpeed()
                * getEnergyFactor()
                * balanceFactor
                * raceForm;
        addDistance(effectiveSpeed);
    }

    @Override
    public void depleteEnergy() {
        setEnergy(getEnergy() - proportionalDrain(0.024 * balanceFactor));
    }

    public double getBalanceFactor() {
        return balanceFactor;
    }

    public void setBalanceFactor(double balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
