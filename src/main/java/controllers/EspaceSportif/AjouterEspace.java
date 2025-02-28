package controllers.EspaceSportif;

import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AjouterEspace {

    @FXML
    private TextField nomField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<String> categorieField;

    @FXML
    private TextField capaciteField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private final EspaceSportifService espaceService = new EspaceSportifService();

    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        List<String> categories = espaceService.getCategories();
        if (categories == null || categories.isEmpty()) {
            categorieField.getItems().setAll("Terrain Foot", "Terrain Basket", "Salle Gym");
        } else {
            categorieField.getItems().setAll(categories);
        }
        categorieField.setPromptText("Sélectionner une catégorie"); // Ajout pour guider l'utilisateur
    }

    @FXML
    private void ajouterEspace(ActionEvent event) {
        String nom = nomField.getText().trim();
        String adresse = adresseField.getText().trim();
        String categorie = categorieField.getValue();
        String capaciteText = capaciteField.getText().trim();

        // Validation des champs
        if (nom.isEmpty() || adresse.isEmpty() || categorie == null || capaciteText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        float capacite;
        try {
            capacite = Float.parseFloat(capaciteText.replace(",", "."));
            if (capacite <= 0) throw new NumberFormatException("Capacité doit être positive");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre positif valide.");
            return;
        }

        try {
            // Création et ajout de l'espace sportif
            EspaceSportif espace = new EspaceSportif(nom, adresse, categorie, capacite);
            espaceService.ajouter(espace);

            // Récupération des coordonnées et du climat via les APIs
            double[] coords = espaceService.getCoordonnes(adresse);
            String messageSuccess;
            if (coords != null) {
                String climat = espaceService.getClimat(coords[0], coords[1]);
                messageSuccess = String.format(
                        "Espace ajouté avec succès !\nCoordonnées : %.4f, %.4f\nClimat : %s",
                        coords[0], coords[1], climat != null ? climat : "Non disponible"
                );
            } else {
                messageSuccess = "Espace ajouté avec succès !\nCoordonnées et climat non disponibles.";
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", messageSuccess);
            clearFields();
            goToAfficherEspace(event);
        } catch (IllegalArgumentException e) {
            // Capture l'exception de setNomEspace dans EspaceSportif
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        } catch (IOException e) {
            // Gestion des erreurs API
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Espace ajouté, mais erreur lors de la récupération des coordonnées/climat : " + e.getMessage());
            clearFields();
            goToAfficherEspace(event);
        }
    }

    @FXML
    private void goToAfficherEspace(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Espaces Sportifs");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AffichageEspace.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        nomField.clear();
        adresseField.clear();
        categorieField.setValue(null);
        capaciteField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
