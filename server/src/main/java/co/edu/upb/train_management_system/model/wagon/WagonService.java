package co.edu.upb.train_management_system.model.wagon;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class WagonService extends UnicastRemoteObject implements WagonInterface {
    private static WagonService instance;

    protected WagonService() throws RemoteException {
        super();
    }

    public static WagonService getInstance() throws RemoteException {
        if (instance == null)
            instance = new WagonService();
        return instance;
    }

    @Override
    public LinkedList<Wagon> getByTrain(int idTren) throws SQLException {
        LinkedList<Wagon> list = new LinkedList<>();
        PreparedStatement stmt = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM vagon WHERE id_tren=?");
        stmt.setInt(1, idTren);
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        return list;
    }

    @Override
    public LinkedList<Wagon> getAll() throws SQLException {
        LinkedList<Wagon> list = new LinkedList<>();
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery("SELECT * FROM vagon");
        while (rs.next())
            list.add(new Wagon(String.valueOf(rs.getInt("id_vagon"))));
        return list;
    }

    @Override
    public void create(int idTren, String tipo, int capacidad) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection()
                .prepareStatement("INSERT INTO vagon (id_tren, tipo, capacidad) VALUES (?, ?, ?)");
        stmt.setInt(1, idTren);
        stmt.setString(2, tipo);
        stmt.setInt(3, tipo.equals("PASAJEROS") ? 32 : 64);
        stmt.executeUpdate();
    }

    @Override
    public void update(int id, String tipo, int capacidad) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection()
                .prepareStatement("UPDATE vagon SET tipo=?, capacidad=? WHERE id_vagon=?");
        stmt.setString(1, tipo);
        stmt.setInt(2, capacidad);
        stmt.setInt(3, id);
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM vagon WHERE id_vagon=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}