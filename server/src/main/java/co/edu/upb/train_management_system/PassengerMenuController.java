package co.edu.upb.train_management_system;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PassengerMenuController {
    @FXML
    Label nameLabel;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void displayName(String username){
        nameLabel.setText("Bienvenido " + username + " al sistema de gestion de transporte");
    }

    public void logout(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader =  new FXMLLoader(App.class.getResource("login.fxml"));
        root = fxmlLoader.load();

        LoginControler loginControler = fxmlLoader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Sistema de gestion de trenes");
        stage.setScene(scene);
        stage.show();
    }
}
