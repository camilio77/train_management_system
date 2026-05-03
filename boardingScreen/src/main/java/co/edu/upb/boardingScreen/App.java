package co.edu.upb.boardingScreen;

import co.edu.upb.boardingScreen.controller.BoardingLoginController;
import co.edu.upb.boardingScreen.factory.BoardingFactory;

public class App {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            BoardingFactory.create();
        } catch (Exception e) {
            System.err.println("Error al iniciar el cliente: " + e.getMessage());
        }
    }
}
