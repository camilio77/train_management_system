package co.edu.upb.train_management_system.model.ticket;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketInterface extends Remote {
  LinkedList<Ticket> buyTickets(String idUsuario, String estacionOrigen,
                                String estacionDestino, String categoria,
                                double pesoEquipaje)              throws RemoteException;
  LinkedList<Ticket> getTicketsByPassenger(String idUsuario)        throws RemoteException;
  boolean cancelTicket(int idTicket)                                throws RemoteException;
}