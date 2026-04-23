package co.edu.upb.train_management_system.model.route;

import java.util.Date;

import co.edu.upb.app.Queue.dinamic.Queue;
import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.train.Train;

public class Route {
    private int id;
    private Date dateOfLeaving;
    private Date dateOfArrival;
    private Queue<Train> trains;
    private Station origin;
    private Station destination;

    public Route(int id, Date dateOfLeaving, Date dateOfArrival) {
        this.id = id;
        this.dateOfLeaving = dateOfLeaving;
        this.trains = new Queue<>();
    }

    public int getId() {
        return id;
    }

    public Date getDateOfLeaving() {
        return dateOfLeaving;
    }

    public Station getOriginStation() {
        return origin;
    }

    public Station getDestinationStation() {
        return destination;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public boolean setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
        return true;
    }

    public boolean setDateOfLeaving(Date dateOfLeaving) {
        this.dateOfLeaving = dateOfLeaving;
        return true;
    }
    

    public int calculateDistance(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean setTrains(Queue<Train> trains) {
        this.trains = trains;
        return true;
    }

    public boolean setOrigin(Station origin) {
        this.origin = origin;
        return true;
    }

    public boolean setDestination(Station destination) {
        this.destination = destination;
        return true;
    }

    public Train[] getTrains() {
        Train[] trainArray = new Train[trains.size()];
        trains.forEach(train -> {
            for (int i = 0; i < trainArray.length; i++) {
                if (trainArray[i] == null) {
                    trainArray[i] = train;
                    break;
                }
            }
            return null;
        });
        return trainArray;
    }
     
}
