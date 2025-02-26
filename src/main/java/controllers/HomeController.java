/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author oXCToo
 */
public class HomeController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private VBox pnl_scroll;

    @FXML
    private Button joueurButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) joueurButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
}
