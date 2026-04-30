package co.edu.upb.train_management_system.model.train;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class TrainService extends UnicastRemoteObject implements TrainInterface {
    private static TrainService instance;

    protected TrainService() throws RemoteException { super(); }

    public static TrainService getInstance() throws RemoteException {
        if (instance == null) instance = new TrainService();
        return instance;
    }

    @Override
    public LinkedList<Train> getAll() throws SQLException {
        LinkedList<Train> list = new LinkedList<>();
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery("SELECT * FROM tren");
        while (rs.next()) {
            Train t = new Train(String.valueOf(rs.getInt("id_tren")), rs.getString("nombre"));
            t.setType(rs.getString("tipo"));
            t.setMileage(rs.getInt("kilometraje"));
            list.add(t);
        }
        return list;
    }

    @Override
    public void create(String nombre, String tipo) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("INSERT INTO tren (nombre, tipo) VALUES (?, ?)");
        stmt.setString(1, nombre); stmt.setString(2, tipo);
        stmt.executeUpdate();
    }

    @Override
    public void update(int id, String nombre, String tipo, int kilometraje) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("UPDATE tren SET nombre=?, tipo=?, kilometraje=? WHERE id_tren=?");
        stmt.setString(1, nombre); stmt.setString(2, tipo);
        stmt.setInt(3, kilometraje); stmt.setInt(4, id);
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("DELETE FROM tren WHERE id_tren=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}