package co.edu.upb.ticketValidator.factory;

import co.edu.upb.ticketValidator.controller.LoginController;
import co.edu.upb.ticketValidator.controller.ValidatorController;
import co.edu.upb.ticketValidator.model.ValidatorModel;
import co.edu.upb.ticketValidator.view.LoginClientView;
import co.edu.upb.train_management_system.enviroment.Environment;

public class ValidationFactory {

    private ValidationFactory() {
    }

    public static ValidatorController create() {
        Environment env = Environment.getInstance();
        if (env == null) {
            throw new IllegalStateException("Failed to load Environment");
        }

        ValidatorModel model = new ValidatorModel(env.getIp(), env.getPort(), env.getServiceName());

        if (!model.connect()) {
            throw new IllegalStateException("No se pudo conectar al servidor RMI.");
        }

        // Arrancar siempre por el login, no por el ValidatorView
        LoginClientView loginView = new LoginClientView();
        LoginController loginCtrl = new LoginController(model, loginView);

        // Retornar null porque el flujo continúa desde el LoginController
        return null;
    }
}
