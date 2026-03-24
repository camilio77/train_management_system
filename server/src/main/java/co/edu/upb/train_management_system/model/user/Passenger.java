package co.edu.upb.train_management_system.model.user;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.model.ticket.Ticket;

import java.io.Serializable;

public class Passenger extends AbstractUser implements Serializable {
    private String address;
    private LinkedList<Ticket> previousTickets;
    private Ticket currentTicket;
    private LinkedList<EmergencyContact> emergencyContacts;

    public Passenger(int identificacion, String names, String lastNames, String identificationType, String address) {
        super(identificacion, names, lastNames, identificationType);
        this.address = address;
        this.previousTickets = new LinkedList<>();
        this.currentTicket = null;
        this.emergencyContacts = new LinkedList<>();
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        this.address = address;
        return true;
    }

    public Ticket[] getPreviousTickets() {
        return previousTickets.toArray();
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public boolean setCurrentTicket(Ticket currentTicket) {
        this.currentTicket = currentTicket;
        return true;
    }

    public EmergencyContact[] getEmergencyContacts() {
        return emergencyContacts.toArray();
    }

    public boolean addEmergencyContact(EmergencyContact emergencyContact){
        return emergencyContacts.add(emergencyContact);
    }

    public boolean deleteEmergencyContact(int idContact){
        emergencyContacts.forEach(e -> {
            if (e.getIdentificacion() == idContact){
                emergencyContacts.remove(e);
            }
            return null;
        });
        return true;
    }

    public boolean modifyEmergencyContact(int idContact, EmergencyContact newContact){
        emergencyContacts.forEach(e -> {
            if (e.getIdentificacion() == idContact){
                emergencyContacts.replace(e, newContact, t -> t.getIdentificacion() == idContact);
            }
            return null;
        });
        return true;
    }

    public boolean addCurrentTicketToPrevious(){
        previousTickets.add(currentTicket);
        currentTicket = null;
        return true;
    }
}
