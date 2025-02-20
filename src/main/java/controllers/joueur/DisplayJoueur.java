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
    private void handleAddJoueurButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutJoueur.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) addJoueurButton.getScene().getWindow();

            // Set the new scene
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

    private void openModifyWindow(Joueur joueur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierJoueur.fxml"));
            Parent root = loader.load();

            ModifyJoueur controller = loader.getController();
            controller.setJoueurToModify(joueur);

            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier Joueur");
            modifyStage.setScene(new Scene(root));

            // Add a listener to reload the table when the modify window is closed
            modifyStage.setOnHidden(event -> loadJoueurs());

            modifyStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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

        // Set up action for the Modifier column
        modifierColumn.setCellFactory(param -> new TableCell<Joueur, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setId("btn-modify"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    openModifyWindow(selectedJoueur); // Open the ModifierJoueur.fxml in a new window
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });


// Set up action for the Delete column
        deleteColumn.setCellFactory(param -> new TableCell<Joueur, Void>() {
            private final Button btn = new Button("Delete");

            {
                btn.setId("btn-delete"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    joueurService.supprimer(selectedJoueur);  // Delete the player using the service
                    loadJoueurs();  // Reload the table after deletion
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