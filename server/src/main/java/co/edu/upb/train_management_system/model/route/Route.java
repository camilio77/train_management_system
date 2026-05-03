package co.edu.upb.train_management_system.model.route;

import java.io.Serializable;
import java.util.Date;

import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.train.Train;

public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date dateOfLeaving;
    private Date dateOfArrival;
    private Train train;
    private Station origin;
    private Station destination;

    public Route(String id, Date dateOfLeaving, Date dateOfArrival) {
        this.id = id;
        this.dateOfLeaving = dateOfLeaving;
        this.dateOfArrival = dateOfArrival;
    }

    public Route(String id, Date dateOfLeaving, Date dateOfArrival,
            Train train, Station origin, Station destination) {
        this.id = id;
        this.dateOfLeaving = dateOfLeaving;
        this.dateOfArrival = dateOfArrival;
        this.train = train;
        this.origin = origin;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public Date getDateOfLeaving() {
        return dateOfLeaving;
    }

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public Train getTrain() {
        return train;
    }

    public Station getOriginStation() {
        return origin;
    }

    public Station getDestinationStation() {
        return destination;
    }

    public void setDateOfLeaving(Date d) {
        this.dateOfLeaving = d;
    }

    public void setDateOfArrival(Date d) {
        this.dateOfArrival = d;
    }

    public void setTrain(Train t) {
        this.train = t;
    }

    public void setOrigin(Station s) {
        this.origin = s;
    }

    public void setDestination(Station s) {
        this.destination = s;
    }

    public String getTrainId() {
        return train.getId();
    }

    public String getTrainName() {
        return train != null ? train.getName() : "Sin tren";
    }

    public String getOriginName() {
        return origin != null ? origin.getName() : "—";
    }

    public String getDestinationName() {
        return destination != null ? destination.getName() : "—";
    }

    public int calculateDistance() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}