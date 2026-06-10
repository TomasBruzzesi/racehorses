package dtos;

//DTO para transferir datos de un jugador hacia la capa de controladores.
public class PlayerDTO {

    private String name;
    private String email;
    private int score;
    private String selectedHorseName;

    //Constructor por defecto.
    public PlayerDTO() {
    }

    //Constructor con todos los campos.
    public PlayerDTO(String name, String email, int score, String selectedHorseName) {
        this.name = name;
        this.email = email;
        this.score = score;
        this.selectedHorseName = selectedHorseName;
    }

    //Método para obtener el nombre del jugador.
    public String getName() {
        return name;
    }

    //Método para establecer el nombre del jugador.
    public void setName(String name) {
        this.name = name;
    }

    //Método para obtener el email del jugador.
    public String getEmail() {
        return email;
    }

    //Método para establecer el email del jugador.
    public void setEmail(String email) {
        this.email = email;
    }

    //Método para obtener el puntaje del jugador.
    public int getScore() {
        return score;
    }

    //Método para establecer el puntaje del jugador.
    public void setScore(int score) {
        this.score = score;
    }

    //Método para obtener el nombre del caballo seleccionado.
    public String getSelectedHorseName() {
        return selectedHorseName;
    }

    //Método para establecer el nombre del caballo seleccionado.
    public void setSelectedHorseName(String selectedHorseName) {
        this.selectedHorseName = selectedHorseName;
    }
}
