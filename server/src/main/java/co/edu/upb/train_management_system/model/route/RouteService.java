package co.edu.upb.train_management_system.model.route;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.train.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteService {
    private static RouteService instance;

    private RouteService() {}

    public static RouteService getInstance() {
        if (instance == null) instance = new RouteService();
        return instance;
    }

    public List<Route> getAll() throws SQLException {
        List<Route> list = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        String sql = """
            SELECT r.id_ruta, r.fecha_salida, r.fecha_llegada,
                   t.id_tren, t.nombre AS tren_nombre, t.tipo AS tren_tipo, t.kilometraje
            FROM ruta r
            JOIN tren t ON r.id_tren = t.id_tren
            ORDER BY r.id_ruta
            """;

        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            Train train = new Train(
                String.valueOf(rs.getInt("id_tren")),   // int → String
                rs.getString("tren_nombre")
            );
            train.setType(rs.getString("tren_tipo"));   // era "tren_tipo" literal — bug corregido
            train.setMileage(rs.getInt("kilometraje"));

            Route route = new Route(
                String.valueOf(rs.getInt("id_ruta")),   // int → String
                rs.getTimestamp("fecha_salida"),
                rs.getTimestamp("fecha_llegada"),
                train, null, null
            );
            list.add(route);
        }

        for (Route route : list) {
            String stationSql = """
                SELECT e.id_estacion, e.nombre, re.orden
                FROM ruta_estacion re
                JOIN estacion e ON re.id_estacion = e.id_estacion
                WHERE re.id_ruta = ?
                ORDER BY re.orden
                """;

            PreparedStatement ps = conn.prepareStatement(stationSql);
            ps.setInt(1, Integer.parseInt(route.getId()));  // String → int para el prepared statement
            ResultSet stRs = ps.executeQuery();

            Station first = null, last = null;
            while (stRs.next()) {
                Station s = new Station(
                    String.valueOf(stRs.getInt("id_estacion")),  // int → String
                    stRs.getString("nombre")
                );
                if (first == null) first = s;
                last = s;
            }
            route.setOrigin(first);
            route.setDestination(last);
        }

        return list;
    }

    public void create(String idTren, String idOrigen, String idDestino,
                       Timestamp salida, Timestamp llegada) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            String sql = "INSERT INTO ruta (id_tren, fecha_salida, fecha_llegada) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, Integer.parseInt(idTren));       // String → int
            stmt.setTimestamp(2, salida);
            stmt.setTimestamp(3, llegada);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            int idRuta = keys.getInt(1);

            String stSql = "INSERT INTO ruta_estacion (id_ruta, id_estacion, orden) VALUES (?, ?, ?)";
            PreparedStatement stStmt = conn.prepareStatement(stSql);

            stStmt.setInt(1, idRuta);
            stStmt.setInt(2, Integer.parseInt(idOrigen));   // String → int
            stStmt.setInt(3, 1);
            stStmt.executeUpdate();

            stStmt.setInt(1, idRuta);
            stStmt.setInt(2, Integer.parseInt(idDestino));  // String → int
            stStmt.setInt(3, 2);
            stStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void update(String id, Timestamp salida, Timestamp llegada) throws SQLException {
        String sql = "UPDATE ruta SET fecha_salida=?, fecha_llegada=? WHERE id_ruta=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setTimestamp(1, salida);
        stmt.setTimestamp(2, llegada);
        stmt.setInt(3, Integer.parseInt(id));               // String → int
        stmt.executeUpdate();
    }

    public void delete(String id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement stStmt = conn.prepareStatement(
                "DELETE FROM ruta_estacion WHERE id_ruta=?");
            stStmt.setInt(1, Integer.parseInt(id));         // String → int
            stStmt.executeUpdate();

            PreparedStatement rStmt = conn.prepareStatement(
                "DELETE FROM ruta WHERE id_ruta=?");
            rStmt.setInt(1, Integer.parseInt(id));          // String → int
            rStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}