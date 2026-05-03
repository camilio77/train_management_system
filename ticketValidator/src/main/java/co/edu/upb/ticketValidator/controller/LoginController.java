package co.edu.upb.ticketValidator.controller;

import co.edu.upb.ticketValidator.model.ValidatorModel;
import co.edu.upb.ticketValidator.view.LoginClientView;
import co.edu.upb.ticketValidator.view.ValidatorView;
import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;
import co.edu.upb.train_management_system.model.user.Admin;
import co.edu.upb.train_management_system.model.user.Employee;

public class LoginController {

    private final ValidatorModel  model;
    private final LoginClientView view;

    public LoginController(ValidatorModel model, LoginClientView view) {
        this.model = model;
        this.view  = view;
        view.onLogin(this::handleLogin);
    }

    private void handleLogin() {
        String id       = view.getId();
        String password = view.getPassword();

        if (id.isEmpty() || password.isEmpty()) {
            view.showError("Completa todos los campos.");
            return;
        }

        try {
            // El servidor ya filtra: solo retorna Admin o Employee, null si es pasajero
            AbstractUserWithPower user = model.getUserService().login(id, password);

            if (user == null) {
                view.showError("Credenciales incorrectas o sin permisos.");
                return;
            }

            // Verificación de rol con instanceof (no necesita getRole())
            if (!(user instanceof Admin) && !(user instanceof Employee)) {
                view.showError("No tienes permisos para acceder al validador.");
                return;
            }

            view.close();
            ValidatorView validatorView = new ValidatorView(user.getFullName());
            ValidatorController ctrl = new ValidatorController(model, validatorView, user);
            ctrl.init();

        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}