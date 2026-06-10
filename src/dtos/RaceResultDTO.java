package dtos;

//DTO para transferir el resultado de una carrera hacia la capa de controladores.
public class RaceResultDTO {

    private String winnerName;
    private int playerPosition;
    private int pointsEarned;

    //Constructor por defecto.
    public RaceResultDTO() {
    }

    //Constructor con todos los campos.
    public RaceResultDTO(String winnerName, int playerPosition, int pointsEarned) {
        this.winnerName = winnerName;
        this.playerPosition = playerPosition;
        this.pointsEarned = pointsEarned;
    }

    //Método para obtener el nombre del caballo ganador.
    public String getWinnerName() {
        return winnerName;
    }

    //Método para establecer el nombre del caballo ganador.
    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    //Método para obtener la posición del jugador en la carrera.
    public int getPlayerPosition() {
        return playerPosition;
    }

    //Método para establecer la posición del jugador en la carrera.
    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    //Método para obtener los puntos obtenidos segun el resultado.
    public int getPointsEarned() {
        return pointsEarned;
    }

    //Método para establecer los puntos obtenidos segun el resultado.
    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}
