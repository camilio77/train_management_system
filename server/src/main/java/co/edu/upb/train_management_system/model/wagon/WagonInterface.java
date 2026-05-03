package co.edu.upb.train_management_system.model.wagon;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface WagonInterface extends Remote {
    LinkedList<Wagon> getAll() throws RemoteException, SQLException;

    LinkedList<Wagon> getByTrain(int idTren) throws RemoteException, SQLException;

    void create(int idTren, String tipo, int capacidad) throws RemoteException, SQLException;

    void update(int id, String tipo, int capacidad) throws RemoteException, SQLException;

    void delete(int id) throws RemoteException, SQLException;
}