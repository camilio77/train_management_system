package co.edu.upb.client.factory;

import co.edu.upb.client.controller.ClientController;
import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.ClientView;
import co.edu.upb.train_management_system.enviroment.Environment;

public class ClientFactory {

    private ClientFactory() {}

    public static ClientController create() {
        Environment env = Environment.getInstance();
        if (env == null) throw new IllegalStateException("Failed to load Environment");

        ClientModel model = new ClientModel(env.getIp(), env.getPort(), env.getServiceName());
        ClientView  view  = new ClientView("Train Management — Cliente");

        return new ClientController(model, view);
    }
}