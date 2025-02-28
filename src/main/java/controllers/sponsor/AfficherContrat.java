package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Contrat;
import services.ContratService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherContrat {

    @FXML
    private TableView<Contrat> tableViewContrats;

    @FXML
    private TableColumn<Contrat, String> colTitre;

    @FXML
    private TableColumn<Contrat, String> colSponsor;

    @FXML
    private TableColumn<Contrat, String> colDateDebut;

    @FXML
    private TableColumn<Contrat, String> colDateFin;

    @FXML
    private TableColumn<Contrat, String> colMontant;

    @FXML
    private TableColumn<Contrat, Void> colActions;

    private final ContratService contratService = new ContratService();
    private final ObservableList<Contrat> contratList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitre()));
        colSponsor.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getId_sponsor())));
        colDateDebut.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateDebut().toString()));
        colDateFin.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDateFin().toString()));
        colMontant.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getMontant())));

        addActionsColumn();
        loadContrats();
    }

    private void addActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Contrat, Void> call(TableColumn<Contrat, Void> param) {
                return new TableCell<>() {
                    final Button btnModifier = new Button("Modifier");
                    final Button btnSupprimer = new Button("Supprimer");
                    final HBox pane = new HBox(10, btnModifier, btnSupprimer);

                    {
                        btnModifier.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        btnSupprimer.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        });
    }

    private void loadContrats() {
        contratList.clear();
        List<Contrat> contrats = contratService.recherche();
        contratList.addAll(contrats);
        tableViewContrats.setItems(contratList);
    }

    @FXML
    private void addContrat() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter un contrat");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadContrats();
    }

    private void handleEdit(Contrat contrat) {
        if (contrat == null) return;
        System.out.println("Modifier " + contrat.getTitre());
    }

    private void handleDelete(Contrat contrat) {
        if (contrat == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le contrat");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + contrat.getTitre() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                contratService.supprimer(contrat);
                loadContrats();
            }
        });
    }
}
