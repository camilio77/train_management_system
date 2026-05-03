package co.edu.upb.ticketValidator;

import co.edu.upb.ticketValidator.factory.ValidationFactory;

public class App {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            ValidationFactory.create();
        } catch (Exception e) {
            System.err.println("Error al iniciar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
