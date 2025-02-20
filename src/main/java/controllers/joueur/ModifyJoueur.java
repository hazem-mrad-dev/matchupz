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
import java.sql.Date;

public class ModifyJoueur {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField dateNaissanceField;

    @FXML
    private TextField posteField;

    @FXML
    private TextField tailleField;

    @FXML
    private TextField poidsField;

    @FXML
    private TextField statutField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField idSportField;

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    @FXML
    private Button joueurButton;

    @FXML
    private Button Home;

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();

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
    private void HandleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
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

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
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
    private Joueur joueur;
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }
    private Joueur joueurToModify;

    public void setJoueurToModify(Joueur joueur) {
        this.joueurToModify = joueur;
        nomField.setText(joueur.getNom());
        prenomField.setText(joueur.getPrenom());
        dateNaissanceField.setText(joueur.getDateNaissance().toString());
        posteField.setText(joueur.getPoste());
        tailleField.setText(String.valueOf(joueur.getTaille()));
        poidsField.setText(String.valueOf(joueur.getPoids()));
        statutField.setText(joueur.getStatut());
        emailField.setText(joueur.getEmail());
        telephoneField.setText(joueur.getTelephone());
        idSportField.setText(String.valueOf(joueur.getIdSport()));
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() || prenomField.getText().trim().isEmpty() ||
                dateNaissanceField.getText().trim().isEmpty() || posteField.getText().trim().isEmpty() ||
                tailleField.getText().trim().isEmpty() || poidsField.getText().trim().isEmpty() ||
                statutField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty() || idSportField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier un joueur.");
            alert.showAndWait();
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String dateNaissanceStr = dateNaissanceField.getText().trim();
            String poste = posteField.getText().trim();
            float taille = Float.parseFloat(tailleField.getText().trim());
            float poids = Float.parseFloat(poidsField.getText().trim());
            String statut = statutField.getText().trim();
            String email = emailField.getText().trim();
            String telephone = telephoneField.getText().trim();
            int idSport = Integer.parseInt(idSportField.getText().trim());

            java.sql.Date dateNaissance;
            try {
                dateNaissance = java.sql.Date.valueOf(dateNaissanceStr);
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de format");
                alert.setHeaderText("Format de date invalide");
                alert.setContentText("Veuillez entrer la date dans le format yyyy-mm-dd.");
                alert.showAndWait();
                return;
            }

            joueurToModify.setNom(nom);
            joueurToModify.setPrenom(prenom);
            joueurToModify.setDateNaissance(dateNaissance);
            joueurToModify.setPoste(poste);
            joueurToModify.setTaille(taille);
            joueurToModify.setPoids(poids);
            joueurToModify.setStatut(statut);
            joueurToModify.setEmail(email);
            joueurToModify.setTelephone(telephone);
            joueurToModify.setIdSport(idSport);

            JoueurService joueurService = new JoueurService();
            joueurService.modifier(joueurToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Modification réussie !");
            alert.setContentText("Les informations du joueur ont été modifiées avec succès.");
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
        }

    }
}

