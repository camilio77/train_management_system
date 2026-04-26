package co.edu.upb.train_management_system.model.route;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
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
        String sql = "SELECT * FROM ruta";
        ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            Route r = new Route(
                    rs.getInt("id_ruta"),
                    rs.getTimestamp("fecha_salida"),
                    rs.getTimestamp("fecha_llegada")
            );
            r.setIdTren(rs.getInt("id_tren"));
            list.add(r);
        }
        return list;
    }

    public void create(int idTren, Timestamp salida, Timestamp llegada) throws SQLException {
        String sql = "INSERT INTO ruta (id_tren, fecha_salida, fecha_llegada) VALUES (?, ?, ?)";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, idTren);
        stmt.setTimestamp(2, salida);
        stmt.setTimestamp(3, llegada);
        stmt.executeUpdate();
    }

    public void update(int id, Timestamp salida, Timestamp llegada) throws SQLException {
        String sql = "UPDATE ruta SET fecha_salida=?, fecha_llegada=? WHERE id_ruta=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setTimestamp(1, salida);
        stmt.setTimestamp(2, llegada);
        stmt.setInt(3, id);
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ruta WHERE id_ruta=?";
        PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}