package co.edu.upb.train_management_system.model.wagon;

import java.io.Serializable;

import co.edu.upb.app.PriorityQueue.PriorityQueue;
import co.edu.upb.train_management_system.model.user.Passenger;

public class PassengerWagon extends Wagon implements Serializable {
    private static final long serialVersionUID = 1L;
    private PriorityQueue<Passenger> passengers;

    public PassengerWagon(String id) {
        super(id);
        this.passengers = new PriorityQueue<>(3);
    }

    public int getPassengerCount() {
        return passengers.size();
    }

    public boolean loadPassenger(Passenger passenger) {
       throw new IllegalStateException("Not done yet.");
    }

    public Passenger unloadPassenger() {
        return passengers.extract();
    }
}
