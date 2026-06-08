package controllers;

import daos.PlayerDAO;
import dtos.HorseDTO;
import dtos.PlayerDTO;
import schemas.Horse;
import schemas.Player;
import system.RaceSystem;

/**
 * Controlador responsable de la gestion de jugadores del sistema.
 * Convierte entre el dominio (Player) y los DTOs de transferencia.
 */
public class PlayerController {

    private static final int WINNER_POINTS = 100;
    private static final int SECOND_PLACE_POINTS = 50;
    private static final int PARTICIPATION_POINTS = 10;

    private RaceSystem raceSystem;
    private PlayerDAO playerDAO;
    private Player currentPlayer;

    /**
     * Constructor por defecto. Obtiene la instancia central del sistema.
     */
    public PlayerController() {
        this(RaceSystem.getInstance());
    }

    /**
     * @param raceSystem instancia del sistema de carreras
     */
    public PlayerController(RaceSystem raceSystem) {
        this.raceSystem = raceSystem;
        this.playerDAO = new PlayerDAO();
    }

    /**
     * Inicia sesion o registra un jugador segun el e-mail recibido.
     * Si el e-mail ya existe devuelve sus datos.
     * Si no existe y el nombre viene vacio devuelve null para que la UI pida el nombre.
     * Si no existe y trae nombre lo registra en la base.
     *
     * @param dto datos del jugador (e-mail obligatorio, nombre solo para registro)
     * @return DTO del jugador o null si falta el nombre para un e-mail nuevo
     */
    public PlayerDTO createPlayer(PlayerDTO dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            return null;
        }

        String email = dto.getEmail().trim();
        Player existingPlayer = playerDAO.findByEmail(email);
        if (existingPlayer != null) {
            currentPlayer = existingPlayer;
            resolveSelectedHorse(currentPlayer);
            syncPlayerInSystem(currentPlayer);
            return toDTO(currentPlayer);
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            return null;
        }

        Player player = new Player(dto.getName().trim(), email);
        playerDAO.insert(player);
        raceSystem.getPlayers().add(player);
        currentPlayer = player;
        return toDTO(currentPlayer);
    }

    /**
     * Asigna un caballo al jugador activo usando la informacion del DTO.
     *
     * @param dto DTO con el nombre del caballo seleccionado
     */
    public void selectHorse(HorseDTO dto) {
        if (currentPlayer == null || dto == null || dto.getName() == null) {
            return;
        }

        Horse horse = findHorseByName(dto.getName());
        if (horse == null) {
            return;
        }

        currentPlayer.selectHorse(horse);
        currentPlayer.setSelectedHorseId(horse.getId());

        if (currentPlayer.getId() > 0 && horse.getId() > 0) {
            playerDAO.updateSelectedHorse(currentPlayer.getId(), horse.getId());
        }
    }

    /**
     * Suma puntos al jugador activo segun su posicion en la carrera.
     *
     * @param position posicion final del jugador (1 = ganador)
     */
    public void addScore(int position) {
        if (currentPlayer == null) {
            return;
        }

        currentPlayer.addScore(calculatePoints(position));

        if (currentPlayer.getId() > 0) {
            playerDAO.updateScore(currentPlayer.getId(), currentPlayer.getScore());
        }
    }

    /**
     * @return DTO del jugador activo
     */
    public PlayerDTO getPlayerDTO() {
        if (currentPlayer == null) {
            return null;
        }
        return toDTO(currentPlayer);
    }

    private void resolveSelectedHorse(Player player) {
        Integer horseId = player.getSelectedHorseId();
        if (horseId == null) {
            return;
        }

        for (Horse horse : raceSystem.getHorses()) {
            if (horse.getId() == horseId) {
                player.selectHorse(horse);
                return;
            }
        }
    }

    private void syncPlayerInSystem(Player player) {
        for (Player existing : raceSystem.getPlayers()) {
            if (existing.getId() == player.getId()) {
                return;
            }
        }
        raceSystem.getPlayers().add(player);
    }

    /**
     * Busca un caballo por nombre dentro del sistema.
     *
     * @param name nombre del caballo
     * @return caballo encontrado o null
     */
    private Horse findHorseByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (Horse horse : raceSystem.getHorses()) {
            if (name.equalsIgnoreCase(horse.getName())) {
                return horse;
            }
        }
        return null;
    }

    /**
     * Calcula los puntos segun la posicion en la carrera.
     *
     * @param position posicion final del jugador
     * @return puntos a otorgar
     */
    private int calculatePoints(int position) {
        if (position == 1) {
            return WINNER_POINTS;
        }
        if (position == 2) {
            return SECOND_PLACE_POINTS;
        }
        return PARTICIPATION_POINTS;
    }

    /**
     * Convierte un jugador del dominio a su representacion DTO.
     *
     * @param player jugador del dominio
     * @return DTO con los datos del jugador
     */
    private PlayerDTO toDTO(Player player) {
        String selectedHorseName = null;
        if (player.getSelectedHorse() != null) {
            selectedHorseName = player.getSelectedHorse().getName();
        }
        return new PlayerDTO(
                player.getName(),
                player.getEmail(),
                player.getScore(),
                selectedHorseName
        );
    }
}
