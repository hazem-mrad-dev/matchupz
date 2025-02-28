package controllers.fournisseur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.logistics.Fournisseur;
import services.logistics.FournisseurService;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;

public class ModifyFournisseur {

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    @FXML
    private Button fournisseurButton;

    @FXML
    private Button Home;

    private Fournisseur fournisseurToModify;
    @FXML
    public void initialize() {
        System.out.println("✅ ModifyFournisseur : initialize() exécuté !");
        System.out.println("🔍 comboBoxCategorie = " + comboBoxCategorie);
    }
    public void setFournisseurToModify(Fournisseur fournisseur) {
        if (comboBoxCategorie == null) {
            System.out.println("⚠️ ERREUR : setFournisseurToModify() appelé AVANT initialize() !");
            return;
        }
        this.fournisseurToModify = fournisseur;
        nomField.setText(fournisseur.getNom());
        emailField.setText(fournisseur.getEmail());
        adresseField.setText(fournisseur.getAdresse());
        comboBoxCategorie.setValue(fournisseur.getCategorie_produit());
        System.out.println("✅ Fournisseur chargé : " + fournisseur.getNom());
    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger Home.fxml", e);
        }
    }

    @FXML
    private void handleFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) fournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
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
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                comboBoxCategorie.getValue().trim().isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs obligatoires manquants",
                    "Veuillez remplir tous les champs avant de modifier un fournisseur.");
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            String categorieProduit = comboBoxCategorie.getValue().trim();

            fournisseurToModify.setNom(nom);
            fournisseurToModify.setEmail(email);
            fournisseurToModify.setAdresse(adresse);
            fournisseurToModify.setCategorie_produit(categorieProduit);

            FournisseurService fournisseurService = new FournisseurService();
            fournisseurService.modifier(fournisseurToModify);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification réussie !",
                    "Les informations du fournisseur ont été modifiées avec succès.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue", "Détails : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, title, header, "Détails : " + e.getMessage());
    }
}
