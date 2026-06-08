package dtos;

/**
 * DTO para transferir el resultado de una carrera hacia la capa de controladores.
 */
public class RaceResultDTO {

    private String winnerName;
    private int playerPosition;
    private int pointsEarned;

    /**
     * Constructor por defecto.
     */
    public RaceResultDTO() {
    }

    /**
     * @param winnerName      nombre del caballo ganador
     * @param playerPosition  posicion del jugador en la carrera
     * @param pointsEarned    puntos obtenidos segun el resultado
     */
    public RaceResultDTO(String winnerName, int playerPosition, int pointsEarned) {
        this.winnerName = winnerName;
        this.playerPosition = playerPosition;
        this.pointsEarned = pointsEarned;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}
