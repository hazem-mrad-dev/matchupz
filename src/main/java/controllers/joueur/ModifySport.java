package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.joueur.Sport;  // Make sure to import the Sport model
import services.joueur.SportService;  // Make sure to import the SportService

import java.io.IOException;

public class ModifySport {

    @FXML
    private TextField nomSportField;

    @FXML
    private TextField descriptionSportField;

    @FXML
    private Button annulerButton;

    @FXML
    private Button sportButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button modifierButton;

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) homeButton.getScene().getWindow();

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

    @FXML
    private void handleSport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplaySport.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) sportButton.getScene().getWindow();

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

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplaySport.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) annulerButton.getScene().getWindow();

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

    private Sport sport;
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    private Sport sportToModify;

    public void setSportToModify(Sport sport) {
        this.sportToModify = sport;
        nomSportField.setText(sport.getNomSport());
        descriptionSportField.setText(sport.getDescription());
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML file: " + fxmlPath);
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomSportField.getText().trim().isEmpty() || descriptionSportField.getText().trim().isEmpty()) {  // Corrected here
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier le sport.");
            alert.showAndWait();
            return;
        }

        try {
            String nomSport = nomSportField.getText().trim();
            String description = descriptionSportField.getText().trim();  // Corrected here

            sportToModify.setNomSport(nomSport);
            sportToModify.setDescription(description);

            SportService sportService = new SportService();
            sportService.modifier(sportToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Modification réussie !");
            alert.setContentText("Les informations du sport ont été modifiées avec succès.");
            alert.showAndWait();

            // Automatically return to DisplaySport after adding
            loadScene("/joueur/DisplaySport.fxml", modifierButton);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
