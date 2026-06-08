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

/**
 * Controlador principal que orquesta los demas controladores del sistema.
 * Punto de entrada entre la interfaz y la logica de negocio.
 */
public class RaceSystemController {

    private PlayerController playerCtrl;
    private HorseController horseCtrl;
    private RaceController raceCtrl;
    private RaceDAO raceDAO;

    /**
     * Constructor por defecto.
     */
    public RaceSystemController() {
    }

    /**
     * Inicializa el sistema, los controladores y los caballos por defecto.
     */
    public void initSystem() {
        RaceSystem raceSystem = RaceSystem.getInstance();
        playerCtrl = new PlayerController(raceSystem);
        horseCtrl = new HorseController(raceSystem);
        raceCtrl = new RaceController(raceSystem);
        raceDAO = new RaceDAO();
        loadHorses(raceSystem);
    }

    /**
     * Inicia sesion o registra un jugador segun el e-mail.
     * Si el e-mail ya existe devuelve sus datos.
     * Si no existe y falta el nombre devuelve null para que la UI lo solicite.
     *
     * @param dto datos del jugador
     * @return DTO del jugador o null si el e-mail es nuevo y falta el nombre
     */
    public PlayerDTO registerPlayer(PlayerDTO dto) {
        return playerCtrl.createPlayer(dto);
    }

    /**
     * Inicia una nueva carrera con el caballo del jugador y hasta 3 rivales.
     */
    public void launchRace() {
        horseCtrl.resetHorses();
        raceCtrl.startRace(buildRaceParticipants());
    }

    /**
     * @return distancia total de la pista en metros
     */
    public double getRaceTrackDistance() {
        return RaceController.DEFAULT_TRACK_DISTANCE;
    }

    /**
     * Finaliza la carrera actual, asigna puntaje y devuelve el resultado.
     *
     * @return resultado de la carrera
     */
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

    /**
     * @return puntaje acumulado del jugador activo
     */
    public int getPlayerScore() {
        PlayerDTO playerDTO = playerCtrl.getPlayerDTO();
        if (playerDTO == null) {
            return 0;
        }
        return playerDTO.getScore();
    }

    /**
     * @return lista de caballos disponibles expresados como DTO
     */
    public List<HorseDTO> getAvailableHorses() {
        return horseCtrl.getHorseDTOs();
    }

    /**
     * @param playerId  identificador del jugador (reservado para uso futuro)
     * @param horseName nombre del caballo a seleccionar
     */
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

    /**
     * Avanza un instante de la carrera en curso.
     *
     * @return estado actual de los caballos
     */
    public List<HorseDTO> tickRace() {
        return raceCtrl.tick();
    }

    /**
     * @return true si la carrera actual finalizo
     */
    public boolean isRaceFinished() {
        return raceCtrl.isFinished();
    }

    /**
     * Carga el catalogo de caballos desde la base de datos.
     *
     * @param raceSystem instancia central del sistema
     */
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

    private void loadHorses(RaceSystem raceSystem) {
        HorseDAO horseDAO = new HorseDAO();
        horseDAO.seedDefaultsIfEmpty();

        List<Horse> horses = raceSystem.getHorses();
        horses.clear();
        horses.addAll(horseDAO.findAll());
    }
}
