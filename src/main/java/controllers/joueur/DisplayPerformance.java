package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.PerformanceJoueur;
import services.joueur.PerformanceJoueurService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class DisplayPerformance {
    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addPerformanceButton;
    @FXML private Button searchButton; // Added for search functionality
    @FXML private TextField searchField; // Added for search functionality
    @FXML private TableView<PerformanceJoueur> tableView;
    @FXML private TableColumn<PerformanceJoueur, Integer> idPerformanceColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> idJoueurColumn;
    @FXML private TableColumn<PerformanceJoueur, String> saisonColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> nombreMatchesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> minutesJoueesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> butsMarquesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> passesDecisivesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> cartonsJaunesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> cartonsRougesColumn;
    @FXML private TableColumn<PerformanceJoueur, Void> modifierColumn;
    @FXML private TableColumn<PerformanceJoueur, Void> deleteColumn;

    private ObservableList<PerformanceJoueur> performanceList = FXCollections.observableArrayList();
    private PerformanceJoueurService performanceService = new PerformanceJoueurService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Home page", e);
        }
    }

    @FXML
    private void handleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) joueurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Main Joueur page", e);
        }
    }

    @FXML
    private void handlePerformanceButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutPerformance.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addPerformanceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Add Performance page", e);
        }
    }

    private void openModifyWindow(PerformanceJoueur performance, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierPerformance.fxml"));
            Parent root = loader.load();
            ModifyPerformance controller = loader.getController();
            controller.setPerformanceToModify(performance);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> reloadDisplay(stage));
        } catch (IOException e) {
            showError("Failed to load the Modify Performance page", e);
        }
    }

    private void reloadDisplay(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayPerformance.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to reload DisplayPerformance.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        if (idPerformanceColumn == null) {
            System.err.println("idPerformanceColumn is null - Check FXML fx:id");
            return; // Early exit for debugging
        }

        // Initialize columns with appropriate data
        idPerformanceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPerformance()).asObject());
        idJoueurColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdJoueur()).asObject());
        saisonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSaison()));
        nombreMatchesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNombreMatches()).asObject());
        minutesJoueesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMinutesJouees()).asObject());
        butsMarquesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getButsMarques()).asObject());
        passesDecisivesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPassesDecisives()).asObject());
        cartonsJaunesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCartonsJaunes()).asObject());
        cartonsRougesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCartonsRouges()).asObject());

        // Modify column with a button
        modifierColumn.setCellFactory(param -> new TableCell<PerformanceJoueur, Void>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    PerformanceJoueur selectedPerformance = getTableView().getItems().get(getIndex());
                    showConfirmation("Are you sure you want to modify this performance?",
                            "ID Performance: " + selectedPerformance.getIdPerformance(), () -> {
                                Stage stage = (Stage) btn.getScene().getWindow();
                                openModifyWindow(selectedPerformance, stage);
                            });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Delete column with confirmation dialog
        deleteColumn.setCellFactory(param -> new TableCell<PerformanceJoueur, Void>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    PerformanceJoueur selectedPerformance = getTableView().getItems().get(getIndex());
                    showConfirmation("Are you sure you want to delete this performance?",
                            "ID Performance: " + selectedPerformance.getIdPerformance(), () -> {
                                performanceService.supprimer(selectedPerformance);
                                loadPerformances();
                            });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Search button action
        searchButton.setOnAction(event -> handleSearch());

        // Optional: Real-time filtering as user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPerformances(newValue);
        });

        loadPerformances();
    }

    public void loadPerformances() {
        performanceList.clear();
        performanceList.addAll(performanceService.recherche());
        tableView.setItems(performanceList);
    }

    @FXML
    private void handleSearch() {
        filterPerformances(searchField.getText());
    }

    private void filterPerformances(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(performanceList);
        } else {
            ObservableList<PerformanceJoueur> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (PerformanceJoueur performance : performanceList) {
                if (String.valueOf(performance.getIdJoueur()).contains(lowerCaseFilter) ||
                        performance.getSaison().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(performance);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    private void showError(String header, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("Details: " + e.getMessage());
        alert.showAndWait();
    }

    private void showConfirmation(String header, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}