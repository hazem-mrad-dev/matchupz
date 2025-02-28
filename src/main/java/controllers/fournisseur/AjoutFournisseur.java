package controllers.fournisseur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.logistics.Fournisseur;
import services.logistics.FournisseurService;

import java.io.IOException;

public class AjoutFournisseur {

    @FXML
    private TextField nomField, emailField, adresseField;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button btnAjout;

    @FXML
    private Button annulerButton;

    private FournisseurService fournisseurService = new FournisseurService();

    @FXML
    public void initialize() {
        comboBoxCategorie.getItems().addAll(
                "EQUIPEMENT_SPORTIF",
                "ACCESSOIRE_ENTRAINEMENT",
                "MATERIEL_JEU",
                "TENUE_SPORTIVE",
                "EQUIPEMENT_PROTECTION",
                "INFRASTRUCTURE"
        );
        comboBoxCategorie.setValue("MATERIEL_JEU");
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() || comboBoxCategorie.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        String nom = nomField.getText().trim();

        // Check if Fournisseur already exists
        if (fournisseurService.exists(nom)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Fournisseur déjà existant"
            );
            return;
        }

        Fournisseur newFournisseur = new Fournisseur();
        newFournisseur.setNom(nom);
        newFournisseur.setEmail(emailField.getText().trim());
        newFournisseur.setAdresse(adresseField.getText().trim());
        newFournisseur.setCategorie_produit(comboBoxCategorie.getValue());

        fournisseurService.ajouter(newFournisseur); // Assuming this method exists

        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Fournisseur ajouté avec succès !");
        clearFields();
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de DisplayFournisseur.fxml : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        adresseField.clear();
        comboBoxCategorie.setValue("MATERIEL_JEU");
    }
}