package controllers;

import java.util.ArrayList;
import java.util.List;

import dtos.HorseDTO;
import dtos.RaceResultDTO;
import schemas.Horse;
import schemas.Player;
import schemas.Race;
import schemas.Track;
import system.RaceSystem;

//Controlador responsable de la gestion de carreras del sistema.
public class RaceController {

    public static final double DEFAULT_TRACK_DISTANCE = 600.0;
    public static final int MAX_RACE_HORSES = 4;
    private static final int WINNER_POINTS = 100;
    private static final int SECOND_PLACE_POINTS = 50;
    private static final int PARTICIPATION_POINTS = 10;

    private RaceSystem raceSystem;
    private Race currentRace;

    //Constructor por defecto. Obtiene la instancia central del sistema.
    public RaceController() {
        this.raceSystem = RaceSystem.getInstance();
    }

    //Método para obtener la instancia del sistema de carreras
    public RaceController(RaceSystem raceSystem) {
        this.raceSystem = raceSystem;
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

    //Método para obtener el jugador activo del sistema (ultimo registrado)
    private Player getActivePlayer() {
        List<Player> players = raceSystem.getPlayers();
        if (players.isEmpty()) {
            return null;
        }
        return players.get(players.size() - 1);
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
