package co.edu.upb.train_management_system.model;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import co.edu.upb.train_management_system.model.route.RouteService;
import co.edu.upb.train_management_system.model.station.StationGraphService;
import co.edu.upb.train_management_system.model.station.StationService;
import co.edu.upb.train_management_system.model.ticket.TicketService;
import co.edu.upb.train_management_system.model.train.TrainService;
import co.edu.upb.train_management_system.model.user.UserService;
import co.edu.upb.train_management_system.model.wagon.WagonService;

public class ServerModel {

  private final String ip;
  private final int port;
  private final String serviceName;

  private final String ticketUri;
  private final String userUri;
  private final String trainUri;
  private final String routeUri;
  private final String wagonUri;
  private final String stationUri;
  private final String stationGraphUri;

  private TicketService ticketService;
  private UserService userService;
  private TrainService trainService;
  private RouteService routeService;
  private WagonService wagonService;
  private StationService stationService;
  private StationGraphService stationGraphService;

  private Registry registry;

  public ServerModel(String ip, int port, String serviceName) {
    this.ip = ip;
    this.port = port;
    this.serviceName = serviceName;

    String base = "//" + ip + ":" + port + "/" + serviceName;
    this.ticketUri = base;
    this.userUri = base + "-users";
    this.trainUri = base + "-trains";
    this.routeUri = base + "-routes";
    this.wagonUri = base + "-wagons";
    this.stationUri = base + "-stations";
    this.stationGraphUri = base + "-station-graph";

    System.out.println("Base URI: " + base);
  }

  public boolean deploy() {
    try {
      System.setProperty("java.rmi.server.hostname", ip);

      ticketService = new TicketService();
      userService = UserService.getInstance();
      trainService = TrainService.getInstance();
      routeService = RouteService.getInstance();
      wagonService = WagonService.getInstance();
      stationService = StationService.getInstance();
      stationGraphService = StationGraphService.getInstance();

      stationGraphService.loadGraph();
      registry = LocateRegistry.createRegistry(port);

      Naming.rebind(ticketUri, ticketService);
      Naming.rebind(userUri, userService);
      Naming.rebind(trainUri, trainService);
      Naming.rebind(routeUri, routeService);
      Naming.rebind(wagonUri, wagonService);
      Naming.rebind(stationUri, stationService);
      Naming.rebind(stationGraphUri, stationGraphService);

      System.out.println("✔ Todos los servicios registrados en el puerto " + port);
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean stop() {
    try {
      Naming.unbind(ticketUri);
      Naming.unbind(userUri);
      Naming.unbind(trainUri);
      Naming.unbind(routeUri);
      Naming.unbind(wagonUri);
      Naming.unbind(stationUri);
      Naming.unbind(stationGraphUri);

      UnicastRemoteObject.unexportObject(ticketService, true);
      UnicastRemoteObject.unexportObject(userService, true);
      UnicastRemoteObject.unexportObject(trainService, true);
      UnicastRemoteObject.unexportObject(routeService, true);
      UnicastRemoteObject.unexportObject(wagonService, true);
      UnicastRemoteObject.unexportObject(stationService, true);
      UnicastRemoteObject.unexportObject(stationGraphService, true);

      UnicastRemoteObject.unexportObject(registry, true);

      System.out.println("Servidor detenido correctamente.");
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public String getServiceName() {
    return serviceName;
  }
}