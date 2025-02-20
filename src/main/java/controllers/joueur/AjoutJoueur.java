package controllers.joueur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Joueur;
import services.JoueurService;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Date; // Ensure this is imported
import java.time.LocalDate;

public class AjoutJoueur {

    @FXML
    private Button annulerButton;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField dateNaissanceField; // Change to DatePicker

    @FXML
    private TextField posteField; // Changed back to TextField

    @FXML
    private TextField tailleField; // Changed back to TextField

    @FXML
    private TextField poidsField; // Changed back to TextField

    @FXML
    private TextField statutField; // Changed back to TextField

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField idSportField;

    @FXML
    private Button ajouterButton;

    @FXML
    private void handleAnnulerButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) annulerButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Validate that all fields are filled
        if (nomField.getText().trim().isEmpty() || prenomField.getText().trim().isEmpty() ||
                dateNaissanceField.getText().trim().isEmpty() || posteField.getText().trim().isEmpty() ||
                tailleField.getText().trim().isEmpty() || poidsField.getText().trim().isEmpty() ||
                statutField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty() || idSportField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant d'ajouter un joueur.");
            alert.showAndWait();
            return;
        }

        try {
            // Retrieve values from the fields
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String dateNaissanceStr = dateNaissanceField.getText().trim(); // Get date from TextField
            String poste = posteField.getText().trim();
            float taille = Float.parseFloat(tailleField.getText().trim());
            float poids = Float.parseFloat(poidsField.getText().trim());
            String statut = statutField.getText().trim();
            String email = emailField.getText().trim();
            String telephone = telephoneField.getText().trim();
            int idSport = Integer.parseInt(idSportField.getText().trim());

            // Validate poste
            String[] validPostes = {"GK", "RB", "LB", "RWB", "LWB", "SW", "DM", "CM", "AM", "RM", "LM", "RW", "LW", "CF", "ST", "SS"};
            boolean isPosteValid = false;
            for (String validPoste : validPostes) {
                if (poste.equals(validPoste)) {
                    isPosteValid = true;
                    break;
                }
            }
            if (!isPosteValid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Poste invalide");
                alert.setContentText("Veuillez entrer un poste valide : " + String.join(", ", validPostes) + ".");
                alert.showAndWait();
                return;
            }

            String[] validStatuts = {"Actif", "Blessé", "Suspendu", ""};
            boolean isStatutValid = false;
            for (String validStatut : validStatuts) {
                if (statut.equals(validStatut)) {
                    isStatutValid = true;
                    break;
                }
            }
            if (!isStatutValid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Statut invalide");
                alert.setContentText("Veuillez entrer un statut valide : " + String.join(", ", validStatuts) + ".");
                alert.showAndWait();
                return;
            }

            // Validate taille
            if (taille < 1.50 || taille > 2.50) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Taille invalide");
                alert.setContentText("La taille doit être comprise entre 1.50 m et 2.50 m.");
                alert.showAndWait();
                return;
            }

            // Validate poids
            if (poids < 60 || poids > 110) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Poids invalide");
                alert.setContentText("Le poids doit être compris entre 60 kg et 110 kg.");
                alert.showAndWait();
                return;
            }

            // Validate email
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Email invalide");
                alert.setContentText("Veuillez entrer une adresse e-mail valide.");
                alert.showAndWait();
                return;
            }

            // Validate telephone
            String telephoneRegex = "^\\+?[1-9][0-9]{1,14}$";
            if (!telephone.matches(telephoneRegex)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Téléphone invalide");
                alert.setContentText("Veuillez entrer un numéro de téléphone valide.");
                alert.showAndWait();
                return;
            }

            // Validate date format
            java.sql.Date dateNaissance;
            try {
                dateNaissance = java.sql.Date.valueOf(dateNaissanceStr); // Ensure the format is valid
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de format");
                alert.setHeaderText("Format de date invalide");
                alert.setContentText("Veuillez entrer la date dans le format yyyy-mm-dd.");
                alert.showAndWait();
                return;
            }

            // Create and save the Joueur object
            Joueur joueur = new Joueur(nom, prenom, dateNaissance, poste, taille, poids, statut, email, telephone, idSport);
            JoueurService joueurService = new JoueurService();
            joueurService.ajouter(joueur);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Joueur ajouté avec succès !");
            alert.setContentText("Le joueur " + nom + " " + prenom + " a été ajouté.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setHeaderText("Valeur incorrecte");
            alert.setContentText("Veuillez entrer des valeurs numériques valides pour la taille, le poids et l'ID du sport.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void login(ActionEvent actionEvent) {
        // Code for login handling
    }

    public void goAvatarOnPinterest(ActionEvent actionEvent) {
        // Code for opening Pinterest (if needed)
    }

    public void handleTeamsButton(ActionEvent event) {
        // Code for handling the "Teams" button
    }
}
