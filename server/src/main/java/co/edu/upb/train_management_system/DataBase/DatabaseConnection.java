package co.edu.upb.train_management_system.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.edu.upb.train_management_system.enviroment.Environment;

public class DatabaseConnection {
    private static final String URI = Environment.getInstance().getDatabase();

    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URI);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
