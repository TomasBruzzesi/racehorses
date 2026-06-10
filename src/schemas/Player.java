package schemas;

//Representa un jugador que participa en carreras de caballos.
public class Player {

    private int id;
    private String name;
    private String email;
    private int score;
    private Integer selectedHorseId;
    private Horse selectedHorse;

    //Constructor por defecto.
    public Player() {
    }

    //Constructor con todos los campos.
    public Player(String name, String email) {
        this.name = name;
        this.email = email;
        this.score = 0;
    }

    //Método para asignar el caballo con el que el jugador competira.
    public void selectHorse(Horse h) {
        if (h != null) {
            this.selectedHorse = h;
        }
    }

    //Método para sumar puntos al puntaje acumulado del jugador.
    public void addScore(int pts) {
        if (pts > 0) {
            this.score += pts;
        }
    }

    //Método para obtener el puntaje acumulado del jugador.
    public int getScore() {
        return score;
    }

    //Método para obtener el caballo seleccionado por el jugador.
    public Horse getSelectedHorse() {
        return selectedHorse;
    }

    //Método para obtener el id del caballo seleccionado.
    public Integer getSelectedHorseId() {
        return selectedHorseId;
    }

    //Método para establecer el id del caballo seleccionado.
    public void setSelectedHorseId(Integer selectedHorseId) {
        this.selectedHorseId = selectedHorseId;
    }

    //Método para obtener el id del jugador.
    public int getId() {
        return id;
    }

    //Método para establecer el id del jugador.
    public void setId(int id) {
        this.id = id;
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

    //Método para establecer el puntaje del jugador.
    public void setScore(int score) {
        this.score = Math.max(0, score);
    }
}

