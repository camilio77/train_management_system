package co.edu.upb.train_management_system;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginControler {

    @FXML
    TextField usuarioTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void login(ActionEvent event) throws IOException{
        String username = usuarioTextField.getText();

        FXMLLoader fxmlLoader =  new FXMLLoader(App.class.getResource("passengerMenu.fxml"));
        root = fxmlLoader.load();

        PassengerMenuController passengerMenuController = fxmlLoader.getController();
        passengerMenuController.displayName(username);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Sistema de gestion de trenes");
        stage.setScene(scene);
        stage.show();
    }

}