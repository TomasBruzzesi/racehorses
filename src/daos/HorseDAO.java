package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import horses.BalancedHorse;
import horses.EnduranceHorse;
import horses.FastHorse;
import schemas.Horse;

//Acceso a datos de caballos en MySQL.
public class HorseDAO {

    private static final String TYPE_FAST = "FAST";
    private static final String TYPE_BALANCED = "BALANCED";
    private static final String TYPE_ENDURANCE = "ENDURANCE";

    private final DBConnection dbConnection;

    //Constructor por defecto. Obtiene la instancia central de la conexion a la DB.
    public HorseDAO() {
        this.dbConnection = DBConnection.getInstance();
        createTableIfNotExists();
    }

    //Método para crear la tabla de caballos si aun no existe.
    public void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS horses (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL UNIQUE,
                    base_speed DOUBLE NOT NULL,
                    stamina DOUBLE NOT NULL,
                    horse_type VARCHAR(20) NOT NULL,
                    type_value DOUBLE NOT NULL
                )
                """;

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla horses.", e);
        }
    }

    //Método para insertar un caballo y asigna el id generado.
    public int insert(Horse horse) {
        String sql = """
                INSERT INTO horses (name, base_speed, stamina, horse_type, type_value)
                VALUES (?, ?, ?, ?, ?)
                """;

        String horseType = resolveHorseType(horse);
        double typeValue = resolveTypeValue(horse);

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, horse.getName());
            stmt.setDouble(2, horse.getBaseSpeed());
            stmt.setDouble(3, horse.getStamina());
            stmt.setString(4, horseType);
            stmt.setDouble(5, typeValue);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    horse.setId(id);
                    return id;
                }
            }
            throw new RuntimeException("No se pudo obtener el id del caballo insertado.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar caballo.", e);
        }
    }

    //Método para cargar los caballos iniciales si la tabla esta vacia.
    public void seedDefaultsIfEmpty() {
        if (!findAll().isEmpty()) {
            return;
        }

        insert(new FastHorse("Thunder", 5.45, 80.0, 0.4));
        insert(new BalancedHorse("Storm", 4.6, 90.0, 1.0));
        insert(new EnduranceHorse("Blaze", 3.95, 100.0, 0.5));
        insert(new FastHorse("Bolt", 5.45, 75.0, 0.35));
        insert(new BalancedHorse("Mistral", 4.3, 95.0, 1.1));
    }
    
    //Método para obtener todos los caballos del catalogo.
    public List<Horse> findAll() {
        String sql = """
                SELECT id, name, base_speed, stamina, horse_type, type_value
                FROM horses
                ORDER BY id
                """;
        List<Horse> horses = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                horses.add(mapRow(rs));
            }
            return horses;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar caballos.", e);
        }
    }

    //Método para convertir un ResultSet a un objeto Horse.
    private Horse mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double baseSpeed = rs.getDouble("base_speed");
        double stamina = rs.getDouble("stamina");
        String horseType = rs.getString("horse_type");
        double typeValue = rs.getDouble("type_value");

        Horse horse = createHorse(horseType, name, baseSpeed, stamina, typeValue);
        horse.setId(id);
        return horse;
    }

    //Método para crear un objeto Horse segun el tipo de caballo.
    private Horse createHorse(String horseType, String name, double baseSpeed, double stamina, double typeValue) {
        if (TYPE_FAST.equals(horseType)) {
            return new FastHorse(name, baseSpeed, stamina, typeValue);
        }
        if (TYPE_BALANCED.equals(horseType)) {
            return new BalancedHorse(name, baseSpeed, stamina, typeValue);
        }
        if (TYPE_ENDURANCE.equals(horseType)) {
            return new EnduranceHorse(name, baseSpeed, stamina, typeValue);
        }
        throw new RuntimeException("Tipo de caballo no soportado: " + horseType);
    }

    //Método para obtener el tipo de caballo.
    private String resolveHorseType(Horse horse) {
        if (horse instanceof FastHorse) {
            return TYPE_FAST;
        }
        if (horse instanceof BalancedHorse) {
            return TYPE_BALANCED;
        }
        if (horse instanceof EnduranceHorse) {
            return TYPE_ENDURANCE;
        }
        throw new RuntimeException("No se puede persistir un caballo sin subtipo definido.");
    }

    //Método para obtener el valor del tipo de caballo.
    private double resolveTypeValue(Horse horse) {
        if (horse instanceof FastHorse) {
            return ((FastHorse) horse).getSpeedBoost();
        }
        if (horse instanceof BalancedHorse) {
            return ((BalancedHorse) horse).getBalanceFactor();
        }
        if (horse instanceof EnduranceHorse) {
            return ((EnduranceHorse) horse).getStaminaBonus();
        }
        throw new RuntimeException("No se puede persistir un caballo sin subtipo definido.");
    }
}
