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

    public boolean setDateOfLeaving(Date dateOfLeaving) {
        this.dateOfLeaving = dateOfLeaving;
        return true;
    }
    

    public int calculateDistance(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
     
}
