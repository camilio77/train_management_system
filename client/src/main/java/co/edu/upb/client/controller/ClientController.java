package co.edu.upb.client.controller;

import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.ClientView;
import co.edu.upb.train_management_system.model.observer.Observer;

public class ClientController extends Observer {

    private final ClientModel model;
    private final ClientView  view;

    public ClientController(ClientModel model, ClientView view) {
        super(model);          // se suscribe al Subject (ClientModel)
        this.model = model;
        this.view  = view;
    }

    public void init() {
        view.initComponents(
            // onConnect
            v -> {
                boolean ok = model.connect();
                if (ok) view.onConnected();
                else    view.onConnectionFailed();
                return null;
            },
            // onDisconnect
            v -> {
                model.disconnect();
                view.onDisconnected();
                return null;
            }
        );
    }

    // El observer se activa cada vez que ClientModel llama notifyObservers()
    @Override
    public void update() {
        view.setLog(model.getLogger());
    }

    public ClientModel getModel() { return model; }
    public ClientView  getView()  { return view; }
}