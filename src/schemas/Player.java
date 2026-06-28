package schemas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//Representa un jugador que participa en carreras de caballos.
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_horse_id")
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
        if (selectedHorse == null) {
            return null;
        }
        return selectedHorse.getId();
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

