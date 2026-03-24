package co.edu.upb.train_management_system.controller;

import java.util.function.UnaryOperator;

import co.edu.upb.train_management_system.model.ServerModel;
import co.edu.upb.train_management_system.view.ServerView;

public class ServerController {

  ServerModel model;
  ServerView view;

  public ServerController(ServerModel model, ServerView view) {
    this.model = model;
    this.view = view;
  }

  public void init() {
    if (model.deploy()) {
      view.initComponents(new UnaryOperator<Void>() {
          @Override
          public Void apply(Void event) {
              view.startStatus("Server is already");
              return null;
          }
      });
    } else {
      view.setMessage("Failed to deploy the server.");
    }
  }

}