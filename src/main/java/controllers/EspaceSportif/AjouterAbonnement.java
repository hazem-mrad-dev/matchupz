package controllers.EspaceSportif;

import models.EspaceSportif.Abonnement;
import models.Sport;
import services.EspaceSportif.AbonnementService;
import services.SportService;
import utils.MyDatabase; // Import your MyDatabase class for connection
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AjouterAbonnement {

    @FXML
    private ComboBox<String> sportField; // Utilisation de Sport au lieu de Personne

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private DatePicker dateDebutField;

    @FXML
    private DatePicker dateFinField;

    @FXML
    private TextField tarifField;

    @FXML
    private ComboBox<String> etatField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private final AbonnementService abonnementService;
    private final SportService sportService;

    // Default constructor using MyDatabase singleton (similar to AffichageAbonnement)
    public AjouterAbonnement() {
        this.abonnementService = new AbonnementService(MyDatabase.getInstance().getConnection());
        this.sportService = new SportService();
    }

    @FXML
    public void initialize() {
        loadSports();
        loadTypes();
        loadEtats();
    }

    private void loadSports() {
        List<Sport> sports = sportService.rechercher(); // Récupère la liste des sports depuis SportService
        if (sports != null && !sports.isEmpty()) {
            for (Sport sport : sports) {
                sportField.getItems().add(sport.getNomSport()); // Ajoute les noms des sports
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun sport trouvé dans la base de données.");
        }
    }

    private void loadTypes() {
        typeField.getItems().setAll("Mensuel", "Trimestriel", "Annuel");
    }

    private void loadEtats() {
        etatField.getItems().setAll("Actif", "Expiré", "Suspendu");
    }

    @FXML
    private void ajouterAbonnement(ActionEvent event) {
        String sportNom = sportField.getValue();
        String type = typeField.getValue();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String tarifText = tarifField.getText();
        String etat = etatField.getValue();

        if (sportNom == null || type == null || dateDebut == null || dateFin == null || tarifText.isEmpty() || etat == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (dateFin.isBefore(dateDebut)) {
            showAlert(Alert.AlertType.WARNING, "Date invalide", "La date de fin doit être après la date de début.");
            return;
        }

        double tarif;
        try {
            tarif = Double.parseDouble(tarifText);
            if (tarif <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le tarif doit être un nombre positif.");
            return;
        }

        // Récupérer l'ID du sport sélectionné
        int idSport = getIdSportByName(sportNom);
        if (idSport == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sport non trouvé.");
            return;
        }

        Abonnement abonnement = new Abonnement(0, idSport, sportNom, type, Date.valueOf(dateDebut), Date.valueOf(dateFin), tarif, etat);
        try {
            abonnementService.ajouter(abonnement);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement ajouté avec succès !");
            goToAfficherAbonnements(event);
        } catch (RuntimeException e) { // Updated to catch RuntimeException (from AbonnementService)
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'abonnement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getIdSportByName(String nomSport) {
        List<Sport> sports = sportService.rechercher();
        for (Sport sport : sports) {
            if (sport.getNomSport().equals(nomSport)) {
                return sport.getIdSport();
            }
        }
        return -1;
    }

    @FXML
    private void goToAfficherAbonnements(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AffichageAbonnement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        sportField.setValue(null);
        typeField.setValue(null);
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        tarifField.clear();
        etatField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}