package controllers.sponsor;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Contrat;
import services.ContratService;

import java.io.IOException;
import java.util.List;

import static models.Contrat.convertToDateFormat;

public class AjouterContrat {

    @FXML
    private Button btnAjouterContrat, btnAnnuler, btnSupprimerContrat, btnModifierContrat, btnActualiserContrat;

    @FXML
    private TextField tx_contrat_titre, tx_contrat_montant, tx_contrat_sponsor;

    @FXML
    private DatePicker dp_date_debut, dp_date_fin;

    @FXML
    private TableView<Contrat> tableViewContrats;

    @FXML
    private TableColumn<Contrat, String> colTitre, colSponsor, colDateDebut, colDateFin;

    @FXML
    private TableColumn<Contrat, Float> colMontant;

    private final ContratService contratService = new ContratService();
    private ObservableList<Contrat> contratList = FXCollections.observableArrayList();
    private Contrat selectedContrat = null;

    @FXML
    private void ajouterContrat(ActionEvent event) {
        handleAjouterOuModifier();
    }

    @FXML
    private void goToAfficherContrats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherContrat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Contrats");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AfficherContrat.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        colSponsor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId_sponsor())));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebut()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFin()));
        colMontant.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getMontant()).asObject());

        tableViewContrats.setItems(contratList);
        loadContrats();

        btnAjouterContrat.setOnAction(event -> handleAjouterOuModifier());
        btnAnnuler.setOnAction(event -> clearFields());
        btnSupprimerContrat.setOnAction(event -> handleSupprimer());
        btnActualiserContrat.setOnAction(event -> loadContrats());

        tableViewContrats.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedContrat = newSelection;
                tx_contrat_titre.setText(selectedContrat.getTitre());
                tx_contrat_sponsor.setText(String.valueOf(selectedContrat.getId_sponsor()));
                dp_date_debut.setValue(java.time.LocalDate.parse(selectedContrat.getDateDebut()));
                dp_date_fin.setValue(java.time.LocalDate.parse(selectedContrat.getDateFin()));
                tx_contrat_montant.setText(String.valueOf(selectedContrat.getMontant()));
            }
        });
    }

    private void handleAjouterOuModifier() {
        String titre = tx_contrat_titre.getText().trim();
        String sponsorStr = tx_contrat_sponsor.getText().trim();
        String dateDebut = (dp_date_debut.getValue() != null) ? dp_date_debut.getValue().toString() : "";
        String dateFin = (dp_date_fin.getValue() != null) ? dp_date_fin.getValue().toString() : "";
        String montantStr = tx_contrat_montant.getText().trim();

        if (titre.isEmpty() || sponsorStr.isEmpty() || dateDebut.isEmpty() || dateFin.isEmpty() || montantStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        dateDebut = convertToDateFormat(dateDebut);
        dateFin = convertToDateFormat(dateFin);

        if (dateDebut == null || dateFin == null) {
            showAlert(Alert.AlertType.ERROR, "Format de date incorrect", "La date doit avoir le format AAAA/MM/JJ !");
            return;
        }

        int sponsorId;
        try {
            sponsorId = Integer.parseInt(sponsorStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID du sponsor doit être un nombre valide !");
            return;
        }

        float montant;
        try {
            montant = Float.parseFloat(montantStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être un nombre valide !");
            return;
        }

        if (selectedContrat == null) {
            Contrat contrat = new Contrat(sponsorId, titre, dateDebut, dateFin, montant);
            contratService.ajouter(contrat);
            contratList.add(contrat);
        } else {
            selectedContrat.setTitre(titre);
            selectedContrat.setId_sponsor(sponsorId);
            selectedContrat.setDateDebut(dateDebut);
            selectedContrat.setDateFin(dateFin);
            selectedContrat.setMontant(montant);
            contratService.modifier(selectedContrat);
            loadContrats();
        }

        clearFields();
    }

    private void loadContrats() {
        contratList.clear();
        contratList.addAll(contratService.recherche());
    }

    private void handleSupprimer() {
        Contrat selected = tableViewContrats.getSelectionModel().getSelectedItem();
        if (selected != null) {
            contratService.supprimer(selected);
            contratList.remove(selected);
        }
    }

    private void clearFields() {
        tx_contrat_titre.clear();
        tx_contrat_sponsor.clear();
        dp_date_debut.setValue(null);
        dp_date_fin.setValue(null);
        tx_contrat_montant.clear();
        selectedContrat = null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
