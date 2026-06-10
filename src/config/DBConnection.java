package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    //URL de conexión a la DB
    private static final String URL = "jdbc:mysql://localhost:3306/horseRace?createDatabaseIfNotExist=true&serverTimezone=UTC";
    //Usuario de la DB
    private static final String USER = "root";
    //Contraseña de la DB
    private static final String PASSWORD = "root";

    //Constructor de la clase DBConnection
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el driver JDBC de MySQL. Agregar mysql-connector-j al Build Path.", e);
        }
    }

    //Método para obtener la instancia de la clase DBConnection
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    //Método para obtener la conexión a la DB
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
