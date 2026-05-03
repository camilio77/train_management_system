package co.edu.upb.ticketMachine.controller;

import co.edu.upb.ticketMachine.model.TicketMachineModel;
import co.edu.upb.ticketMachine.view.TicketMachineView;
import co.edu.upb.ticketMachine.view.UserDataView;

public class UserDataController {

    private final TicketMachineModel model;
    private final UserDataView       view;

    public UserDataController(TicketMachineModel model, UserDataView view) {
        this.model = model;
        this.view  = view;
        view.onContinuar(this::handleContinuar);
    }

    public void init() {}

    private void handleContinuar() {
        String id     = view.getId();
        String nombre = view.getNombre();

        if (id.isEmpty()) {
            view.showError("Ingresa tu número de identificación.");
            return;
        }
        if (nombre.isEmpty()) {
            view.showError("Ingresa tu nombre completo.");
            return;
        }
        if (!id.matches("\\d+")) {
            view.showError("El ID debe contener solo números.");
            return;
        }

        view.close();
        TicketMachineView machineView = new TicketMachineView(nombre);
        TicketMachineController ctrl  =
                new TicketMachineController(model, machineView, id, nombre);
        ctrl.init();
    }
}