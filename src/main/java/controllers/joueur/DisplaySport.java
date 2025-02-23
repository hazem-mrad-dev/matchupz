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
import models.Sport;
import services.*;

import java.io.IOException;

public class DisplaySport {
    @FXML
    private Button joueurButton;
    @FXML
    private Button Home;
    @FXML
    private Button addSportButton;
    @FXML
    private TableView<Sport> tableView;
    @FXML
    private TableColumn<Sport, Integer> idSportColumn;
    @FXML
    private TableColumn<Sport, String> nomSportColumn;
    @FXML
    private TableColumn<Sport, String> descriptionColumn;
    @FXML
    private TableColumn<Sport, Void> modifierColumn;
    @FXML
    private TableColumn<Sport, Void> deleteColumn;

    private ObservableList<Sport> sportList = FXCollections.observableArrayList();
    private SportService sportService = new SportService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void openModifyWindow(Sport sport, Stage stage) {
        try {
            // Load ModifierSport.fxml for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierSport.fxml"));
            Parent root = loader.load();

            ModifySport controller = loader.getController();
            controller.setSportToModify(sport);

            // Set the scene for the modification window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Assume after some action (like entering data or focus lost), you want to switch to DisplaySport
            stage.setOnHidden(event -> {
                try {
                    // After modification, load DisplaySport.fxml
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/joueur/DisplaySport.fxml"));
                    Parent displayRoot = displayLoader.load();

                    // Change the scene to DisplaySport
                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene); // Change the scene
                    stage.show(); // Show the new scene (DisplaySport.fxml)
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load DisplaySport.fxml");
                    alert.setContentText("Details: " + e.getMessage());
                    alert.showAndWait();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
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
                btn.setId("btn-modify"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before modification
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to modify this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow(); // Get the stage from the button clicked
                            openModifyWindow(selectedSport, stage); // Pass stage directly
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
            private final Button btn = new Button("Delete");

            {
                btn.setId("btn-delete"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before deletion
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            sportService.supprimer(selectedSport);  // Delete the sport using the service
                            loadSports();  // Reload the table after deletion
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
        loadSports();  // Load sports into the table view
    }

    public void loadSports() {
        sportList.clear();
        sportList.addAll(sportService.recherche());  // Fetch list of sports from the service
        tableView.setItems(sportList);
    }
}
