package co.edu.upb.train_management_system.model.station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public interface StationInterface extends Remote {
    LinkedList<Station> getAll() throws RemoteException, SQLException;

    void create(String nombre) throws RemoteException, SQLException;

    void update(int id, String nombre) throws RemoteException, SQLException;

    void delete(int id) throws RemoteException, SQLException;
}
