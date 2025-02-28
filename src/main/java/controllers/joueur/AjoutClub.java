package controllers.joueur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.Club;
import services.joueur.ClubService;

import java.io.File;
import java.io.IOException;

public class AjoutClub {
    @FXML private Button homeButton;
    @FXML private TextField nomField;
    @FXML private TextField photoField;
    @FXML private Button selectPhotoButton;
    @FXML private Button ajouterButton;
    @FXML private Button annulerButton;

    private ClubService clubService = new ClubService();

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", homeButton);
    }

    @FXML
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de club");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(selectPhotoButton.getScene().getWindow());
        if (file != null) {
            photoField.setText(file.toURI().toString());
        }
    }

    @FXML
    private void ajouter() {
        String nom = nomField.getText().trim();
        String photoUrl = photoField.getText().trim();

        if (nom.isEmpty()) {
            showAlert("Erreur", "Nom requis", "Le nom du club ne peut pas être vide.");
            return;
        }

        Club club = new Club(nom, photoUrl.isEmpty() ? null : photoUrl);
        clubService.ajouter(club);
        handleAnnulerButton();
    }

    @FXML
    private void handleAnnulerButton() {
        loadScene("/joueur/DisplayClub.fxml", annulerButton);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", "Détails : " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}