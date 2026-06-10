package controllers;

import java.util.ArrayList;
import java.util.List;

import dtos.HorseDTO;
import schemas.Horse;
import system.RaceSystem;

//Controlador responsable de la gestion de caballos del sistema.
public class HorseController {

    private RaceSystem raceSystem;

    //Constructor por defecto. Obtiene la instancia central del sistema.
    public HorseController() {
        this.raceSystem = RaceSystem.getInstance();
    }

    //Método para obtener la instancia del sistema de carreras
    public HorseController(RaceSystem raceSystem) {
        this.raceSystem = raceSystem;
    }

    //Método para obtener la lista de caballos disponibles en el sistema
    public List<Horse> getAvailableHorses() {
        if (raceSystem == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(raceSystem.getHorses());
    }

    //Método para obtener la lista de caballos expresados como DTO
    public List<HorseDTO> getHorseDTOs() {
        List<HorseDTO> dtos = new ArrayList<>();
        for (Horse horse : getAvailableHorses()) {
            dtos.add(toDTO(horse));
        }
        return dtos;
    }

    //Método para restaurar el estado de todos los caballos para una nueva carrera.
    public void resetHorses() {
        for (Horse horse : getAvailableHorses()) {
            horse.reset();
        }
    }

    //Método para obtener el caballo por su nombre, si no existe devuelve null
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

    //Método para convertir un caballo del dominio a su representacion DTO
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
