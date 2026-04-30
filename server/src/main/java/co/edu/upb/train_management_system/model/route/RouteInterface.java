package co.edu.upb.train_management_system.model.route;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface RouteInterface extends Remote {
    LinkedList<Route> getAll()                                                              throws RemoteException, SQLException;
    void create(String idTren, String idOrigen, String idDestino, Timestamp s, Timestamp l) throws RemoteException, SQLException;
    void update(String id, Timestamp salida, Timestamp llegada)                             throws RemoteException, SQLException;
    void delete(String id)                                                                  throws RemoteException, SQLException;
}