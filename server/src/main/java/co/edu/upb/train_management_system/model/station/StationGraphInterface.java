package co.edu.upb.train_management_system.model.station;

import java.rmi.Remote;
import java.rmi.RemoteException;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public interface StationGraphInterface extends Remote {
    void loadGraph() throws RemoteException;

    LinkedList<String> shortestPath(String origen, String destino) throws RemoteException;

    int shortestDistance(String origen, String destino) throws RemoteException;

    void addConnection(String idOrigen, String idDestino, double km) throws RemoteException;

    void deleteConnection(String idOrigen, String idDestino) throws RemoteException;

    LinkedList<String> getAllStationNames() throws RemoteException;

    LinkedList<String[]> getAllConnections() throws RemoteException;

    LinkedList<Station> getAllStations() throws RemoteException;
}