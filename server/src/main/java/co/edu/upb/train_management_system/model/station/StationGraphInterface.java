package co.edu.upb.train_management_system.model.station;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StationGraphInterface extends Remote {
    void loadGraph()                                                         throws RemoteException;
    LinkedList<String> shortestPath(String origen, String destino)           throws RemoteException;
    int shortestDistance(String origen, String destino)                      throws RemoteException;
    void addConnection(String idOrigen, String idDestino, double km)         throws RemoteException;
    void deleteConnection(String idOrigen, String idDestino)                 throws RemoteException;
    LinkedList<String> getAllStationNames()                                   throws RemoteException;
    LinkedList<String[]> getAllConnections()                                  throws RemoteException; // ← nuevo
    LinkedList<Station> getAllStations()                                      throws RemoteException; // ← nuevo
}