package co.edu.upb.train_management_system.model.station;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class StationService extends UnicastRemoteObject implements StationInterface {
    private static StationService instance;

    protected StationService() throws RemoteException { super(); }

    public static StationService getInstance() throws RemoteException {
        if (instance == null) instance = new StationService();
        return instance;
    }

    @Override
    public LinkedList<Station> getAll() throws SQLException {
        LinkedList<Station> list = new LinkedList<>();
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery("SELECT * FROM estacion");
        while (rs.next())
            list.add(new Station(String.valueOf(rs.getInt("id_estacion")), rs.getString("nombre")));
        return list;
    }

    @Override
    public void create(String nombre) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("INSERT INTO estacion (nombre) VALUES (?)");
        stmt.setString(1, nombre);
        stmt.executeUpdate();
    }

    @Override
    public void update(int id, String nombre) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("UPDATE estacion SET nombre=? WHERE id_estacion=?");
        stmt.setString(1, nombre); stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("DELETE FROM estacion WHERE id_estacion=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}