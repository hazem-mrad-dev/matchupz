package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.Club;
import models.joueur.HistoriqueClub; // Assuming this is the correct class name
import models.joueur.Joueur;
import services.joueur.ClubService;
import services.joueur.HistoriqueClubService;
import services.joueur.JoueurService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class ModifyHistorique {
    @FXML private Button Home;
    @FXML private Button modifierHistoriqueButton;
    @FXML private Button annulerButton;
    @FXML private ComboBox<String> idJoueurComboBox;
    @FXML private ComboBox<String> idClubComboBox;
    @FXML private DatePicker saisonDebutPicker;
    @FXML private DatePicker saisonFinPicker;

    private HistoriqueClubService historiqueService = new HistoriqueClubService();
    private JoueurService joueurService = new JoueurService();
    private ClubService clubService = new ClubService();
    private HistoriqueClub historiqueToModify;

    @FXML
    private void initialize() {
        idJoueurComboBox.setItems(FXCollections.observableArrayList(
                joueurService.recherche().stream().map(j -> j.getIdJoueur() + " - " + j.getNom() + " " + j.getPrenom()).toList()
        ));
        idClubComboBox.setItems(FXCollections.observableArrayList(
                clubService.recherche().stream().map(c -> c.getIdClub() + " - " + c.getNomClub()).toList()
        ));
    }

    public void setHistoriqueToModify(HistoriqueClub historique) {
        this.historiqueToModify = historique;
        idJoueurComboBox.setValue(historique.getIdJoueur() + " - " + joueurService.recherche().stream()
                .filter(j -> j.getIdJoueur() == historique.getIdJoueur())
                .map(j -> j.getNom() + " " + j.getPrenom())
                .findFirst().orElse(""));
        idClubComboBox.setValue(historique.getIdClub() + " - " + clubService.recherche().stream()
                .filter(c -> c.getIdClub() == historique.getIdClub())
                .map(Club::getNomClub)
                .findFirst().orElse(""));
        saisonDebutPicker.setValue(historique.getSaisonDebut().toLocalDate());
        saisonFinPicker.setValue(historique.getSaisonFin() != null ? historique.getSaisonFin().toLocalDate() : null);
    }

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", Home);
    }

    @FXML
    private void handleAnnulerButton() {
        loadScene("/joueur/DisplayHistorique.fxml", annulerButton);
    }

    @FXML
    private void modifier() {
        if (idJoueurComboBox.getValue() == null || idClubComboBox.getValue() == null || saisonDebutPicker.getValue() == null) {
            showAlert("Erreur", "Champs obligatoires manquants", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurComboBox.getValue().split(" - ")[0]);
            int idClub = Integer.parseInt(idClubComboBox.getValue().split(" - ")[0]);
            LocalDate saisonDebutLocal = saisonDebutPicker.getValue();
            Date saisonDebut = Date.valueOf(saisonDebutLocal); // Convert to java.sql.Date
            LocalDate saisonFinLocal = saisonFinPicker.getValue();
            Date saisonFin = saisonFinLocal != null ? Date.valueOf(saisonFinLocal) : null; // Convert to java.sql.Date

            historiqueToModify.setIdJoueur(idJoueur);
            historiqueToModify.setIdClub(idClub);
            historiqueToModify.setSaisonDebut(saisonDebut);
            historiqueToModify.setSaisonFin(saisonFin);

            historiqueService.modifier(historiqueToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Historique modifié avec succès !");
            alert.setContentText("L'historique pour le joueur " + idJoueur + " et le club " + idClub + " a été modifié.");
            alert.showAndWait();

            handleAnnulerButton();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeur incorrecte", "Veuillez sélectionner des valeurs valides pour le joueur et le club.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue", "Détails : " + e.getMessage());
        }
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", "Détails : " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}