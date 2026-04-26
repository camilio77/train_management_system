package co.edu.upb.train_management_system.model;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import co.edu.upb.train_management_system.model.ticket.TicketInterface;
import co.edu.upb.train_management_system.model.ticket.TicketService;

public class ServerModel {

  private String ip;
  private int port;
  private String serviceName;
  private String uri;
  private Registry registry;      // ← guardar referencia al registry
  private TicketInterface service; // ← guardar referencia al servicio

  public ServerModel(String ip, int port, String serviceName) {
    this.ip = ip;
    this.port = port;
    this.serviceName = serviceName;
    this.uri = "//" + ip + ":" + port + "/" + this.serviceName;
    System.out.println("URI: " + this.uri);
  }

  public boolean deploy() {
    try {
      System.setProperty("java.rmi.server.hostname", ip);
      service = new TicketService();
      registry = LocateRegistry.createRegistry(port);
      Naming.rebind(uri, service);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean stop() {
    try {
      // Des-registra el servicio del registry
      Naming.unbind(uri);
      // Desexporta el objeto remoto
      UnicastRemoteObject.unexportObject(service, true);
      // Destruye el registry
      UnicastRemoteObject.unexportObject(registry, true);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}