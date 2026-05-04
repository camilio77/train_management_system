package co.edu.upb.train_management_system.model.ticket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import co.edu.upb.train_management_system.model.luggage.Luggage;
import co.edu.upb.train_management_system.model.route.Route;
import co.edu.upb.train_management_system.model.station.StationGraphService;
import co.edu.upb.train_management_system.model.wagon.LuggageWagon;
import co.edu.upb.train_management_system.model.wagon.Wagon;

public class TicketService extends UnicastRemoteObject implements TicketInterface {

  private static final int PRECIO_PREMIUM = 150000;
  private static final int PRECIO_EJECUTIVA = 80000;
  private static final int PRECIO_ESTANDAR = 40000;
  private static final int CAP_PREMIUM = 4;
  private static final int CAP_EJECUTIVA = 8;
  private static final int CAP_ESTANDAR = 22;

  public TicketService() throws RemoteException {
    super();
  }

  @Override
  public LinkedList<Ticket> buyTickets(String idUsuario, String estacionOrigen,
      String estacionDestino, String categoria,
      double pesoEquipaje) throws RemoteException {
    try {
      LinkedList<String> camino = findPathByRoutes(estacionOrigen, estacionDestino);

      if (camino.isEmpty())
        throw new RemoteException("No existe camino entre " +
            estacionOrigen + " y " + estacionDestino);

      String[] estaciones = camino.toArray(new String[0]);
      if (estaciones.length < 2)
        throw new RemoteException("El origen y destino son la misma estación.");

      List<Integer> idRutas = new ArrayList<>();
      for (int i = 0; i < estaciones.length - 1; i++) {
        int idRuta = findRouteBetween(estaciones[i], estaciones[i + 1]);
        if (idRuta < 0)
          throw new RemoteException(
              "No hay ruta disponible entre " + estaciones[i] +
                  " y " + estaciones[i + 1]);
        idRutas.add(idRuta);
      }

      int precioPorTramo = switch (categoria.toUpperCase()) {
        case "PREMIUM" -> PRECIO_PREMIUM;
        case "EJECUTIVA" -> PRECIO_EJECUTIVA;
        default -> PRECIO_ESTANDAR;
      };

      LinkedList<Ticket> tickets = new LinkedList<>();
      Connection conn = DatabaseConnection.getConnection();
      conn.setAutoCommit(false);

      try {
        boolean primerTramo = true;
        ensureGuestUser(conn, idUsuario, idUsuario);
        for (int idRuta : idRutas) {
          int idVagon = findAvailableWagon(conn, idRuta);
          if (idVagon < 0) {
            conn.rollback();
            throw new RemoteException(
                "No hay vagón disponible en ruta " + idRuta);
          }

          int asiento = assignSeat(conn, idVagon, categoria);
          if (asiento < 0) {
            conn.rollback();
            throw new RemoteException(
                "No hay asientos en zona " + categoria +
                    " en ruta " + idRuta);
          }

          PreparedStatement ps = conn.prepareStatement(
              """
                  INSERT INTO tiquete
                    (id_usuario, id_ruta, id_vagon, numero_asiento,
                     categoria, valor_pagado, estado)
                  VALUES (?, ?, ?, ?, ?, ?, true)
                  """,
              Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, idUsuario);
          ps.setInt(2, idRuta);
          ps.setInt(3, idVagon);
          ps.setInt(4, asiento);
          ps.setString(5, categoria.toUpperCase());
          ps.setInt(6, precioPorTramo);
          ps.executeUpdate();

          ResultSet keys = ps.getGeneratedKeys();
          keys.next();
          int idTicket = keys.getInt(1);

          if (primerTramo && pesoEquipaje > 0) {
            int idVagonEq = findLuggageWagon(conn, idRuta);
            PreparedStatement psEq = conn.prepareStatement(
                "INSERT INTO equipaje (id_tiquete, id_vagon, peso) VALUES (?, ?, ?)");
            psEq.setInt(1, idTicket);
            if (idVagonEq > 0)
              psEq.setInt(2, idVagonEq);
            else
              psEq.setNull(2, Types.INTEGER);
            psEq.setDouble(3, pesoEquipaje);
            psEq.executeUpdate();
            primerTramo = false;
          }

          tickets.add(buildTicket(conn, idTicket));
        }
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

      return tickets;

    } catch (RemoteException e) {
      throw e;
    } catch (Exception e) {
      throw new RemoteException("Error en compra: " + e.getMessage());
    }
  }

  @Override
  public LinkedList<Ticket> buyTicketsAsGuest(String idUsuario, String nombreCompleto,
      String estacionOrigen, String estacionDestino,
      String categoria, double pesoEquipaje) throws RemoteException {
    try {
      Connection connCheck = DatabaseConnection.getConnection();
      ensureGuestUser(connCheck, idUsuario, nombreCompleto);
    } catch (SQLException e) {
      throw new RemoteException("Error registrando usuario invitado: " + e.getMessage());
    }
    return buyTickets(idUsuario, estacionOrigen, estacionDestino, categoria, pesoEquipaje);
  }

  @Override
  public LinkedList<Ticket> getTicketsByPassenger(String idUsuario) throws RemoteException {
    try {
      LinkedList<Ticket> list = new LinkedList<>();
      Connection conn = DatabaseConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT id_tiquete FROM tiquete WHERE id_usuario=? ORDER BY fecha_compra DESC");
      stmt.setString(1, idUsuario);
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
        list.add(buildTicket(conn, rs.getInt(1)));
      return list;
    } catch (Exception e) {
      throw new RemoteException("Error obteniendo tickets: " + e.getMessage());
    }
  }

  @Override
  public boolean cancelTicket(int idTicket) throws RemoteException {
    try {
      PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
          "UPDATE tiquete SET estado=false WHERE id_tiquete=?");
      stmt.setInt(1, idTicket);
      stmt.executeUpdate();
      return true;
    } catch (Exception e) {
      throw new RemoteException("Error cancelando ticket: " + e.getMessage());
    }
  }

