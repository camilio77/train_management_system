package co.edu.upb.train_management_system.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT * FROM usuario WHERE identificacion = ? AND contrasena = ? AND tipo IN ('ADMINISTRADOR', 'EMPLEADO')";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, identificacion);
        stmt.setString(2, contrasena);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String tipo = rs.getString("tipo");
            if (tipo.equals("ADMINISTRADOR")) {
                return new Admin(
                    rs.getString("identificacion"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("tipo_identificacion"),
                    rs.getString("contrasena")
                );
            } else {
                return new Employee(
                    rs.getString("identificacion"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("tipo_identificacion"),
                    rs.getString("contrasena")
                );
            }
        }
        return null;
    }

    // ── PASAJEROS ──────────────────────────────────────────────────

    public boolean registerPassenger(Passenger passenger) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO usuario (identificacion, nombres, apellidos, contrasena, tipo, tipo_identificacion, direccion) VALUES (?, ?, ?, ?, 'PASAJERO', ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        String[] parts = passenger.getFullName().split(" ", 2);
        stmt.setString(1, passenger.getIdentificacion());
        stmt.setString(2, parts[0]);
        stmt.setString(3, parts.length > 1 ? parts[1] : "");
        stmt.setString(4, passenger.getPassword());
        stmt.setString(5, passenger.getIdentificationType());
        stmt.setString(6, passenger.getAddress());
        stmt.executeUpdate();
        return true;
    }

    public List<Passenger> getAllPassengers() throws Exception {
        List<Passenger> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM usuario WHERE tipo = 'PASAJERO'";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            list.add(new Passenger(
                rs.getString("identificacion"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("tipo_identificacion"),
                rs.getString("direccion") != null ? rs.getString("direccion") : "",
                rs.getString("contrasena")
            ));
        }
        return list;
    }

    public boolean updatePassenger(String identificacion, String nombres, String apellidos,
                                   String tipoId, String direccion) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=?, direccion=? WHERE identificacion=? AND tipo='PASAJERO'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombres);
        stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);
        stmt.setString(4, direccion);
        stmt.setString(5, identificacion);
        stmt.executeUpdate();
        return true;
    }

    public boolean deleteUser(String identificacion) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM usuario WHERE identificacion=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, identificacion);
        stmt.executeUpdate();
        return true;
    }

    // ── EMPLEADOS ──────────────────────────────────────────────────

    public boolean registerEmployee(Employee employee) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO usuario (identificacion, nombres, apellidos, contrasena, tipo, tipo_identificacion) VALUES (?, ?, ?, ?, 'EMPLEADO', ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        String[] parts = employee.getFullName().split(" ", 2);
        stmt.setString(1, employee.getIdentificacion());
        stmt.setString(2, parts[0]);
        stmt.setString(3, parts.length > 1 ? parts[1] : "");
        stmt.setString(4, employee.getPassword());
        stmt.setString(5, employee.getIdentificationType());
        stmt.executeUpdate();
        return true;
    }

    public List<Employee> getAllEmployees() throws Exception {
        List<Employee> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM usuario WHERE tipo = 'EMPLEADO'";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            list.add(new Employee(
                rs.getString("identificacion"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("tipo_identificacion"),
                rs.getString("contrasena")
            ));
        }
        return list;
    }

    public boolean updateEmployee(String identificacion, String nombres, String apellidos,
                                  String tipoId) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=? WHERE identificacion=? AND tipo='EMPLEADO'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombres);
        stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);
        stmt.setString(4, identificacion);
        stmt.executeUpdate();
        return true;
    }

    // ── ADMIN ──────────────────────────────────────────────────────

    public boolean updateAdmin(String identificacion, String nombres, String apellidos,
                               String tipoId, String nuevaContrasena) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=?, contrasena=? WHERE identificacion=? AND tipo='ADMINISTRADOR'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombres);
        stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);
        stmt.setString(4, nuevaContrasena);
        stmt.setString(5, identificacion);
        stmt.executeUpdate();
        return true;
    }
}