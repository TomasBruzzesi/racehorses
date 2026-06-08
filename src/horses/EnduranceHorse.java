package horses;

import schemas.Horse;

/**
 * Caballo de resistencia: pierde energia mas lento y mantiene mejor el ritmo al cansarse.
 */
public class EnduranceHorse extends Horse {

    private double staminaBonus;

    public EnduranceHorse() {
    }

    public EnduranceHorse(String name, double baseSpeed, double stamina, double staminaBonus) {
        super(name, baseSpeed, stamina);
        this.staminaBonus = staminaBonus;
    }

    @Override
    public void advance() {
        // Curva mas suave que el resto: aguanta ritmo sin quedarse plano
        double enduranceFactor = getSmoothEnergyFactor(0.48);
        double effectiveSpeed = getBaseSpeed()
                * enduranceFactor
                * (1 + staminaBonus * 0.05)
                * raceForm;
        addDistance(effectiveSpeed);
    }

    @Override
    public void depleteEnergy() {
        double drainRate = Math.max(0.018, 0.021 - staminaBonus * 0.003);
        setEnergy(getEnergy() - proportionalDrain(drainRate));
    }

    public double getStaminaBonus() {
        return staminaBonus;
    }

    public void setStaminaBonus(double staminaBonus) {
        this.staminaBonus = staminaBonus;
    }
}
