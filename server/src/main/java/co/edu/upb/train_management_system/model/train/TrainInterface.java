package co.edu.upb.train_management_system.model.train;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public interface TrainInterface extends Remote {
    LinkedList<Train> getAll() throws RemoteException, SQLException;

    void create(String nombre, String tipo) throws RemoteException, SQLException;

    void update(int id, String nombre, String tipo, int kilometraje) throws RemoteException, SQLException;

    void delete(int id) throws RemoteException, SQLException;
}