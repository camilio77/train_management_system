package co.edu.upb.client;

import co.edu.upb.client.controller.ClientController;
import co.edu.upb.client.factory.ClientFactory;

public class App {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            ClientController client = ClientFactory.create();
            client.init();
        } catch (Exception e) {
            System.err.println("Error al iniciar el cliente: " + e.getMessage());
        }
    }
}