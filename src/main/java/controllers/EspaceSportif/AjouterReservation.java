package controllers.EspaceSportif;

import models.EspaceSportif.Reservation;
import services.EspaceSportif.ReservationService;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AjouterReservation {

    @FXML
    private ComboBox<String> lieuField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> motifField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private final ReservationService reservationService = new ReservationService();
    private final EspaceSportifService espaceService = new EspaceSportifService();

    @FXML
    public void initialize() {
        loadLieux();
        loadMotifs();
        loadStatus();
    }

    private void loadLieux() {
        List<String> lieux = espaceService.getLieux();
        if (lieux != null && !lieux.isEmpty()) {
            lieuField.getItems().setAll(lieux);
        }
    }

    private void loadMotifs() {
        motifField.getItems().setAll("match", "entraînement", "tournoi", "autre");
    }

    private void loadStatus() {
        statusField.getItems().setAll("confirmée", "annulée", "en attente");
    }

    @FXML
    private void ajouterReservation(ActionEvent event) {
        String lieu = lieuField.getValue();
        LocalDate date = dateField.getValue();
        String motif = motifField.getValue();
        String status = statusField.getValue();

        if (lieu == null || date == null || motif == null || status == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Date invalide", "La date choisie doit être ultérieure à aujourd'hui.");
            return;
        }

        int idLieu = espaceService.getIdLieuByName(lieu);
        Timestamp dateReservee = Timestamp.valueOf(date.atStartOfDay());

        Reservation reservation = new Reservation(idLieu, dateReservee, motif, status);
        reservationService.ajouter(reservation);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");
        goToAfficherReservations(event);
    }

    @FXML
    private void goToAfficherReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réservations");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AffichageReservations.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        lieuField.setValue(null);
        dateField.setValue(null);
        motifField.setValue(null);
        statusField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
