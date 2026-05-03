package co.edu.upb.ticketMachine;

import co.edu.upb.ticketMachine.controller.UserDataController;
import co.edu.upb.ticketMachine.factory.TicketMachineFactory;

public class App {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            UserDataController ctrl = TicketMachineFactory.create();
            ctrl.init();
        } catch (Exception e) {
            System.err.println("Error al iniciar la máquina de tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
