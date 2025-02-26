package controllers.joueur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import models.joueur.EvaluationPhysique;
import services.joueur.EvaluationPhysiqueService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class AjoutEvaluation {

    @FXML private Button joueurButton;
    @FXML private Button Home;
    @FXML private Button annulerButton;
    @FXML private Button ajouterButton;
    @FXML private Button handleEvaluationButton; // Note: This should be renamed or mapped in FXML
    @FXML private Button logout;
    @FXML private TextField idJoueurField;
    @FXML private DatePicker dateEvaluationField;
    @FXML private TextField niveauEnduranceField;
    @FXML private TextField forcePhysiqueField;
    @FXML private TextField vitesseField;
    @FXML private TextField etatBlessureField;

    private EvaluationPhysiqueService evaluationService = new EvaluationPhysiqueService();

    @FXML
    private void handleHome() {
        loadFXML("/joueur/MainController.fxml", "Failed to load the Main page", Home);
    }

    @FXML
    private void handleAnnulerButton() {
        loadFXML("/joueur/DisplayEvaluation.fxml", "Failed to load the Display Evaluations page", annulerButton);
    }

    @FXML
    private void HandleJoueur() {
        loadFXML("/joueur/MainController.fxml", "Failed to load the Main Joueur page", joueurButton);
    }

    @FXML
    private void ajouter() {
        if (idJoueurField.getText().trim().isEmpty() || dateEvaluationField.getValue() == null ||
                niveauEnduranceField.getText().trim().isEmpty() || forcePhysiqueField.getText().trim().isEmpty() ||
                vitesseField.getText().trim().isEmpty() || etatBlessureField.getText().trim().isEmpty()) {

            showErrorAlert("Missing Required Fields", "Please fill in all fields before adding an evaluation.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurField.getText().trim());
            LocalDate dateEvaluation = dateEvaluationField.getValue();
            float niveauEndurance = Float.parseFloat(niveauEnduranceField.getText().trim());
            float forcePhysique = Float.parseFloat(forcePhysiqueField.getText().trim());
            float vitesse = Float.parseFloat(vitesseField.getText().trim());
            String etatBlessure = etatBlessureField.getText().trim();

            if (idJoueur <= 0) {
                showErrorAlert("Invalid Input", "Player ID must be a positive number.");
                return;
            }

            if (niveauEndurance < 0 || forcePhysique < 0 || vitesse < 0) {
                showErrorAlert("Invalid Input", "Endurance, Strength, and Speed must be non-negative.");
                return;
            }

            Date sqlDateEvaluation = Date.valueOf(dateEvaluation);
            EvaluationPhysique evaluation = new EvaluationPhysique(
                    idJoueur, sqlDateEvaluation, niveauEndurance, forcePhysique, vitesse, etatBlessure
            );

            evaluationService.ajouter(evaluation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Evaluation Added Successfully!");
            alert.setContentText("The evaluation for player ID " + idJoueur + " has been added with ID " + evaluation.getIdEvaluation() + ".");
            alert.showAndWait();

            clearFields();
            handleAnnulerButton();

        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Number Format", "Please enter valid numbers for ID, Endurance, Strength, and Speed.");
        } catch (Exception e) {
            showErrorAlert("An Error Occurred", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showErrorAlert("Logout Failed", e.getMessage());
        }
    }

    // Temporary handler for unmapped button (remove or map in FXML)
    @FXML
    private void handleEvaluation() {
        System.out.println("handleEvaluationButton clicked - not implemented yet");
    }

    private void loadFXML(String fxmlPath, String errorMessage, Button sourceButton) {
        try {
            java.net.URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                showErrorAlert("Resource Not Found", "FXML file not found: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert(errorMessage, e.getMessage());
        }
    }

    private void clearFields() {
        idJoueurField.clear();
        dateEvaluationField.setValue(null);
        niveauEnduranceField.clear();
        forcePhysiqueField.clear();
        vitesseField.clear();
        etatBlessureField.clear();
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}