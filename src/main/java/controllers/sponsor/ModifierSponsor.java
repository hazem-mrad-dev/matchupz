package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Sponsor;
import services.SponsorService;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;

public class ModifierSponsor {

    @FXML
    private Button annulerButton;

    @FXML
    private TextField nomField;

    @FXML
    private TextField contactField;

    @FXML
    private ComboBox<String> packField;
    @FXML
    private Button ajouterButton;



    @FXML
    private AnchorPane home_form;

    @FXML
    private Button logout;


    @FXML
    private Button modifierButton;
    @FXML
    void ajouterSponsor(ActionEvent event) {

    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des sponsors.");
            e.printStackTrace();
        }
    }

    private Sponsor sponsorToModify;

    public void setSponsorToModify(Sponsor sponsor) {
        this.sponsorToModify = sponsor;
        nomField.setText(sponsor.getNom());
        contactField.setText(sponsor.getContact());
        packField.setValue(sponsor.getPack());
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() || contactField.getText().trim().isEmpty() || packField.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier un sponsor.");
            alert.showAndWait();
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String contact = contactField.getText().trim();
            String pack = packField.getValue();

            sponsorToModify.setNom(nom);
            sponsorToModify.setContact(contact);
            sponsorToModify.setPack(pack);

            SponsorService sponsorService = new SponsorService();
            sponsorService.modifier(sponsorToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Modification réussie !");
            alert.setContentText("Les informations du sponsor ont été modifiées avec succès.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    public void initialize() {
    packField.setItems(FXCollections.observableArrayList("Bronze", "Silver", "Gold"));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
