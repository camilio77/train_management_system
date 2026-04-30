package co.edu.upb.client.controller;

import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.LoginClientView;
import co.edu.upb.client.view.RegisterClientView;
import co.edu.upb.train_management_system.model.user.Passenger;

public class RegisterController {

    private final ClientModel      model;
    private final RegisterClientView view;

    public RegisterController(ClientModel model, RegisterClientView view) {
        this.model = model;
        this.view  = view;

        view.onRegister((id, names, lastNames, idType, address, password, confirm) ->
            handleRegister(id, names, lastNames, idType, address, password, confirm));

        view.onBack(() -> {
            view.close();
            LoginClientView loginView = new LoginClientView();
            new LoginController(model, loginView);
        });
    }

    private void handleRegister(String id, String names, String lastNames,
                                 String idType, String address,
                                 String password, String confirm) {
        if (id.isEmpty() || names.isEmpty() || lastNames.isEmpty()
                || address.isEmpty() || password.isEmpty()) {
            view.showError("Completa todos los campos.");
            return;
        }
        if (!password.equals(confirm)) {
            view.showError("Las contraseñas no coinciden.");
            return;
        }
        try {
            Passenger passenger = new Passenger(id, names, lastNames, idType, address, password);
            model.getUserService().registerPassenger(passenger);
            view.showSuccess("¡Cuenta creada exitosamente!");
            view.close();
            LoginClientView loginView = new LoginClientView();
            new LoginController(model, loginView);
        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
        }
    }
}