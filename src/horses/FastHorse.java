package horses;

import schemas.Horse;

/**
 * Caballo de velocidad: fuerte al inicio, el sprint se desvanece de forma gradual.
 */
public class FastHorse extends Horse {

    private double speedBoost;

    public FastHorse() {
    }

    public FastHorse(String name, double baseSpeed, double stamina, double speedBoost) {
        super(name, baseSpeed, stamina);
        this.speedBoost = speedBoost;
    }

    @Override
    public void advance() {
        double energyRatio = getEnergyRatio();
        // Sprint que baja progresivamente (no se apaga de golpe al 50%)
        double boostScale = Math.min(1.0, Math.max(0, (energyRatio - 0.08) / 0.80));
        double boost = speedBoost * boostScale;
        double effectiveSpeed = getBaseSpeed()
                * getSmoothEnergyFactor(0.32)
                * (1 + boost)
                * raceForm;
        addDistance(effectiveSpeed);
    }

    @Override
    public void depleteEnergy() {
        double energyRatio = getEnergyRatio();
        // Mas cansancio al sprintar, menos al final para no frenar de golpe
        double drainRate = 0.028 + speedBoost * 0.008 * (0.30 + 0.70 * energyRatio);
        setEnergy(getEnergy() - proportionalDrain(drainRate));
    }

    public double getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(double speedBoost) {
        this.speedBoost = speedBoost;
    }
}
