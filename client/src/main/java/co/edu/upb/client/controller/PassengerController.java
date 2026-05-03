package co.edu.upb.client.controller;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.BuyTicketView;
import co.edu.upb.client.view.LoginClientView;
import co.edu.upb.client.view.PassengerPanelView;
import co.edu.upb.train_management_system.model.ticket.Ticket;
import co.edu.upb.train_management_system.model.user.Passenger;

public class PassengerController {

    private final ClientModel model;
    private final PassengerPanelView view;
    private Passenger passenger;

    public PassengerController(ClientModel model, PassengerPanelView view, Passenger passenger) {
        this.model = model;
        this.view = view;
        this.passenger = passenger;

        reloadRoutes();
        reloadTickets();

        view.onLogout(() -> {
            view.close();
            new LoginController(model, new LoginClientView());
        });

        view.onRefresh(() -> {
            reloadRoutes();
            reloadTickets();
        });

        view.onBuyTicket(() -> {
            BuyTicketView buyView = new BuyTicketView();
            new BuyTicketController(model, buyView, passenger);
        });

        view.onEditProfile(() -> handleEditProfile());

        view.onViewTickets(() -> reloadTickets());
    }

    public void reloadRoutes() {
        try {
            view.clearRoutes();
            model.getRouteService().getAll().forEach(r -> {
                view.addRoute(
                        r.getId(), r.getTrainName(),
                        r.getOriginName(), r.getDestinationName(),
                        r.getDateOfLeaving() != null ? r.getDateOfLeaving().toString() : "—",
                        r.getDateOfArrival() != null ? r.getDateOfArrival().toString() : "—"
                );
                return null;
            });
        } catch (Exception ex) {
            view.showError("Error al cargar rutas: " + ex.getMessage());
        }
    }

    public void reloadTickets() {
        try {
            view.clearTickets();
            LinkedList<Ticket> tickets = model.getTicketService()
                    .getTicketsByPassenger(passenger.getIdentificacion());

            boolean[] tieneActivo = {false};
            tickets.forEach(t -> {
                if (t.isStatus()) {
                    tieneActivo[0] = true;
                }
                view.addTicket(
                        String.valueOf(t.getId()),
                        t.getRoutes().length > 0 ? t.getRoutes()[0].getOriginName() : "—",
                        t.getRoutes().length > 0 ? t.getRoutes()[0].getDestinationName() : "—",
                        t.getCategory(),
                        "$" + String.format("%,d", (long) t.getTotal()).replace(",", "."),
                        String.valueOf(t.getNumeroAsiento()),
                        t.isStatus() ? "✅ Activo" : "❌ Cancelado"
                );
                return null;
            });
            view.setActiveTicketBanner(tieneActivo[0]);
        } catch (Exception ex) {
            view.showError("Error al cargar tickets: " + ex.getMessage());
        }
    }

    private void handleEditProfile() {
        PassengerPanelView.ProfileData data = view.showEditProfileDialog(
                passenger.getFullName().split(" ", 2)[0],
                passenger.getFullName().split(" ", 2).length > 1
                ? passenger.getFullName().split(" ", 2)[1] : "",
                passenger.getIdentificationType(),
                passenger.getAddress()
        );
        if (data == null) {
            return;
        }

        try {
            model.getUserService().updatePassenger(
                    passenger.getIdentificacion(),
                    data.nombres, data.apellidos,
                    data.tipoId, data.direccion
            );
            passenger = new Passenger(
                    passenger.getIdentificacion(),
                    data.nombres, data.apellidos,
                    data.tipoId, data.direccion,
                    passenger.getPassword()
            );
            view.updateName(data.nombres + " " + data.apellidos);
            view.showSuccess("Perfil actualizado correctamente.");
        } catch (Exception ex) {
            view.showError("Error al actualizar perfil: " + ex.getMessage());
        }
    }
}
