package co.edu.upb.train_management_system;

import co.edu.upb.train_management_system.controller.ServerController;
import co.edu.upb.train_management_system.factory.ServerFactory;

public class App {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            ServerController server = ServerFactory.create();
            server.init();
        } catch (Exception e) {
            System.err.println("Failed to start the server application: " + e.getMessage());
        }
    }
}