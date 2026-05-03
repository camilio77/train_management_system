package co.edu.upb.train_management_system.model.ticket;

import java.io.Serializable;
import java.util.Date;

import co.edu.upb.app.Array.Array;
import co.edu.upb.app.Queue.dinamic.Queue;
import co.edu.upb.train_management_system.model.luggage.Luggage;
import co.edu.upb.train_management_system.model.route.Route;
import co.edu.upb.train_management_system.model.user.Passenger;
import co.edu.upb.train_management_system.model.wagon.Wagon;

public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Passenger passenger;
    private String category;
    private Queue<Route> routes;
    private Wagon wagon;
    private int total;
    private boolean status;
    private Date dateOfPurchase;
    private Array<Luggage> luggages;
    private int numeroAsiento;

    public Ticket(int id, Passenger passenger, String category, Wagon wagon, Date dateOfPurchase, int total) {
        this.id = id;
        this.passenger = passenger;
        this.category = category;
        this.wagon = wagon;
        this.routes = new Queue<>();
        this.luggages = new Array<>(2);
        this.dateOfPurchase = dateOfPurchase;
        this.total = total;
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public boolean setCategory(String category) {
        this.category = category;
        return true;
    }

    public boolean setPassenger(Passenger passenger) {
        this.passenger = passenger;
        return true;
    }

    public boolean setWagon(Wagon wagon) {
        this.wagon = wagon;
        return true;
    }

    public boolean addRoute(Route route) {
        return routes.insert(route);
    }

    public boolean addLuggage(Luggage luggage) {
        return luggages.add(luggage);
    }

    public boolean removeLuggage(int luggageIndex) {
        return luggages.remove(luggageIndex);
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Route[] getRoutes() {
        Route[] routesArray = new Route[routes.size()];
        routes.forEach((route) -> {
            for (int i = 0; i < routesArray.length; i++) {
                if (routesArray[i] == null) {
                    routesArray[i] = route;
                    break;
                }
            }
            return null;
        });
        return routesArray;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public int getTotal() {
        return total;
    }

    public boolean isStatus() {
        return status;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public Luggage[] getLuggages() {
        Luggage[] luggageArray = new Luggage[luggages.size()];
        for (int i = 0; i < luggages.size(); i++) {
            luggageArray[i] = luggages.get(i);
        }
        return luggageArray;
    }

    public boolean setStatus(boolean status) {
        this.status = status;
        return true;
    }

    public boolean setTotal(int total) {
        this.total = total;
        return true;
    }

    public boolean setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
        return true;
    }

    public boolean setId(int id) {
        this.id = id;
        return true;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public boolean setNumeroAsiento(int n) {
        this.numeroAsiento = n;
        return true;
    }
}
