package co.edu.upb.train_management_system.model.ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketInterface extends Remote {
  Ticket register(Ticket ticket) throws RemoteException;

  boolean validate(Ticket ticket) throws RemoteException;
}