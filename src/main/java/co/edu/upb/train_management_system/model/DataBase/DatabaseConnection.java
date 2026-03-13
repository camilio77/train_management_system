package co.edu.upb.train_management_system.model.DataBase;

import java.sql.*;

public class DatabaseConnection {
    private static final String URI = "jdbc:postgresql://postgres:trenes170010@db.uvhddzisapmrfmkdlajn.supabase.co:5432/postgres";

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
