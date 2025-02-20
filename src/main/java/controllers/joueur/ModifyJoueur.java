package controllers.joueur;

import models.Joueur;
import services.JoueurService;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

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

    private Joueur joueur; // The Joueur object you want to modify

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        // You can populate the UI with the current player's details if needed
    }
    private Joueur joueurToModify; // Holds the current Joueur to modify

    public void setJoueurToModify(Joueur joueur) {
        this.joueurToModify = joueur;
        // Pre-fill the fields with the player's current information
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
        // Validate that all fields are filled
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
            // Retrieve values from the fields
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

            // Validate inputs (reuse validation logic from AjoutJoueur if necessary)

            // Validate date format
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

            // Update the Joueur object
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

            // Save the changes using the JoueurService
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