  private int findRouteBetween(String nombreOrigen, String nombreDestino)
      throws SQLException {
    PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("""
        SELECT r.id_ruta FROM ruta r
        JOIN ruta_estacion re1 ON re1.id_ruta = r.id_ruta
        JOIN ruta_estacion re2 ON re2.id_ruta = r.id_ruta
        JOIN estacion e1 ON e1.id_estacion = re1.id_estacion
        JOIN estacion e2 ON e2.id_estacion = re2.id_estacion
        WHERE e1.nombre = ? AND e2.nombre = ?
          AND re1.orden < re2.orden
        LIMIT 1
        """);
    stmt.setString(1, nombreOrigen);
    stmt.setString(2, nombreDestino);
    ResultSet rs = stmt.executeQuery();
    return rs.next() ? rs.getInt("id_ruta") : -1;
  }

  private int findAvailableWagon(Connection conn, int idRuta) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement("""
        SELECT v.id_vagon FROM vagon v
        JOIN ruta r ON r.id_tren = v.id_tren
        WHERE r.id_ruta = ? AND v.tipo = 'PASAJEROS'
        LIMIT 1
        """);
    stmt.setInt(1, idRuta);
    ResultSet rs = stmt.executeQuery();
    return rs.next() ? rs.getInt("id_vagon") : -1;
  }

  private int findLuggageWagon(Connection conn, int idRuta) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement("""
        SELECT v.id_vagon FROM vagon v
        JOIN ruta r ON r.id_tren = v.id_tren
        WHERE r.id_ruta = ? AND v.tipo = 'EQUIPAJE'
        LIMIT 1
        """);
    stmt.setInt(1, idRuta);
    ResultSet rs = stmt.executeQuery();
    return rs.next() ? rs.getInt("id_vagon") : -1;
  }

  private int assignSeat(Connection conn, int idVagon, String categoria)
      throws SQLException {
    int inicio, fin;
    switch (categoria.toUpperCase()) {
      case "PREMIUM" -> {
        inicio = 1;
        fin = CAP_PREMIUM;
      }
      case "EJECUTIVA" -> {
        inicio = CAP_PREMIUM + 1;
        fin = CAP_PREMIUM + CAP_EJECUTIVA;
      }
      default -> {
        inicio = CAP_PREMIUM + CAP_EJECUTIVA + 1;
        fin = CAP_PREMIUM + CAP_EJECUTIVA + CAP_ESTANDAR;
      }
    }
    PreparedStatement stmt = conn.prepareStatement("""
        SELECT numero_asiento FROM tiquete
        WHERE id_vagon=? AND numero_asiento BETWEEN ? AND ? AND estado=true
        ORDER BY numero_asiento
        """);
    stmt.setInt(1, idVagon);
    stmt.setInt(2, inicio);
    stmt.setInt(3, fin);
    ResultSet rs = stmt.executeQuery();
    Set<Integer> ocupados = new HashSet<>();
    while (rs.next())
      ocupados.add(rs.getInt(1));
    for (int a = inicio; a <= fin; a++)
      if (!ocupados.contains(a))
        return a;
    return -1;
  }

  private Ticket buildTicket(Connection conn, int idTicket) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement("""
        SELECT t.*, r.fecha_salida, r.fecha_llegada,
               eo.nombre AS origen_nombre, ed.nombre AS destino_nombre,
               tr.nombre AS tren_nombre
        FROM tiquete t
        JOIN ruta r ON t.id_ruta = r.id_ruta
        JOIN tren tr ON r.id_tren = tr.id_tren
        LEFT JOIN (
            SELECT re.id_ruta, e.nombre
            FROM ruta_estacion re JOIN estacion e ON re.id_estacion = e.id_estacion
            WHERE re.orden = (SELECT MIN(orden) FROM ruta_estacion WHERE id_ruta = re.id_ruta)
        ) eo ON eo.id_ruta = r.id_ruta
        LEFT JOIN (
            SELECT re.id_ruta, e.nombre
            FROM ruta_estacion re JOIN estacion e ON re.id_estacion = e.id_estacion
            WHERE re.orden = (SELECT MAX(orden) FROM ruta_estacion WHERE id_ruta = re.id_ruta)
        ) ed ON ed.id_ruta = r.id_ruta
        WHERE t.id_tiquete = ?
        """);
    stmt.setInt(1, idTicket);
    ResultSet rs = stmt.executeQuery();
    if (!rs.next())
      return null;

    Wagon wagon = new Wagon(String.valueOf(rs.getInt("id_vagon")));
    Ticket ticket = new Ticket(
        rs.getInt("id_tiquete"),
        null,
        rs.getString("categoria"),
        wagon,
        rs.getTimestamp("fecha_compra"),
        rs.getInt("valor_pagado"));
    ticket.setStatus(rs.getBoolean("estado"));
    ticket.setNumeroAsiento(rs.getInt("numero_asiento"));

    Route route = new Route(
        String.valueOf(rs.getInt("id_ruta")),
        rs.getTimestamp("fecha_salida"),
        rs.getTimestamp("fecha_llegada"));
    route.setOrigin(new co.edu.upb.train_management_system.model.station.Station(
        "", rs.getString("origen_nombre") != null ? rs.getString("origen_nombre") : ""));
    route.setDestination(new co.edu.upb.train_management_system.model.station.Station(
        "", rs.getString("destino_nombre") != null ? rs.getString("destino_nombre") : ""));
    ticket.addRoute(route);

    PreparedStatement psEq = conn.prepareStatement(
        "SELECT id_equipaje, peso, id_vagon FROM equipaje WHERE id_tiquete=?");
    psEq.setInt(1, idTicket);
    ResultSet rsEq = psEq.executeQuery();
    while (rsEq.next()) {
      Luggage luggage = new Luggage(
          String.valueOf(rsEq.getInt("id_equipaje")),
          rsEq.getDouble("peso"));
      if (rsEq.getObject("id_vagon") != null) {
        luggage.setWagon(new LuggageWagon(
            String.valueOf(rsEq.getInt("id_vagon"))));
      }
      ticket.addLuggage(luggage);
    }

    return ticket;
  }

  @Override
  public LinkedList<Ticket> getTicketsByRoute(String idRuta) throws RemoteException {
    try {
      LinkedList<Ticket> list = new LinkedList<>();
      Connection conn = DatabaseConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT id_tiquete FROM tiquete WHERE id_ruta = ? ORDER BY fecha_compra DESC");
      stmt.setInt(1, Integer.parseInt(idRuta));
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
        list.add(buildTicket(conn, rs.getInt(1)));
      return list;
    } catch (Exception e) {
      throw new RemoteException("Error obteniendo tickets por ruta: " + e.getMessage());
    }
  }

  @Override
  public boolean validateTicket(int idTicket, int idRuta) throws RemoteException {
    try {
      Connection conn = DatabaseConnection.getConnection();
      PreparedStatement stmt = conn.prepareStatement(
          """
              SELECT 1 FROM tiquete
              WHERE id_tiquete = ?
                AND id_ruta    = ?
                AND estado     = true
              """);
      stmt.setInt(1, idTicket);
      stmt.setInt(2, idRuta);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (Exception e) {
      throw new RemoteException("Error validando ticket: " + e.getMessage());
    }
  }

  @Override
  public LinkedList<Ticket> getBoardingOrder(String idRuta) throws RemoteException {
    try {
      Connection conn = DatabaseConnection.getConnection();

      PreparedStatement stmt = conn.prepareStatement("""
          SELECT id_tiquete FROM tiquete
          WHERE id_ruta = ? AND estado = true
          ORDER BY
              CASE categoria
                  WHEN 'PREMIUM'   THEN 1
                  WHEN 'EJECUTIVA' THEN 2
                  ELSE                  3
              END,
              numero_asiento ASC
          """);
      stmt.setInt(1, Integer.parseInt(idRuta));
      ResultSet rs = stmt.executeQuery();

      LinkedList<Ticket> list = new LinkedList<>();
      while (rs.next())
        list.add(buildTicket(conn, rs.getInt(1)));
      return list;

    } catch (Exception e) {
      throw new RemoteException("Error obteniendo orden de abordaje: " + e.getMessage());
    }
  }

  private void ensureGuestUser(Connection conn, String idUsuario, String nombreCompleto)
      throws SQLException {
    PreparedStatement check = conn.prepareStatement(
        "SELECT 1 FROM usuario WHERE identificacion = ?");
    check.setString(1, idUsuario);
    ResultSet rs = check.executeQuery();
    if (rs.next())
      return;

    String[] partes = nombreCompleto.trim().split(" ", 2);
    String nombres = partes[0];
    String apellidos = partes.length > 1 ? partes[1] : "-";

    PreparedStatement insert = conn.prepareStatement("""
        INSERT INTO usuario
          (identificacion, nombres, apellidos, contrasena,
           tipo, tipo_identificacion)
        VALUES (?, ?, ?, 'INVITADO', 'PASAJERO', 'CC')
        """);
    insert.setString(1, idUsuario);
    insert.setString(2, nombres);
    insert.setString(3, apellidos);
    insert.executeUpdate();
  }

  @Override
  public LinkedList<String> findPathByRoutes(String origen, String destino)
          throws RemoteException {
    try {
      // Construir un grafo temporal solo con los tramos que tienen rutas registradas
      co.edu.upb.app.GraphPrototipe.MatrixGraph<String> routeGraph =
              new co.edu.upb.app.GraphPrototipe.MatrixGraph<>(50);

      Connection conn = DatabaseConnection.getConnection();

      // Agregar todas las estaciones como vértices
      ResultSet rsEst = conn.createStatement()
              .executeQuery("SELECT nombre FROM estacion ORDER BY id_estacion");
      while (rsEst.next())
        routeGraph.nuevoVertice(rsEst.getString("nombre"));

      // Agregar aristas SOLO donde hay rutas registradas
      ResultSet rsRutas = conn.createStatement().executeQuery("""
            SELECT e1.nombre AS origen, e2.nombre AS destino,
                   COALESCE(ce.distancia_km, 1) AS km
            FROM ruta r
            JOIN ruta_estacion re1 ON re1.id_ruta = r.id_ruta
            JOIN ruta_estacion re2 ON re2.id_ruta = r.id_ruta
            JOIN estacion e1 ON e1.id_estacion = re1.id_estacion
            JOIN estacion e2 ON e2.id_estacion = re2.id_estacion
            LEFT JOIN conexion_estacion ce
                ON ce.id_estacion_origen  = re1.id_estacion
               AND ce.id_estacion_destino = re2.id_estacion
            WHERE re1.orden < re2.orden
            GROUP BY e1.nombre, e2.nombre, ce.distancia_km
            """);
      while (rsRutas.next()) {
        try {
          routeGraph.newEdge(
                  rsRutas.getString("origen"),
                  rsRutas.getString("destino"),
                  rsRutas.getInt("km"));
        } catch (Exception ignored) {}
      }

      // Dijkstra sobre el grafo de rutas disponibles
      LinkedList<String> path = routeGraph.dijkstra(origen, destino);
      return path;

    } catch (Exception e) {
      throw new RemoteException("Error buscando camino: " + e.getMessage());
    }
  }
}