package co.edu.upb.train_management_system.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;

public class UserService {

    private static UserService instance;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    public AbstractUserWithPower login(String identificacion, String contrasena) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM usuario WHERE identificacion = ? AND contrasena = ? AND tipo = 'ADMINISTRADOR'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, identificacion);
        stmt.setString(2, contrasena);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Admin(
                    rs.getString("identificacion"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("tipo_identificacion"),
                    rs.getString("contrasena")
            );
        }
        return null;
    }

    public boolean registerPassenger(Passenger passenger) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO usuario (identificacion, nombres, apellidos, contrasena, tipo, tipo_identificacion, direccion) VALUES (?, ?, ?, ?, 'PASAJERO', ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, String.valueOf(passenger.getIdentificacion()));
        stmt.setString(2, passenger.getFullName().split(" ")[0]);
        stmt.setString(3, passenger.getFullName().split(" ")[1]);
        stmt.setString(4, passenger.getPassword());
        stmt.setString(5, passenger.getIdentificationType());
        stmt.setString(6, passenger.getAddress());
        stmt.executeUpdate();
        return true;
    }
}