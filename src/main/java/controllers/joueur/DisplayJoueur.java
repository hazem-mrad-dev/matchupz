package controllers.joueur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Joueur;
import services.JoueurService;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class DisplayJoueur {

    @FXML
    private Button Home;

    @FXML
    private Button joueurButton;

    @FXML
    private Button addJoueurButton;

    @FXML
    private TableView<Joueur> tableView;

    @FXML
    private TableColumn<Joueur, Integer> idColumn;

    @FXML
    private TableColumn<Joueur, Integer> nomSportColumn;

    @FXML
    private TableColumn<Joueur, String> nomColumn;

    @FXML
    private TableColumn<Joueur, String> prenomColumn;

    @FXML
    private TableColumn<Joueur, String> dateNaissanceColumn;

    @FXML
    private TableColumn<Joueur, String> posteColumn;

    @FXML
    private TableColumn<Joueur, Float> tailleColumn;

    @FXML
    private TableColumn<Joueur, Float> poidsColumn;

    @FXML
    private TableColumn<Joueur, String> statutColumn;

    @FXML
    private TableColumn<Joueur, String> emailColumn;

    @FXML
    private TableColumn<Joueur, String> telephoneColumn;

    @FXML
    private TableColumn<Joueur, Void> modifierColumn;

    @FXML
    private TableColumn<Joueur, Void> deleteColumn;

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
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
    private void handleAddJoueurButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutJoueur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addJoueurButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private ObservableList<Joueur> joueurList = FXCollections.observableArrayList();
    private JoueurService joueurService = new JoueurService();

    private void openModifyWindow(Joueur joueur, Stage stage) {
        try {
            // Load ModifierJoueur.fxml for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierJoueur.fxml"));
            Parent root = loader.load();

            ModifyJoueur controller = loader.getController();
            controller.setJoueurToModify(joueur);

            // Set the scene for the modification window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Assume after some action (like entering data or focus lost), you want to switch to DisplayJoueur
            stage.setOnHidden(event -> {
                try {
                    // After modification, load DisplayJoueur.fxml
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
                    Parent displayRoot = displayLoader.load();

                    // Change the scene to DisplayJoueur
                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene); // Change the scene
                    stage.show(); // Show the new scene (DisplayJoueur.fxml)
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load DisplayJoueur.fxml");
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
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());
        nomSportColumn.setCellValueFactory(cellData -> cellData.getValue().idSportProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        dateNaissanceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy").format(cellData.getValue().getDateNaissance()))
        );
        posteColumn.setCellValueFactory(cellData -> cellData.getValue().posteProperty());
        tailleColumn.setCellValueFactory(cellData -> cellData.getValue().tailleProperty().asObject());
        poidsColumn.setCellValueFactory(cellData -> cellData.getValue().poidsProperty().asObject());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telephoneColumn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());

        // Modifier column with confirmation dialog
        modifierColumn.setCellFactory(param -> new TableCell<Joueur, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setId("btn-modify"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before modification
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to modify this player?");
                    alert.setContentText("Player: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow(); // Get the stage from the button clicked
                            openModifyWindow(selectedJoueur, stage); // Pass stage directly
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
        deleteColumn.setCellFactory(param -> new TableCell<Joueur, Void>() {
            private final Button btn = new Button("Delete");

            {
                btn.setId("btn-delete"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before deletion
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this player?");
                    alert.setContentText("Player: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            joueurService.supprimer(selectedJoueur);  // Delete the player using the service
                            loadJoueurs();  // Reload the table after deletion
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
        loadJoueurs();  // Load joueurs into the table view
    }

    public void loadJoueurs() {
        joueurList.clear();
        joueurList.addAll(joueurService.rechercher());  // Fetch list of joueurs from the service
        tableView.setItems(joueurList);
    }
}