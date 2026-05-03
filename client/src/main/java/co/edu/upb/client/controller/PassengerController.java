package co.edu.upb.client.controller;

import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.BuyTicketView;
import co.edu.upb.client.view.LoginClientView;
import co.edu.upb.client.view.PassengerPanelView;
import co.edu.upb.train_management_system.model.user.Passenger;

public class PassengerController {

    private final ClientModel        model;
    private final PassengerPanelView view;
    private final Passenger          passenger;

    public PassengerController(ClientModel model, PassengerPanelView view, Passenger passenger) {
        this.model     = model;
        this.view      = view;
        this.passenger = passenger;

        reloadRoutes();

        view.onLogout(() -> {
            view.close();
            LoginClientView loginView = new LoginClientView();
            new LoginController(model, loginView);
        });

        view.onBuyTicket(() -> {
            BuyTicketView buyView = new BuyTicketView();
            new BuyTicketController(model, buyView, passenger);
        });
    }

    public void reloadRoutes() {
        try {
            view.clearRoutes();
            model.getRouteService().getAll().forEach(r -> {
                view.addRoute(
                    r.getId(),
                    r.getTrainName(),
                    r.getOriginName(),
                    r.getDestinationName(),
                    r.getDateOfLeaving() != null ? r.getDateOfLeaving().toString() : "—",
                    r.getDateOfArrival()  != null ? r.getDateOfArrival().toString()  : "—"
                );
                return null;
            });
        } catch (Exception ex) {
            view.showError("Error al cargar rutas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    
}