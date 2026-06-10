package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import daos.HorseDAO;
import daos.RaceDAO;
import dtos.HorseDTO;
import dtos.PlayerDTO;
import dtos.RaceResultDTO;
import schemas.Horse;
import schemas.Race;
import system.RaceSystem;

//Controlador principal que orquesta los demas controladores del sistema.
public class RaceSystemController {

    private PlayerController playerCtrl;
    private HorseController horseCtrl;
    private RaceController raceCtrl;
    private RaceDAO raceDAO;

    //Constructor por defecto.
    public RaceSystemController() {
    }

    //Método para inicializar el sistema, los controladores y los caballos por defecto.
    public void initSystem() {
        RaceSystem raceSystem = RaceSystem.getInstance();
        playerCtrl = new PlayerController(raceSystem);
        horseCtrl = new HorseController(raceSystem);
        raceCtrl = new RaceController(raceSystem);
        raceDAO = new RaceDAO();
        loadHorses(raceSystem);
    }

    //Método para iniciar sesión o registrar un jugador segun el e-mail.
    public PlayerDTO registerPlayer(PlayerDTO dto) {
        return playerCtrl.createPlayer(dto);
    }

    //Método para iniciar una nueva carrera con el caballo del jugador y hasta 3 rivales.
    public void launchRace() {
        horseCtrl.resetHorses();
        raceCtrl.startRace(buildRaceParticipants());
    }

    //Método para obtener la distancia total de la pista en metros
    public double getRaceTrackDistance() {
        return RaceController.DEFAULT_TRACK_DISTANCE;
    }

    //Método para finalizar la carrera actual, asignar puntaje y devolver el resultado.
    public RaceResultDTO finishRace() {
        RaceResultDTO result = raceCtrl.getResult();
        if (result == null) {
            return null;
        }

        playerCtrl.addScore(result.getPlayerPosition());

        Race completedRace = raceCtrl.getCurrentRace();
        if (completedRace != null) {
            raceDAO.insert(completedRace);
        }

        return result;
    }

    //Método para obtener el puntaje acumulado del jugador activo
    public int getPlayerScore() {
        PlayerDTO playerDTO = playerCtrl.getPlayerDTO();
        if (playerDTO == null) {
            return 0;
        }
        return playerDTO.getScore();
    }

    //Método para obtener la lista de caballos disponibles expresados como DTO
    public List<HorseDTO> getAvailableHorses() {
        return horseCtrl.getHorseDTOs();
    }

    //Método para seleccionar un caballo para el jugador activo
    public void selectHorse(String playerId, String horseName) {
        Horse horse = horseCtrl.getHorseByName(horseName);
        if (horse == null) {
            return;
        }

        HorseDTO horseDTO = new HorseDTO(
                horse.getName(),
                horse.getBaseSpeed(),
                horse.getStamina(),
                horse.getEnergy(),
                horse.getDistanceTraveled()
        );
        playerCtrl.selectHorse(horseDTO);
    }

    //Método para avanzar un instante de la carrera en curso.
    public List<HorseDTO> tickRace() {
        return raceCtrl.tick();
    }

    //Método para verificar si la carrera actual ha finalizado.
    public boolean isRaceFinished() {
        return raceCtrl.isFinished();
    }

    //Método para cargar el catalogo de caballos desde la base de datos.
    private List<Horse> buildRaceParticipants() {
        List<Horse> participants = new ArrayList<>();
        PlayerDTO player = playerCtrl.getPlayerDTO();
        Horse playerHorse = null;

        if (player != null && player.getSelectedHorseName() != null) {
            playerHorse = horseCtrl.getHorseByName(player.getSelectedHorseName());
        }

        if (playerHorse != null) {
            participants.add(playerHorse);
        }

        List<Horse> rivals = new ArrayList<>();
        for (Horse horse : horseCtrl.getAvailableHorses()) {
            if (playerHorse == null || !horse.getName().equalsIgnoreCase(playerHorse.getName())) {
                rivals.add(horse);
            }
        }
        Collections.shuffle(rivals);

        int slotsLeft = RaceController.MAX_RACE_HORSES - participants.size();
        for (int i = 0; i < Math.min(slotsLeft, rivals.size()); i++) {
            participants.add(rivals.get(i));
        }

        return participants;
    }

    //Método para cargar el catalogo de caballos desde la base de datos.
    private void loadHorses(RaceSystem raceSystem) {
        HorseDAO horseDAO = new HorseDAO();
        horseDAO.seedDefaultsIfEmpty();

        List<Horse> horses = raceSystem.getHorses();
        horses.clear();
        horses.addAll(horseDAO.findAll());
    }
}
