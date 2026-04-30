package co.edu.upb.train_management_system.model.user;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class UserService extends UnicastRemoteObject implements UserInterface {
    private static UserService instance;

    protected UserService() throws RemoteException { super(); }

    public static UserService getInstance() throws RemoteException {
        if (instance == null) instance = new UserService();
        return instance;
    }

    @Override
    public AbstractUserWithPower login(String identificacion, String contrasena) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM usuario WHERE identificacion=? AND contrasena=? AND tipo IN ('ADMINISTRADOR','EMPLEADO')");
        stmt.setString(1, identificacion); stmt.setString(2, contrasena);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String tipo = rs.getString("tipo");
            if (tipo.equals("ADMINISTRADOR"))
                return new Admin(rs.getString("identificacion"), rs.getString("nombres"),
                        rs.getString("apellidos"), rs.getString("tipo_identificacion"), rs.getString("contrasena"));
            else
                return new Employee(rs.getString("identificacion"), rs.getString("nombres"),
                        rs.getString("apellidos"), rs.getString("tipo_identificacion"), rs.getString("contrasena"));
        }
        return null;
    }

    @Override
    public boolean registerPassenger(Passenger passenger) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO usuario (identificacion, nombres, apellidos, contrasena, tipo, tipo_identificacion, direccion) VALUES (?, ?, ?, ?, 'PASAJERO', ?, ?)");
        String[] parts = passenger.getFullName().split(" ", 2);
        stmt.setString(1, passenger.getIdentificacion()); stmt.setString(2, parts[0]);
        stmt.setString(3, parts.length > 1 ? parts[1] : ""); stmt.setString(4, passenger.getPassword());
        stmt.setString(5, passenger.getIdentificationType()); stmt.setString(6, passenger.getAddress());
        stmt.executeUpdate();
        return true;
    }

    @Override
    public LinkedList<Passenger> getAllPassengers() throws Exception {
        LinkedList<Passenger> list = new LinkedList<>();
        ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT * FROM usuario WHERE tipo='PASAJERO'");
        while (rs.next())
            list.add(new Passenger(rs.getString("identificacion"), rs.getString("nombres"),
                    rs.getString("apellidos"), rs.getString("tipo_identificacion"),
                    rs.getString("direccion") != null ? rs.getString("direccion") : "",
                    rs.getString("contrasena")));
        return list;
    }

    @Override
    public boolean updatePassenger(String id, String nombres, String apellidos,
                                   String tipoId, String direccion) throws Exception {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
                "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=?, direccion=? WHERE identificacion=? AND tipo='PASAJERO'");
        stmt.setString(1, nombres); stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);  stmt.setString(4, direccion); stmt.setString(5, id);
        stmt.executeUpdate();
        return true;
    }

    @Override
    public boolean deleteUser(String identificacion) throws Exception {
        PreparedStatement stmt = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM usuario WHERE identificacion=?");
        stmt.setString(1, identificacion);
        stmt.executeUpdate();
        return true;
    }

    @Override
    public boolean registerEmployee(Employee employee) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO usuario (identificacion, nombres, apellidos, contrasena, tipo, tipo_identificacion) VALUES (?, ?, ?, ?, 'EMPLEADO', ?)");
        String[] parts = employee.getFullName().split(" ", 2);
        stmt.setString(1, employee.getIdentificacion()); stmt.setString(2, parts[0]);
        stmt.setString(3, parts.length > 1 ? parts[1] : ""); stmt.setString(4, employee.getPassword());
        stmt.setString(5, employee.getIdentificationType());
        stmt.executeUpdate();
        return true;
    }

    @Override
    public LinkedList<Employee> getAllEmployees() throws Exception {
        LinkedList<Employee> list = new LinkedList<>();
        ResultSet rs = DatabaseConnection.getConnection().createStatement()
                .executeQuery("SELECT * FROM usuario WHERE tipo='EMPLEADO'");
        while (rs.next())
            list.add(new Employee(rs.getString("identificacion"), rs.getString("nombres"),
                    rs.getString("apellidos"), rs.getString("tipo_identificacion"), rs.getString("contrasena")));
        return list;
    }

    @Override
    public boolean updateEmployee(String id, String nombres, String apellidos, String tipoId) throws Exception {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
                "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=? WHERE identificacion=? AND tipo='EMPLEADO'");
        stmt.setString(1, nombres); stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);  stmt.setString(4, id);
        stmt.executeUpdate();
        return true;
    }

    @Override
    public boolean updateAdmin(String id, String nombres, String apellidos,
                               String tipoId, String pass) throws Exception {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
                "UPDATE usuario SET nombres=?, apellidos=?, tipo_identificacion=?, contrasena=? WHERE identificacion=? AND tipo='ADMINISTRADOR'");
        stmt.setString(1, nombres); stmt.setString(2, apellidos);
        stmt.setString(3, tipoId);  stmt.setString(4, pass); stmt.setString(5, id);
        stmt.executeUpdate();
        return true;
    }
}