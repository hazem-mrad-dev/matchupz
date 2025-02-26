package controllers.joueur;

import controllers.joueur.ModifySport;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.Sport;
import services.joueur.SportService;

import java.io.IOException;

public class DisplaySport {
    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addSportButton;
    @FXML private Button searchButton; // Added for search functionality
    @FXML private TextField searchField; // Existing search field
    @FXML private TableView<Sport> tableView;
    @FXML private TableColumn<Sport, Integer> idSportColumn;
    @FXML private TableColumn<Sport, String> nomSportColumn;
    @FXML private TableColumn<Sport, String> descriptionColumn;
    @FXML private TableColumn<Sport, Void> modifierColumn;
    @FXML private TableColumn<Sport, Void> deleteColumn;

    private ObservableList<Sport> sportList = FXCollections.observableArrayList();
    private SportService sportService = new SportService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the MainController page", e);
        }
    }

    @FXML
    private void HandleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) joueurButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the MainController page", e);
        }
    }

    @FXML
    private void handleAddSportButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutSport.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addSportButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Add Sport page", e);
        }
    }

    private void openModifyWindow(Sport sport, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierSport.fxml"));
            Parent root = loader.load();
            ModifySport controller = loader.getController();
            controller.setSportToModify(sport);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> loadSports()); // Simplified reload
        } catch (IOException e) {
            showError("Failed to load the Modify Sport page", e);
        }
    }

    @FXML
    public void initialize() {
        // Initialize columns with appropriate data
        idSportColumn.setCellValueFactory(cellData -> cellData.getValue().idSportProperty().asObject());
        nomSportColumn.setCellValueFactory(cellData -> cellData.getValue().nomSportProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Modifier column with confirmation dialog
        modifierColumn.setCellFactory(param -> new TableCell<Sport, Void>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to modify this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedSport, stage);
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

        // Delete column with confirmation dialog
        deleteColumn.setCellFactory(param -> new TableCell<Sport, Void>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            sportService.supprimer(selectedSport);
                            loadSports();
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

        // Search button action
        searchButton.setOnAction(event -> handleSearch());

        // Real-time filtering as user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSports(newValue));

        loadSports();
    }

    public void loadSports() {
        sportList.clear();
        sportList.addAll(sportService.recherche());
        tableView.setItems(sportList);
    }

    @FXML
    private void handleSearch() {
        filterSports(searchField.getText());
    }

    private void filterSports(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(sportList);
        } else {
            ObservableList<Sport> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (Sport sport : sportList) {
                if (String.valueOf(sport.getIdSport()).contains(lowerCaseFilter) ||
                        sport.getNomSport().toLowerCase().contains(lowerCaseFilter) ||
                        sport.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(sport);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    private void showError(String header, IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("Details: " + e.getMessage());
        alert.showAndWait();
    }
}