package co.edu.upb.ticketMachine.model;

import java.rmi.Naming;

import co.edu.upb.train_management_system.model.route.RouteInterface;
import co.edu.upb.train_management_system.model.station.StationGraphInterface;
import co.edu.upb.train_management_system.model.station.StationInterface;
import co.edu.upb.train_management_system.model.ticket.TicketInterface;

public class TicketMachineModel {

    private final String ticketUri;
    private final String routeUri;
    private final String stationUri;
    private final String stationGraphUri;

    private TicketInterface       ticketService;
    private RouteInterface        routeService;
    private StationInterface      stationService;
    private StationGraphInterface stationGraphService;

    public TicketMachineModel(String ip, int port, String serviceName) {
        String base          = "rmi://" + ip + ":" + port + "/" + serviceName;
        this.ticketUri       = base;
        this.routeUri        = base + "-routes";
        this.stationUri      = base + "-stations";
        this.stationGraphUri = base + "-station-graph";
    }

    public boolean connect() {
        try {
            ticketService       = (TicketInterface)       Naming.lookup(ticketUri);
            routeService        = (RouteInterface)        Naming.lookup(routeUri);
            stationService      = (StationInterface)      Naming.lookup(stationUri);
            stationGraphService = (StationGraphInterface) Naming.lookup(stationGraphUri);
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    public TicketInterface       getTicketService()       { return ticketService; }
    public RouteInterface        getRouteService()        { return routeService; }
    public StationInterface      getStationService()      { return stationService; }
    public StationGraphInterface getStationGraphService() { return stationGraphService; }
    public boolean               isConnected()            { return ticketService != null; }
}