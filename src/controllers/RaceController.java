package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import daos.RaceDAO;
import dtos.HorseDTO;
import dtos.PlayerDTO;
import dtos.RaceResultDTO;
import schemas.Horse;
import schemas.Player;
import schemas.Race;
import schemas.Track;

//Controlador responsable de la gestion de carreras del sistema.
public class RaceController {

    public static final double DEFAULT_TRACK_DISTANCE = 600.0;
    public static final int MAX_RACE_HORSES = 4;
    private static final int WINNER_POINTS = 100;
    private static final int SECOND_PLACE_POINTS = 50;
    private static final int PARTICIPATION_POINTS = 10;

    private static RaceController instance;

    private final RaceDAO raceDAO;
    private Race currentRace;

    private RaceController() {
        this.raceDAO = new RaceDAO();
    }

    public static RaceController getInstance() {
        if (instance == null) {
            instance = new RaceController();
        }
        return instance;
    }

    //Método para iniciar una nueva carrera con el caballo del jugador y hasta 3 rivales.
    public void launchRace() {
        HorseController.getInstance().resetHorses();
        startRace(buildRaceParticipants());
    }

    //Método para crear e iniciar una nueva carrera con los caballos participantes indicados.
    public void startRace(List<Horse> participants) {
        if (participants == null || participants.isEmpty()) {
            return;
        }

        List<Horse> raceHorses = new ArrayList<>(participants);
        if (raceHorses.size() > MAX_RACE_HORSES) {
            raceHorses = new ArrayList<>(raceHorses.subList(0, MAX_RACE_HORSES));
        }

        Track track = new Track(DEFAULT_TRACK_DISTANCE);
        currentRace = new Race(raceHorses, track);
        currentRace.start();
    }

    //Método para avanzar un instante de la carrera y devolver el estado de los caballos.
    public List<HorseDTO> tick() {
        if (currentRace == null) {
            return new ArrayList<>();
        }

        currentRace.tick();
        return toDTOList(currentRace.getHorses());
    }

    //Método para verificar si la carrera actual ha finalizado.
    public boolean isFinished() {
        return currentRace != null && currentRace.isFinished();
    }

    //Método para obtener la distancia total de la pista en metros.
    public double getTrackDistance() {
        return DEFAULT_TRACK_DISTANCE;
    }

    //Método para finalizar la carrera actual, asignar puntaje y devolver el resultado.
    public RaceResultDTO finishRace() {
        RaceResultDTO result = getResult();
        if (result == null) {
            return null;
        }

        PlayerController.getInstance().addScore(result.getPlayerPosition());

        if (currentRace != null) {
            raceDAO.insert(currentRace);
        }

        return result;
    }

    //Método para obtener la carrera actual en curso.
    public Race getCurrentRace() {
        return currentRace;
    }

    //Método para obtener el resultado de la carrera actual expresado como DTO.
    public RaceResultDTO getResult() {
        if (currentRace == null) {
            return null;
        }

        Horse winner = currentRace.getWinner();
        String winnerName = winner != null ? winner.getName() : null;

        Player activePlayer = getActivePlayer();
        int playerPosition = -1;
        int pointsEarned = 0;

        if (activePlayer != null && activePlayer.getSelectedHorse() != null) {
            playerPosition = currentRace.getPosition(activePlayer.getSelectedHorse());
            pointsEarned = calculatePoints(playerPosition);
        }

        return new RaceResultDTO(winnerName, playerPosition, pointsEarned);
    }

    //Método para armar los participantes de la carrera con el caballo del jugador y rivales aleatorios.
    private List<Horse> buildRaceParticipants() {
        List<Horse> participants = new ArrayList<>();
        HorseController horseCtrl = HorseController.getInstance();
        PlayerDTO player = PlayerController.getInstance().getPlayerDTO();
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

        int slotsLeft = MAX_RACE_HORSES - participants.size();
        for (int i = 0; i < Math.min(slotsLeft, rivals.size()); i++) {
            participants.add(rivals.get(i));
        }

        return participants;
    }

    //Método para obtener el jugador activo de la sesion actual.
    private Player getActivePlayer() {
        return PlayerController.getInstance().getCurrentPlayer();
    }

    //Método para calcular los puntos segun la posicion en la carrera.
    private int calculatePoints(int position) {
        if (position == 1) {
            return WINNER_POINTS;
        }
        if (position == 2) {
            return SECOND_PLACE_POINTS;
        }
        return PARTICIPATION_POINTS;
    }

    //Método para convertir una lista de caballos del dominio a DTOs.
    private List<HorseDTO> toDTOList(List<Horse> horses) {
        List<HorseDTO> dtos = new ArrayList<>();
        for (Horse horse : horses) {
            dtos.add(toDTO(horse));
        }
        return dtos;
    }

    //Método para convertir un caballo del dominio a su representacion DTO.
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
