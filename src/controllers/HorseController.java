package controllers;

import java.util.ArrayList;
import java.util.List;

import daos.HorseDAO;
import dtos.HorseDTO;
import schemas.Horse;

//Controlador responsable de la gestion de caballos del sistema.
public class HorseController {

    private static HorseController instance;

    private final List<Horse> horses;

    private HorseController() {
        this.horses = new ArrayList<>();
    }

    public static HorseController getInstance() {
        if (instance == null) {
            instance = new HorseController();
        }
        return instance;
    }

    //Método para cargar el catalogo de caballos desde la base de datos.
    public void loadHorses() {
        HorseDAO horseDAO = new HorseDAO();
        horseDAO.seedDefaultsIfEmpty();
        horses.clear();
        horses.addAll(horseDAO.findAll());
    }

    //Método para obtener la lista de caballos disponibles en el sistema
    public List<Horse> getAvailableHorses() {
        return new ArrayList<>(horses);
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
        for (Horse horse : horses) {
            horse.reset();
        }
    }

    //Método para obtener el caballo por su nombre, si no existe devuelve null
    public Horse getHorseByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (Horse horse : horses) {
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
