package co.edu.upb.train_management_system.model.train;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;

import java.sql.*;

public class TrainService {
    private static TrainService instance;
    private TrainService() {}
    public static TrainService getInstance() {
        if (instance == null) instance = new TrainService();
        return instance;
    }

    public LinkedList<Train> getAll() throws SQLException {
        LinkedList<Train> list = new LinkedList<>();
        String sql = "SELECT * FROM tren";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            Train t = new Train(String.valueOf(rs.getInt("id_tren")), rs.getString("nombre"));
            t.setType(rs.getString("tipo"));
            t.setMileage(rs.getInt("kilometraje"));
            list.add(t);
        }
        return list;
    }

    public void create(String nombre, String tipo) throws SQLException {
        String sql = "INSERT INTO tren (nombre, tipo) VALUES (?, ?)";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, tipo);
        stmt.executeUpdate();
    }

    public void update(int id, String nombre, String tipo, int kilometraje) throws SQLException {
        String sql = "UPDATE tren SET nombre=?, tipo=?, kilometraje=? WHERE id_tren=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, tipo);
        stmt.setInt(3, kilometraje);
        stmt.setInt(4, id);
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tren WHERE id_tren=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}