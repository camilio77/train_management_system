package co.edu.upb.boardingScreen.controller;

import co.edu.upb.boardingScreen.model.BoardingModel;
import co.edu.upb.boardingScreen.view.BoardingLoginView;
import co.edu.upb.boardingScreen.view.BoardingView;
import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;
import co.edu.upb.train_management_system.model.user.Admin;
import co.edu.upb.train_management_system.model.user.Employee;

public class BoardingLoginController {

    private final BoardingModel     model;
    private final BoardingLoginView view;

    public BoardingLoginController(BoardingModel model, BoardingLoginView view) {
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
            AbstractUserWithPower user = model.getUserService().login(id, password);

            if (user == null) {
                view.showError("Credenciales incorrectas o sin permisos.");
                return;
            }
            if (!(user instanceof Admin) && !(user instanceof Employee)) {
                view.showError("Solo administradores y empleados pueden acceder.");
                return;
            }

            view.close();
            BoardingView boardingView = new BoardingView(user.getFullName());
            BoardingController ctrl = new BoardingController(model, boardingView, user);
            ctrl.init();

        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}