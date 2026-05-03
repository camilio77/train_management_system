package co.edu.upb.train_management_system.model.station;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.edu.upb.app.GraphPrototipe.MatrixGraph;
import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;

public class StationGraphService extends UnicastRemoteObject implements StationGraphInterface {
    private static StationGraphService instance;
    private MatrixGraph<String> graph;

    protected StationGraphService() throws RemoteException {
        super();
        this.graph = new MatrixGraph<>(50);
    }

    public static StationGraphService getInstance() throws RemoteException {
        if (instance == null)
            instance = new StationGraphService();
        return instance;
    }

    @Override
    public void loadGraph() throws RemoteException {
        try {
            graph = new MatrixGraph<>(50);
            Connection conn = DatabaseConnection.getConnection();

            ResultSet rsEstaciones = conn.createStatement()
                    .executeQuery("SELECT nombre FROM estacion ORDER BY id_estacion");
            while (rsEstaciones.next())
                graph.nuevoVertice(rsEstaciones.getString("nombre"));

            ResultSet rsConexiones = conn.createStatement().executeQuery("""
                    SELECT e1.nombre AS origen, e2.nombre AS destino, c.distancia_km
                    FROM conexion_estacion c
                    JOIN estacion e1 ON c.id_estacion_origen  = e1.id_estacion
                    JOIN estacion e2 ON c.id_estacion_destino = e2.id_estacion
                    """);
            while (rsConexiones.next()) {
                String origen = rsConexiones.getString("origen");
                String destino = rsConexiones.getString("destino");
                int km = rsConexiones.getInt("distancia_km");
                try {
                    graph.newEdge(origen, destino, km);
                } catch (Exception e) {
                    System.err.println("Error agregando arista: " + e.getMessage());
                }
            }
            System.out.println("Grafo cargado: " + graph.getNumVerts() + " estaciones.");
        } catch (SQLException e) {
            throw new RemoteException("Error cargando grafo: " + e.getMessage());
        }
    }

    @Override
    public LinkedList<String> shortestPath(String origen, String destino) throws RemoteException {
        try {
            return graph.dijkstra(origen, destino);
        } catch (Exception e) {
            throw new RemoteException("Error en Dijkstra: " + e.getMessage());
        }
    }

    @Override
    public int shortestDistance(String origen, String destino) throws RemoteException {
        try {
            LinkedList<String> path = graph.dijkstra(origen, destino);
            if (path.isEmpty())
                return -1;

            String[] nodos = (String[]) path.toArray();
            int total = 0;
            for (int i = 0; i < nodos.length - 1; i++)
                total += graph.getEdgeWeight(
                        graph.numVertice(nodos[i]),
                        graph.numVertice(nodos[i + 1]));
            return total;
        } catch (Exception e) {
            throw new RemoteException("Error calculando distancia: " + e.getMessage());
        }
    }

