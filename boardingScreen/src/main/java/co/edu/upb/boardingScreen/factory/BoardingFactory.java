package co.edu.upb.boardingScreen.factory;

import co.edu.upb.boardingScreen.controller.BoardingLoginController;
import co.edu.upb.boardingScreen.model.BoardingModel;
import co.edu.upb.boardingScreen.view.BoardingLoginView;
import co.edu.upb.train_management_system.enviroment.Environment;

public class BoardingFactory {

    private BoardingFactory() {}

    public static BoardingLoginController create() {
        Environment env = Environment.getInstance();
        if (env == null) {
            throw new IllegalStateException("Failed to load Environment");
        }

        BoardingModel model = new BoardingModel(
                env.getIp(), env.getPort(), env.getServiceName());

        if (!model.connect()) {
            throw new IllegalStateException("No se pudo conectar al servidor RMI.");
        }

        BoardingLoginView loginView = new BoardingLoginView();
        return new BoardingLoginController(model, loginView);
    }
}