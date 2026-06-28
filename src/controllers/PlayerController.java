package controllers;

import java.util.ArrayList;
import java.util.List;

import daos.PlayerDAO;
import dtos.HorseDTO;
import dtos.PlayerDTO;
import schemas.Horse;
import schemas.Player;

//Controlador responsable de la gestion de jugadores del sistema.
public class PlayerController {

    private static final int WINNER_POINTS = 100;
    private static final int SECOND_PLACE_POINTS = 50;
    private static final int PARTICIPATION_POINTS = 10;

    private static PlayerController instance;

    private final List<Player> players;
    private final PlayerDAO playerDAO;
    private Player currentPlayer;

    private PlayerController() {
        this.players = new ArrayList<>();
        this.playerDAO = new PlayerDAO();
    }

    public static PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }

    //Método para iniciar sesión o registrar un jugador segun el e-mail recibido.
    //Si el e-mail ya existe devuelve sus datos.
    //Si no existe y el nombre viene vacio devuelve null para que la UI pida el nombre.
    //Si no existe y trae nombre lo registra en la base.
    //Método para crear un jugador segun el DTO recibido
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
        players.add(player);
        currentPlayer = player;
        return toDTO(currentPlayer);
    }

    //Método para asignar un caballo al jugador activo por nombre.
    public void selectHorse(String horseName) {
        Horse horse = HorseController.getInstance().getHorseByName(horseName);
        if (horse == null) {
            return;
        }

        HorseDTO dto = new HorseDTO(
                horse.getName(),
                horse.getBaseSpeed(),
                horse.getStamina(),
                horse.getEnergy(),
                horse.getDistanceTraveled()
        );
        selectHorse(dto);
    }

    //Método para asignar un caballo al jugador activo usando la informacion del DTO.
    public void selectHorse(HorseDTO dto) {
        if (currentPlayer == null || dto == null || dto.getName() == null) {
            return;
        }

        Horse horse = findHorseByName(dto.getName());
        if (horse == null) {
            return;
        }

        currentPlayer.selectHorse(horse);

        if (currentPlayer.getId() > 0 && horse.getId() > 0) {
            playerDAO.updateSelectedHorse(currentPlayer.getId(), horse.getId());
        }
    }

    //Método para sumar puntos al jugador activo segun su posicion en la carrera.
    public void addScore(int position) {
        if (currentPlayer == null) {
            return;
        }

        currentPlayer.addScore(calculatePoints(position));

        if (currentPlayer.getId() > 0) {
            playerDAO.update(currentPlayer);
        }
    }

    //Método para obtener el DTO del jugador activo
    public PlayerDTO getPlayerDTO() {
        if (currentPlayer == null) {
            return null;
        }
        return toDTO(currentPlayer);
    }

    //Método para obtener el puntaje acumulado del jugador activo.
    public int getPlayerScore() {
        PlayerDTO dto = getPlayerDTO();
        if (dto == null) {
            return 0;
        }
        return dto.getScore();
    }

    //Método para obtener el jugador activo de la sesion actual.
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //Método para resolver el caballo seleccionado por el jugador
    private void resolveSelectedHorse(Player player) {
        Integer horseId = player.getSelectedHorseId();
        if (horseId == null) {
            return;
        }

        for (Horse horse : HorseController.getInstance().getAvailableHorses()) {
            if (horse.getId() == horseId) {
                player.selectHorse(horse);
                return;
            }
        }
    }

    //Método para sincronizar el jugador en el sistema
    private void syncPlayerInSystem(Player player) {
        for (Player existing : players) {
            if (existing.getId() == player.getId()) {
                return;
            }
        }
        players.add(player);
    }

    //Método para buscar un caballo por nombre dentro del sistema
    private Horse findHorseByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (Horse horse : HorseController.getInstance().getAvailableHorses()) {
            if (name.equalsIgnoreCase(horse.getName())) {
                return horse;
            }
        }
        return null;
    }

    //Método para calcular los puntos segun la posicion en la carrera
    private int calculatePoints(int position) {
        if (position == 1) {
            return WINNER_POINTS;
        }
        if (position == 2) {
            return SECOND_PLACE_POINTS;
        }
        return PARTICIPATION_POINTS;
    }

    //Método para convertir un jugador del dominio a su representacion DTO
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
