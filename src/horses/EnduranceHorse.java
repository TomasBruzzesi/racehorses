package horses;

import schemas.Horse;

//Caballo de resistencia: pierde energia mas lento y mantiene mejor el ritmo al cansarse.
public class EnduranceHorse extends Horse {

    private double staminaBonus;

    //Constructor por defecto.
    public EnduranceHorse() {
    }

    //Constructor con todos los campos.
    public EnduranceHorse(String name, double baseSpeed, double stamina, double staminaBonus) {
        super(name, baseSpeed, stamina);
        this.staminaBonus = staminaBonus;
    }

    //Método para avanzar el caballo.
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

    //Método para depletar la energia del caballo.
    @Override
    public void depleteEnergy() {
        double drainRate = Math.max(0.018, 0.021 - staminaBonus * 0.003);
        setEnergy(getEnergy() - proportionalDrain(drainRate));
    }

    //Método para obtener el bonus de stamina del caballo.
    public double getStaminaBonus() {
        return staminaBonus;
    }

    //Método para establecer el bonus de stamina del caballo.
    public void setStaminaBonus(double staminaBonus) {
        this.staminaBonus = staminaBonus;
    }
}
