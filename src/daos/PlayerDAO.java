package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import schemas.Player;

/**
 * Acceso a datos de jugadores en MySQL.
 */
public class PlayerDAO {

    private final DBConnection dbConnection;

    public PlayerDAO() {
        this.dbConnection = DBConnection.getInstance();
        createTableIfNotExists();
    }

    /**
     * Crea la tabla de jugadores si aun no existe.
     */
    public void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS players (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    score INT NOT NULL DEFAULT 0,
                    selected_horse_id INT NULL
                )
                """;

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla players.", e);
        }
    }

    /**
     * Inserta un jugador y asigna el id generado.
     *
     * @param player jugador a persistir
     * @return id generado
     */
    public int insert(Player player) {
        String sql = "INSERT INTO players (name, email, score, selected_horse_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, player.getName());
            stmt.setString(2, player.getEmail());
            stmt.setInt(3, player.getScore());
            setNullableInt(stmt, 4, getSelectedHorseId(player));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    player.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se pudo obtener el id del jugador insertado.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar jugador.", e);
        }
    }

    /**
     * Actualiza el puntaje de un jugador.
     *
     * @param playerId identificador del jugador
     * @param score    nuevo puntaje
     */
    public void updateScore(int playerId, int score) {
        String sql = "UPDATE players SET score = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, score);
            stmt.setInt(2, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar puntaje del jugador.", e);
        }
    }

    /**
     * Asigna el caballo seleccionado de un jugador.
     *
     * @param playerId identificador del jugador
     * @param horseId  identificador del caballo seleccionado
     */
    public void updateSelectedHorse(int playerId, int horseId) {
        String sql = "UPDATE players SET selected_horse_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, horseId);
            stmt.setInt(2, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar caballo seleccionado.", e);
        }
    }

    /**
     * @param id identificador del jugador
     * @return jugador encontrado o null
     */
    public Player findById(int id) {
        String sql = "SELECT id, name, email, score, selected_horse_id FROM players WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jugador por id.", e);
        }
    }

    /**
     * @param email correo del jugador
     * @return jugador encontrado o null
     */
    public Player findByEmail(String email) {
        String sql = "SELECT id, name, email, score, selected_horse_id FROM players WHERE email = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar jugador por email.", e);
        }
    }

    /**
     * @return todos los jugadores registrados
     */
    public List<Player> findAll() {
        String sql = "SELECT id, name, email, score, selected_horse_id FROM players";
        List<Player> players = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                players.add(mapRow(rs));
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar jugadores.", e);
        }
    }

    private Player mapRow(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getInt("id"));
        player.setName(rs.getString("name"));
        player.setEmail(rs.getString("email"));
        player.setScore(rs.getInt("score"));

        int horseId = rs.getInt("selected_horse_id");
        if (!rs.wasNull()) {
            player.setSelectedHorseId(horseId);
        }
        return player;
    }

    private Integer getSelectedHorseId(Player player) {
        return player.getSelectedHorseId();
    }

    private void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }
}
