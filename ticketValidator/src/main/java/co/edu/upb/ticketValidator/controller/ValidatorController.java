package co.edu.upb.ticketValidator.controller;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.ticketValidator.model.ValidatorModel;
import co.edu.upb.ticketValidator.view.LoginClientView;
import co.edu.upb.ticketValidator.view.ValidatorView;
import co.edu.upb.train_management_system.model.ticket.Ticket;
import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;

public class ValidatorController {

    private final ValidatorModel model;
    private final ValidatorView view;
    private final AbstractUserWithPower user;

    public ValidatorController(ValidatorModel model, ValidatorView view,
            AbstractUserWithPower user) {
        this.model = model;
        this.view = view;
        this.user = user;
    }

    public void init() {
        loadRoutes();
        bindEvents();
    }

    private void bindEvents() {
        view.onLoadRoutes(this::loadTicketsForSelectedRoute);
        view.onValidate(this::handleValidate);
        view.onLogout(() -> {
            view.close();
            LoginClientView loginView = new LoginClientView();
            new LoginController(model, loginView);
        });
    }

    private void loadRoutes() {
        try {
            view.clearRoutes();
            model.getRouteService().getAll().forEach(r -> {
                String label = r.getOriginName() + " → " + r.getDestinationName()
                        + " | " + r.getTrainName()
                        + " | " + (r.getDateOfLeaving() != null
                                ? r.getDateOfLeaving().toString()
                                : "—");
                view.addRouteOption(r.getId(), label);
                return null;
            });
            loadTicketsForSelectedRoute();
        } catch (Exception ex) {
            view.showError("Error cargando rutas: " + ex.getMessage());
        }
    }

    private void loadTicketsForSelectedRoute() {
        String idRuta = view.getSelectedRouteId();
        if (idRuta == null)
            return;

        view.clearTickets();
        try {
            LinkedList<Ticket> tickets = model.getTicketService().getTicketsByRoute(idRuta);

            if (tickets.isEmpty()) {
                view.showValidResult(false,
                        "No hay tickets registrados para esta ruta.");
                return;
            }

            tickets.forEach(t -> {
                String pasajero = t.getPassenger() != null
                        ? t.getPassenger().getFullName()
                        : "ID: " + t.getId();
                String estado = t.isStatus() ? "✔ Activo" : "✖ Cancelado";
                view.addTicketRow(
                        String.valueOf(t.getId()),
                        pasajero,
                        t.getCategory(),
                        String.valueOf(t.getNumeroAsiento()),
                        estado);
                return null;
            });

        } catch (Exception ex) {
            view.showError("Error cargando tickets: " + ex.getMessage());
        }
    }

    private void handleValidate() {
        String idRuta = view.getSelectedRouteId();
        String idTicket = view.getTicketId();

        if (idRuta == null) {
            view.showValidResult(false, "Selecciona una ruta primero.");
            return;
        }
        if (idTicket == null || idTicket.isEmpty()) {
            view.showValidResult(false, "Ingresa el ID del ticket.");
            return;
        }

        try {
            int idT = Integer.parseInt(idTicket);
            int idR = Integer.parseInt(idRuta);
            boolean valid = model.getTicketService().validateTicket(idT, idR);

            if (valid) {
                view.showValidResult(true,
                        "Ticket #" + idTicket + " VÁLIDO para esta ruta.");
            } else {
                view.showValidResult(false,
                        "Ticket #" + idTicket + " NO válido o no pertenece a esta ruta.");
            }
        } catch (NumberFormatException ex) {
            view.showValidResult(false, "El ID del ticket debe ser un número.");
        } catch (Exception ex) {
            view.showError("Error validando: " + ex.getMessage());
        }
    }
}