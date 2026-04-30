package co.edu.upb.client.model;

import java.rmi.Naming;

import co.edu.upb.train_management_system.model.observer.Subject;
import co.edu.upb.train_management_system.model.route.RouteInterface;
import co.edu.upb.train_management_system.model.station.StationInterface;
import co.edu.upb.train_management_system.model.ticket.TicketInterface;
import co.edu.upb.train_management_system.model.train.TrainInterface;
import co.edu.upb.train_management_system.model.user.UserInterface;
import co.edu.upb.train_management_system.model.wagon.WagonInterface;

public class ClientModel extends Subject {

    private String logger;

    // URIs
    private final String ticketUri;
    private final String userUri;
    private final String trainUri;
    private final String stationUri;
    private final String routeUri;
    private final String wagonUri;

    // Servicios remotos
    private TicketInterface  ticketService;
    private UserInterface    userService;
    private TrainInterface   trainService;
    private StationInterface stationService;
    private RouteInterface   routeService;
    private WagonInterface   wagonService;

    public ClientModel(String ip, int port, String serviceName) {
        String base = "rmi://" + ip + ":" + port + "/" + serviceName;
        this.ticketUri  = base;
        this.userUri    = base + "-users";
        this.trainUri   = base + "-trains";
        this.stationUri = base + "-stations";
        this.routeUri   = base + "-routes";
        this.wagonUri   = base + "-wagons";
    }

    public boolean connect() {
        try {
            ticketService  = (TicketInterface)  Naming.lookup(ticketUri);
            userService    = (UserInterface)    Naming.lookup(userUri);
            trainService   = (TrainInterface)   Naming.lookup(trainUri);
            stationService = (StationInterface) Naming.lookup(stationUri);
            routeService   = (RouteInterface)   Naming.lookup(routeUri);
            wagonService   = (WagonInterface)   Naming.lookup(wagonUri);

            this.logger = "Conectado al servidor en: " + ticketUri;
            this.notifyObservers();
            return true;
        } catch (Exception e) {
            this.logger = "Error al conectar con el servidor: " + e.getMessage();
            this.notifyObservers();
            e.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() {
        ticketService  = null;
        userService    = null;
        trainService   = null;
        stationService = null;
        routeService   = null;
        wagonService   = null;
        this.logger = "Desconectado del servidor.";
        this.notifyObservers();
        return true;
    }

    public boolean isConnected() {
        return ticketService != null;
    }

    public TicketInterface  getTicketService()  { return ticketService; }
    public UserInterface    getUserService()    { return userService; }
    public TrainInterface   getTrainService()   { return trainService; }
    public StationInterface getStationService() { return stationService; }
    public RouteInterface   getRouteService()   { return routeService; }
    public WagonInterface   getWagonService()   { return wagonService; }
    public String           getLogger()         { return logger; }
}