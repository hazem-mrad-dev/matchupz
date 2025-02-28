package controllers.fournisseur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.logistics.Materiel;
import services.logistics.MaterielService;


import java.io.IOException;

public class DisplayMateriel {

    @FXML
    private Button Home;

    @FXML
    private Button materielButton;

    @FXML
    private Button fournisseurButton;

    @FXML
    private Button addMaterielButton;

    @FXML
    private Button log_out; // Added field to bind the logout button

    @FXML
    private TableView<Materiel> tableView;

    @FXML
    private TableColumn<Materiel, Integer> idColumn;

    @FXML
    private TableColumn<Materiel, String> nomColumn;

    @FXML
    private TableColumn<Materiel, String> typeColumn;

    @FXML
    private TableColumn<Materiel, Integer> quantiteColumn;

    @FXML
    private TableColumn<Materiel, String> etatColumn;

    @FXML
    private TableColumn<Materiel, Float> prixUnitaireColumn;

    @FXML
    private TableColumn<Materiel, Void> modifierColumn;

    @FXML
    private TableColumn<Materiel, Void> deleteColumn;

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private MaterielService materielService = new MaterielService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger Home.fxml", e);
        }
    }

    @FXML
    private void handleMateriel() {
        loadMateriels(); // Refresh the current view
    }

    @FXML
    private void handleFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleAddMaterielButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AjoutMateriel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addMaterielButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger AjoutMateriel.fxml", e);
        }
    }

    @FXML
    private void log_out(ActionEvent event) { // Added logout method
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger la page d'inscription.");
            alert.showAndWait();
        }
    }

    private void openModifyWindow(Materiel materiel, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/materiel/ModifyMateriel.fxml"));
            Parent root = loader.load();
            ModifyMateriel controller = loader.getController();
            controller.setMaterielToModify(materiel);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadMateriels());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifyMateriel.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId_materiel()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
        quantiteColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        prixUnitaireColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getPrix_unitaire()).asObject());

        modifierColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Modifier ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedMateriel, stage);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            materielService.supprimer(selectedMateriel);
                            loadMateriels();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadMateriels();
    }

    public void loadMateriels() {
        materielList.clear();
        materielList.addAll(materielService.recherche());
        tableView.setItems(materielList);
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("Détails : " + e.getMessage());
        alert.showAndWait();
    }
}