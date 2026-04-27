package co.edu.upb.train_management_system.model.wagon;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WagonService {
    private static WagonService instance;
    private WagonService() {}
    public static WagonService getInstance() {
        if (instance == null) instance = new WagonService();
        return instance;
    }

    public List<Wagon> getByTrain(int idTren) throws SQLException {
        List<Wagon> list = new ArrayList<>();
        String sql = "SELECT * FROM vagon WHERE id_tren=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, idTren);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        }
        return list;
    }

    public List<Wagon> getAll() throws SQLException {
        List<Wagon> list = new ArrayList<>();
        String sql = "SELECT * FROM vagon";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        }
        return list;
    }

    public void create(int idTren, String tipo, int capacidad) throws SQLException {
        String sql = "INSERT INTO vagon (id_tren, tipo, capacidad) VALUES (?, ?, ?)";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, idTren);
        stmt.setString(2, tipo);
        if(tipo.equals("PASAJEROS")){
            stmt.setInt(3, 32);
        } else {
            stmt.setInt(3, 64);
        }
        stmt.executeUpdate();
    }

    public void update(int id, String tipo, int capacidad) throws SQLException {
        String sql = "UPDATE vagon SET tipo=?, capacidad=? WHERE id_vagon=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setString(1, tipo);
        stmt.setInt(2, capacidad);
        stmt.setInt(3, id);
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vagon WHERE id_vagon=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}