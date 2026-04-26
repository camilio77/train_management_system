package co.edu.upb.train_management_system.model.station;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationService {
    private static StationService instance;
    private StationService() {}
    public static StationService getInstance() {
        if (instance == null) instance = new StationService();
        return instance;
    }

    public List<Station> getAll() throws SQLException {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM estacion";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            list.add(new Station(String.valueOf(rs.getInt("id_estacion")), rs.getString("nombre")));
        }
        return list;
    }

    public void create(String nombre) throws SQLException {
        String sql = "INSERT INTO estacion (nombre) VALUES (?)";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.executeUpdate();
    }

    public void update(int id, String nombre) throws SQLException {
        String sql = "UPDATE estacion SET nombre=? WHERE id_estacion=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM estacion WHERE id_estacion=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}