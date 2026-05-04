package co.edu.upb.train_management_system.model.ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public interface TicketInterface extends Remote {
    LinkedList<Ticket> buyTickets(String idUsuario, String estacionOrigen,
            String estacionDestino, String categoria,
            double pesoEquipaje) throws RemoteException;

    LinkedList<Ticket> getTicketsByPassenger(String idUsuario) throws RemoteException;

    boolean cancelTicket(int idTicket) throws RemoteException;

    LinkedList<Ticket> getTicketsByRoute(String idRuta) throws RemoteException;

    boolean validateTicket(int idTicket, int idRuta) throws RemoteException;

    LinkedList<Ticket> getBoardingOrder(String idRuta) throws RemoteException;

    LinkedList<Ticket> buyTicketsAsGuest(String idUsuario, String nombreCompleto,
            String estacionOrigen, String estacionDestino,
            String categoria, double pesoEquipaje) throws RemoteException;

    LinkedList<String> findPathByRoutes(String origen, String destino) throws RemoteException;
}