package schemas;

/**
 * Representa un jugador que participa en carreras de caballos.
 */
public class Player {

    private int id;
    private String name;
    private String email;
    private int score;
    private Integer selectedHorseId;
    private Horse selectedHorse;

    /**
     * Constructor por defecto.
     */
    public Player() {
    }

    /**
     * @param name  nombre del jugador
     * @param email correo del jugador
     */
    public Player(String name, String email) {
        this.name = name;
        this.email = email;
        this.score = 0;
    }

    /**
     * Asigna el caballo con el que el jugador competira.
     *
     * @param h caballo seleccionado
     */
    public void selectHorse(Horse h) {
        if (h != null) {
            this.selectedHorse = h;
        }
    }

    /**
     * Suma puntos al puntaje acumulado del jugador.
     *
     * @param pts puntos a agregar
     */
    public void addScore(int pts) {
        if (pts > 0) {
            this.score += pts;
        }
    }

    /**
     * @return puntaje acumulado del jugador
     */
    public int getScore() {
        return score;
    }

    /**
     * @return caballo seleccionado por el jugador
     */
    public Horse getSelectedHorse() {
        return selectedHorse;
    }

    public Integer getSelectedHorseId() {
        return selectedHorseId;
    }

    public void setSelectedHorseId(Integer selectedHorseId) {
        this.selectedHorseId = selectedHorseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setScore(int score) {
        this.score = Math.max(0, score);
    }
}

