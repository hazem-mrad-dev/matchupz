package controllers.EspaceSportif;

import models.EspaceSportif.Abonnement;
import models.joueur.Club;  // Changé de Sport à Club
import services.EspaceSportif.AbonnementService;
import services.joueur.ClubService;  // Changé de SportService à ClubService
import utils.MyDataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AjouterAbonnement {

    @FXML
    private ComboBox<String> clubField;  // Changé de sportField à clubField

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
    private final ClubService clubService;  // Changé de SportService à ClubService

    public AjouterAbonnement() {
        this.abonnementService = new AbonnementService(MyDataSource.getInstance().getConn());
        this.clubService = new ClubService();  // Changé de SportService à ClubService
    }

    @FXML
    public void initialize() {
        loadClubs();  // Changé de loadSports à loadClubs
        loadTypes();
        loadEtats();
    }

    private void loadClubs() {  // Changé de loadSports à loadClubs
        List<Club> clubs = clubService.recherche();  // Changé de sportService à clubService
        if (clubs != null && !clubs.isEmpty()) {
            for (Club club : clubs) {
                clubField.getItems().add(club.getNomClub());  // Changé de getNomSport() à getNomClub()
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun club trouvé dans la base de données.");
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
        String clubNom = clubField.getValue();  // Changé de sportNom à clubNom
        String type = typeField.getValue();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String tarifText = tarifField.getText();
        String etat = etatField.getValue();

        if (clubNom == null || type == null || dateDebut == null || dateFin == null || tarifText.isEmpty() || etat == null) {
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

        // Récupérer l'ID du club sélectionné
        int idClub = getIdClubByName(clubNom);  // Changé de getIdSportByName à getIdClubByName
        if (idClub == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Club non trouvé.");
            return;
        }

        Abonnement abonnement = new Abonnement(0, idClub, clubNom, type, Date.valueOf(dateDebut), Date.valueOf(dateFin), tarif, etat);  // Changé idSport à idClub et sportNom à clubNom
        try {
            abonnementService.ajouter(abonnement);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement ajouté avec succès !");
            goToAfficherAbonnements(event);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'abonnement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getIdClubByName(String nomClub) {  // Changé de getIdSportByName à getIdClubByName
        List<Club> clubs = clubService.recherche();  // Changé de sportService à clubService
        for (Club club : clubs) {
            if (club.getNomClub().equals(nomClub)) {  // Changé de getNomSport() à getNomClub()
                return club.getIdClub();  // Changé de getIdSport() à getIdClub()
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
        clubField.setValue(null);  // Changé de sportField à clubField
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