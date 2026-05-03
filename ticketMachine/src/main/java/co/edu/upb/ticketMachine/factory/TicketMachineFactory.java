package co.edu.upb.ticketMachine.factory;

import co.edu.upb.ticketMachine.controller.UserDataController;
import co.edu.upb.ticketMachine.model.TicketMachineModel;
import co.edu.upb.ticketMachine.view.UserDataView;
import co.edu.upb.train_management_system.enviroment.Environment;

public class TicketMachineFactory {

    private TicketMachineFactory() {}

    public static UserDataController create() {
        Environment env = Environment.getInstance();
        if (env == null)
            throw new IllegalStateException("Failed to load Environment");

        TicketMachineModel model = new TicketMachineModel(
                env.getIp(), env.getPort(), env.getServiceName());

        if (!model.connect())
            throw new IllegalStateException("No se pudo conectar al servidor RMI.");

        UserDataView view = new UserDataView();
        return new UserDataController(model, view);
    }
}