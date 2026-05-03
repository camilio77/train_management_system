package co.edu.upb.boardingScreen.model;

import java.rmi.Naming;

import co.edu.upb.train_management_system.model.route.RouteInterface;
import co.edu.upb.train_management_system.model.ticket.TicketInterface;
import co.edu.upb.train_management_system.model.user.UserInterface;

public class BoardingModel {

    private final String ticketUri;
    private final String userUri;
    private final String routeUri;

    private TicketInterface ticketService;
    private UserInterface   userService;
    private RouteInterface  routeService;

    public BoardingModel(String ip, int port, String serviceName) {
        String base   = "rmi://" + ip + ":" + port + "/" + serviceName;
        this.ticketUri = base;
        this.userUri   = base + "-users";
        this.routeUri  = base + "-routes";
    }

    public boolean connect() {
        try {
            ticketService = (TicketInterface) Naming.lookup(ticketUri);
            userService   = (UserInterface)   Naming.lookup(userUri);
            routeService  = (RouteInterface)  Naming.lookup(routeUri);
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    public boolean isConnected()              { return ticketService != null; }
    public TicketInterface getTicketService() { return ticketService; }
    public UserInterface   getUserService()   { return userService; }
    public RouteInterface  getRouteService()  { return routeService; }
}