package co.edu.upb.train_management_system.model.ticket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public class TicketService extends UnicastRemoteObject implements TicketInterface {
  private int index = 0;
  private LinkedList<Ticket> tickets = new LinkedList<>();

  public TicketService() throws RemoteException {
    super();
  }

  @Override
  public Ticket register(Ticket ticket) throws RemoteException {
    Ticket newTicket = new Ticket(index, ticket.getPassenger(), ticket.getCategory(), ticket.getWagon(), ticket.getDateOfPurchase(), ticket.getTotal());
    tickets.add(newTicket);
    index++;
    return newTicket;
  }

  @Override
  public boolean validate(Ticket ticket) throws RemoteException {
    return tickets.contains(ticket);
  }
}