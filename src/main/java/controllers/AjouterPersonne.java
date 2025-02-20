package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Personne;
import services.PersonneService;

public class AjouterPersonne {

    @FXML
    private TextField ahmed;

    @FXML
    private Button btnajout;

    @FXML
    private TextField hmed;

    @FXML
    void AjouterPersonne(ActionEvent event) {
        if(ahmed.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Yhedik");
            alert.show();
        }
        PersonneService ps = new PersonneService();
        ps.ajouter(new Personne(ahmed.getText(), hmed.getText()));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.show();

    }

}
