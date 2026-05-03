package co.edu.upb.client.controller;

import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.LoginClientView;
import co.edu.upb.client.view.PassengerPanelView;
import co.edu.upb.client.view.RegisterClientView;
import co.edu.upb.train_management_system.model.user.Passenger;

public class LoginController {

    private final ClientModel model;
    private final LoginClientView view;

    public LoginController(ClientModel model, LoginClientView view) {
        this.model = model;
        this.view = view;

        view.onLogin((id, password) -> handleLogin(id, password));
        view.onRegister(() -> openRegister());
    }

    private void handleLogin(String id, String password) {
        if (id.isEmpty() || password.isEmpty()) {
            view.showError("Completa todos los campos.");
            return;
        }
        try {
            Passenger passenger = findPassenger(id, password);

            if (passenger != null) {
                view.close();
                PassengerPanelView panelView = new PassengerPanelView(passenger.getFullName());
                PassengerController pc = new PassengerController(model, panelView, passenger);
                panelView.onRefresh(() -> pc.reloadRoutes());
            } else {
                view.showError("Credenciales incorrectas.");
            }
        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Passenger findPassenger(String id, String password) throws Exception {
        return model.getUserService().loginPassenger(id, password);
    }

    private void openRegister() {
        view.close();
        RegisterClientView registerView = new RegisterClientView();
        new RegisterController(model, registerView);
    }
}
