package co.edu.upb.train_management_system.controller;

import java.sql.SQLException;
import java.util.function.UnaryOperator;

import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import co.edu.upb.train_management_system.model.ServerModel;
import co.edu.upb.train_management_system.view.ServerView;

public class ServerController {

    ServerModel model;
    ServerView view;

    public ServerController(ServerModel model, ServerView view) {
        this.model = model;
        this.view = view;
    }

    public void init() {
        if (model.deploy()) {
            view.initComponents(startServer(), stopServer());
        } else {
            view.setMessage("Failed to deploy the server.");
        }
    }

    private UnaryOperator<Void> startServer() {
        return event -> {
            try {
                DatabaseConnection.getConnection();
                view.startStatus("Server Running");
                view.setMessage("Database connected successfully.");
                view.showTestButton();
            } catch (SQLException e) {
                System.err.println("SQLState: " + e.getSQLState());
                view.setMessage("DB Error: " + e.getMessage());
            }
            return null;
        };
    }

    private UnaryOperator<Void> stopServer() {
        return event -> {
            DatabaseConnection.closeConnection();
            model.stop();
            view.stopStatus("Start Server");
            view.setMessage("Server stopped.");
            return null;
        };
    }
}