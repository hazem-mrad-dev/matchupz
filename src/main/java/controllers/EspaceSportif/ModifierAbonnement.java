package controllers.EspaceSportif;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.EspaceSportif.Abonnement;
import models.joueur.Club;  // Changé de Sport à Club
import services.EspaceSportif.AbonnementService;
import services.joueur.ClubService;  // Changé de SportService à ClubService
import utils.MyDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ModifierAbonnement {

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
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private final AbonnementService abonnementService;
    private final ClubService clubService;  // Changé de SportService à ClubService
    private Abonnement abonnementToEdit;

    public ModifierAbonnement() {
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
        try {
            List<Club> clubs = clubService.recherche();  // Changé de sportService à clubService
            if (clubs != null && !clubs.isEmpty()) {
                clubField.getItems().setAll(clubs.stream().map(Club::getNomClub).toList());  // Changé de getNomSport à getNomClub
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun club", "Aucun club disponible.");
            }
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les clubs : " + e.getMessage());
        }
    }

    private void loadTypes() {
        typeField.getItems().setAll("Mensuel", "Trimestriel", "Annuel");
    }

    private void loadEtats() {
        etatField.getItems().setAll("Actif", "Expiré", "Suspendu");
    }

    private LocalDate sqlDateToLocalDate(Date sqlDate) {
        if (sqlDate == null) return null;
        return sqlDate.toLocalDate();
    }

    public void setAbonnementToEdit(Abonnement abonnement) {
        this.abonnementToEdit = abonnement;
        clubField.setValue(abonnement.getNomClub());  // Changé de getNomSport à getNomClub
        typeField.setValue(abonnement.getTypeAbonnement());
        dateDebutField.setValue(sqlDateToLocalDate(abonnement.getDateDebut()));
        dateFinField.setValue(sqlDateToLocalDate(abonnement.getDateFin()));
        tarifField.setText(String.valueOf(abonnement.getTarif()));
        etatField.setValue(abonnement.getEtat());
    }

    @FXML
    private void updateAbonnement(ActionEvent event) {
        if (abonnementToEdit == null) return;

        String clubNom = clubField.getValue();  // Changé de sportNom à clubNom
        String type = typeField.getValue();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String tarifText = tarifField.getText();
        String etat = etatField.getValue();

        // Validation des champs
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

        // Récupérer l'ID du club
        int idClub = getIdClubByName(clubNom);  // Changé de getIdSport à getIdClub
        if (idClub == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Club non trouvé.");
            return;
        }

        // Mettre à jour l'objet Abonnement
        abonnementToEdit.setIdClub(idClub);  // Changé de setIdSport à setIdClub
        abonnementToEdit.setNomClub(clubNom);  // Changé de setNomSport à setNomClub
        abonnementToEdit.setTypeAbonnement(type);
        abonnementToEdit.setDateDebut(Date.valueOf(dateDebut));
        abonnementToEdit.setDateFin(Date.valueOf(dateFin));
        abonnementToEdit.setTarif(tarif);
        abonnementToEdit.setEtat(etat);

        try {
            abonnementService.modifier(abonnementToEdit);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement modifié avec succès !");
            goToAfficherAbonnements(event);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier l'abonnement : " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        closeWindow(event);
    }

    private int getIdClubByName(String nomClub) {  // Changé de getIdSportByName à getIdClubByName
        try {
            List<Club> clubs = clubService.recherche();  // Changé de sportService à clubService
            for (Club club : clubs) {
                if (club.getNomClub().equals(nomClub)) {  // Changé de getNomSport à getNomClub
                    return club.getIdClub();  // Changé de getIdSport à getIdClub
                }
            }
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des clubs : " + e.getMessage());
        }
        return -1;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        }
    }
}