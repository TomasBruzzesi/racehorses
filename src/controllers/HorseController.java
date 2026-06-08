package controllers;

import java.util.ArrayList;
import java.util.List;

import dtos.HorseDTO;
import schemas.Horse;
import system.RaceSystem;

/**
 * Controlador responsable de la gestion de caballos del sistema.
 * Convierte entre el dominio (Horse) y los DTOs de transferencia.
 */
public class HorseController {

    private RaceSystem raceSystem;

    /**
     * Constructor por defecto. Obtiene la instancia central del sistema.
     */
    public HorseController() {
        this.raceSystem = RaceSystem.getInstance();
    }

    /**
     * @param raceSystem instancia del sistema de carreras
     */
    public HorseController(RaceSystem raceSystem) {
        this.raceSystem = raceSystem;
    }

    /**
     * @return lista de caballos disponibles en el sistema
     */
    public List<Horse> getAvailableHorses() {
        if (raceSystem == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(raceSystem.getHorses());
    }

    /**
     * @return lista de caballos expresados como DTO
     */
    public List<HorseDTO> getHorseDTOs() {
        List<HorseDTO> dtos = new ArrayList<>();
        for (Horse horse : getAvailableHorses()) {
            dtos.add(toDTO(horse));
        }
        return dtos;
    }

    /**
     * Restaura el estado de todos los caballos para una nueva carrera.
     */
    public void resetHorses() {
        for (Horse horse : getAvailableHorses()) {
            horse.reset();
        }
    }

    /**
     * @param name nombre del caballo buscado
     * @return caballo encontrado o null si no existe
     */
    public Horse getHorseByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (Horse horse : getAvailableHorses()) {
            if (name.equalsIgnoreCase(horse.getName())) {
                return horse;
            }
        }
        return null;
    }

    /**
     * Convierte un caballo del dominio a su representacion DTO.
     *
     * @param horse caballo del dominio
     * @return DTO con los datos del caballo
     */
    private HorseDTO toDTO(Horse horse) {
        return new HorseDTO(
                horse.getName(),
                horse.getBaseSpeed(),
                horse.getStamina(),
                horse.getEnergy(),
                horse.getDistanceTraveled()
        );
    }
}
