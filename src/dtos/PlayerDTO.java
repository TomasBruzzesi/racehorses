package dtos;

/**
 * DTO para transferir datos de un jugador hacia la capa de controladores.
 */
public class PlayerDTO {

    private String name;
    private String email;
    private int score;
    private String selectedHorseName;

    /**
     * Constructor por defecto.
     */
    public PlayerDTO() {
    }

    /**
     * @param name               nombre del jugador
     * @param email              correo del jugador
     * @param score              puntaje acumulado
     * @param selectedHorseName  nombre del caballo seleccionado
     */
    public PlayerDTO(String name, String email, int score, String selectedHorseName) {
        this.name = name;
        this.email = email;
        this.score = score;
        this.selectedHorseName = selectedHorseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSelectedHorseName() {
        return selectedHorseName;
    }

    public void setSelectedHorseName(String selectedHorseName) {
        this.selectedHorseName = selectedHorseName;
    }
}
