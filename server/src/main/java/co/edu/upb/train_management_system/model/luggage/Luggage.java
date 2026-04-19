package co.edu.upb.train_management_system.model.luggage;

import java.io.Serializable;

import co.edu.upb.train_management_system.model.ticket.Ticket;
import co.edu.upb.train_management_system.model.wagon.LuggageWagon;

public class Luggage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private double weight;
    private LuggageWagon wagon;
    private Ticket ticket;

    public Luggage(String id, double weight) {
        this.id = id;
        this.weight = weight;
        this.wagon = null;
        this.ticket = null;
    }

    public String getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public LuggageWagon getWagon() {
        return wagon;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setWagon(LuggageWagon wagon) {
        this.wagon = wagon;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setId(String id) {
        this.id = id;
    }

}
