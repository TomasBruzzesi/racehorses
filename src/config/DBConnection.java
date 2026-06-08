package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;

    private static final String URL = "jdbc:mysql://localhost:3306/horseRace?createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el driver JDBC de MySQL. Agregar mysql-connector-j al Build Path.", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
