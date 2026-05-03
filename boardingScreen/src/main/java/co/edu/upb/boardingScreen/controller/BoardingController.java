package co.edu.upb.boardingScreen.controller;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.boardingScreen.model.BoardingModel;
import co.edu.upb.boardingScreen.view.BoardingLoginView;
import co.edu.upb.boardingScreen.view.BoardingView;
import co.edu.upb.train_management_system.model.ticket.Ticket;
import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;

public class BoardingController {

    private final BoardingModel         model;
    private final BoardingView          view;
    private final AbstractUserWithPower user;

    public BoardingController(BoardingModel model, BoardingView view,
                              AbstractUserWithPower user) {
        this.model = model;
        this.view  = view;
        this.user  = user;
    }

    public void init() {
        loadRoutes();
        bindEvents();
    }

    private void bindEvents() {
        view.onLoad(this::loadBoardingOrder);
        view.onLogout(() -> {
            view.close();
            BoardingLoginView loginView = new BoardingLoginView();
            new BoardingLoginController(model, loginView);
        });
    }

    private void loadRoutes() {
        try {
            view.clearRoutes();
            model.getRouteService().getAll().forEach(r -> {
                String label = r.getOriginName() + " → " + r.getDestinationName()
                        + " | " + r.getTrainName()
                        + " | " + (r.getDateOfLeaving() != null
                                ? r.getDateOfLeaving().toString() : "—");
                view.addRouteOption(r.getId(), label);
                return null;
            });
        } catch (Exception ex) {
            view.showError("Error cargando rutas: " + ex.getMessage());
        }
    }

    private void loadBoardingOrder() {
        String idRuta = view.getSelectedRouteId();
        if (idRuta == null) return;

        view.clearTable();
        try {
            LinkedList<Ticket> tickets =
                    model.getTicketService().getBoardingOrder(idRuta);

            if (tickets.isEmpty()) {
                view.setInfo("No hay pasajeros activos en esta ruta.");
                return;
            }

            int[] orden = {1};
            tickets.forEach(t -> {
                String pasajero = t.getPassenger() != null
                        ? t.getPassenger().getFullName()
                        : "Pasajero #" + t.getId();
                String vagon = t.getWagon() != null
                        ? "Vagón " + t.getWagon().getId()
                        : "—";
                view.addBoardingRow(
                        orden[0]++,
                        String.valueOf(t.getId()),
                        pasajero,
                        t.getCategory(),
                        vagon,
                        String.valueOf(t.getNumeroAsiento()));
                return null;
            });

            view.setInfo("Total pasajeros: " + (orden[0] - 1)
                    + "  |  Orden: PREMIUM → EJECUTIVA → ESTÁNDAR");

        } catch (Exception ex) {
            view.showError("Error cargando orden de abordaje: " + ex.getMessage());
        }
    }
}