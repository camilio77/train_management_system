package co.edu.upb.train_management_system.model.wagon;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;

import java.sql.*;

public class WagonService {
    private static WagonService instance;
    private WagonService() {}
    public static WagonService getInstance() {
        if (instance == null) instance = new WagonService();
        return instance;
    }

    public LinkedList<Wagon> getByTrain(int idTren) throws SQLException {
        LinkedList<Wagon> list = new LinkedList<>();
        String sql = "SELECT * FROM vagon WHERE id_tren=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, idTren);
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        return list;
    }

    public LinkedList<Wagon> getAll() throws SQLException {
        LinkedList<Wagon> list = new LinkedList<>();
        String sql = "SELECT * FROM vagon";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next())
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        return list;
    }

    public void create(int idTren, String tipo, int capacidad) throws SQLException {
        String sql = "INSERT INTO vagon (id_tren, tipo, capacidad) VALUES (?, ?, ?)";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, idTren);
        stmt.setString(2, tipo);
        stmt.setInt(3, tipo.equals("PASAJEROS") ? 32 : 64);
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