    @Override
    public void addConnection(String idOrigen, String idDestino, double distanciaKm)
            throws RemoteException {
        try {
            Connection conn = DatabaseConnection.getConnection();

            String nombreOrigen = getStationName(conn, idOrigen);
            String nombreDestino = getStationName(conn, idDestino);

            if (nombreOrigen == null || nombreDestino == null) {
                throw new RemoteException("Una de las estaciones no existe en la BD.");
            }

            if (graph.numVertice(nombreOrigen) < 0)
                graph.nuevoVertice(nombreOrigen);
            if (graph.numVertice(nombreDestino) < 0)
                graph.nuevoVertice(nombreDestino);

            PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO conexion_estacion (id_estacion_origen, id_estacion_destino, distancia_km)
                    VALUES (?, ?, ?)
                    ON CONFLICT (id_estacion_origen, id_estacion_destino) DO UPDATE
                    SET distancia_km = EXCLUDED.distancia_km
                    """);
            stmt.setInt(1, Integer.parseInt(idOrigen));
            stmt.setInt(2, Integer.parseInt(idDestino));
            stmt.setDouble(3, distanciaKm);
            stmt.executeUpdate();

            System.out.println("Vértices en grafo: " + graph.getNumVerts());
            System.out.println("Buscando origen: '" + nombreOrigen + "' → índice: " + graph.numVertice(nombreOrigen));
            System.out
                    .println("Buscando destino: '" + nombreDestino + "' → índice: " + graph.numVertice(nombreDestino));
            graph.newEdge(nombreOrigen, nombreDestino, (int) distanciaKm);

        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            throw new RemoteException("Error agregando conexión: " + e.getMessage());
        }
    }

    @Override
    public void deleteConnection(String idOrigen, String idDestino) throws RemoteException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("""
                    DELETE FROM conexion_estacion
                    WHERE id_estacion_origen=? AND id_estacion_destino=?
                    """);
            stmt.setInt(1, Integer.parseInt(idOrigen));
            stmt.setInt(2, Integer.parseInt(idDestino));
            stmt.executeUpdate();

            // Actualizar grafo en memoria
            String nombreOrigen = getStationName(conn, idOrigen);
            String nombreDestino = getStationName(conn, idDestino);
            graph.deleteEdge(nombreOrigen, nombreDestino);

        } catch (Exception e) {
            throw new RemoteException("Error eliminando conexión: " + e.getMessage());
        }
    }

    @Override
    public LinkedList<String> getAllStationNames() throws RemoteException {
        try {
            LinkedList<String> names = new LinkedList<>();
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                    .executeQuery("SELECT nombre FROM estacion ORDER BY nombre");
            while (rs.next())
                names.add(rs.getString("nombre"));
            return names;
        } catch (SQLException e) {
            throw new RemoteException("Error obteniendo estaciones: " + e.getMessage());
        }
    }

    private String getStationName(Connection conn, String id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT nombre FROM estacion WHERE id_estacion=?");
        ps.setInt(1, Integer.parseInt(id));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("nombre") : null;
    }

    @Override
    public LinkedList<String[]> getAllConnections() throws RemoteException {
        try {
            LinkedList<String[]> list = new LinkedList<>();
            String sql = """
                    SELECT e1.id_estacion AS id_origen,  e1.nombre AS nombre_origen,
                           e2.id_estacion AS id_destino, e2.nombre AS nombre_destino,
                           c.distancia_km
                    FROM conexion_estacion c
                    JOIN estacion e1 ON c.id_estacion_origen  = e1.id_estacion
                    JOIN estacion e2 ON c.id_estacion_destino = e2.id_estacion
                    ORDER BY e1.nombre, e2.nombre
                    """;
            ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new String[] {
                        rs.getString("id_origen"),
                        rs.getString("nombre_origen"),
                        rs.getString("id_destino"),
                        rs.getString("nombre_destino"),
                        rs.getString("distancia_km")
                });
            }
            return list;
        } catch (SQLException e) {
            throw new RemoteException("Error obteniendo conexiones: " + e.getMessage());
        }
    }

    @Override
    public LinkedList<Station> getAllStations() throws RemoteException {
        try {
            LinkedList<Station> list = new LinkedList<>();
            ResultSet rs = DatabaseConnection.getConnection().createStatement()
                    .executeQuery("SELECT id_estacion, nombre FROM estacion ORDER BY nombre");
            while (rs.next())
                list.add(new Station(
                        String.valueOf(rs.getInt("id_estacion")),
                        rs.getString("nombre")));
            return list;
        } catch (SQLException e) {
            throw new RemoteException("Error obteniendo estaciones: " + e.getMessage());
        }
    }

    public LinkedList<Station> getConnectedStations(String idOrigen) throws SQLException {
        LinkedList<Station> list = new LinkedList<>();
        String sql = """
                SELECT e.id_estacion, e.nombre
                FROM conexion_estacion ce
                JOIN estacion e ON ce.id_estacion_destino = e.id_estacion
                WHERE ce.id_estacion_origen = ?
                """;
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(idOrigen));
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
            list.add(new Station(String.valueOf(rs.getInt("id_estacion")), rs.getString("nombre")));
        return list;
    }
}