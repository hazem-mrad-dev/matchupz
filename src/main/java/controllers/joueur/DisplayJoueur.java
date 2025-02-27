package controllers.joueur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.joueur.Joueur;
import services.joueur.JoueurService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayJoueur {

    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addJoueurButton;
    @FXML private Button trackPlayersButton;
    @FXML private TableView<Joueur> tableView;
    @FXML private TableColumn<Joueur, Integer> idColumn;
    @FXML private TableColumn<Joueur, Integer> idSportColumn;
    @FXML private TableColumn<Joueur, String> nomSportColumn;
    @FXML private TableColumn<Joueur, String> nomColumn;
    @FXML private TableColumn<Joueur, String> prenomColumn;
    @FXML private TableColumn<Joueur, String> dateNaissanceColumn;
    @FXML private TableColumn<Joueur, String> posteColumn;
    @FXML private TableColumn<Joueur, Float> tailleColumn;
    @FXML private TableColumn<Joueur, Float> poidsColumn;
    @FXML private TableColumn<Joueur, String> statutColumn;
    @FXML private TableColumn<Joueur, String> emailColumn;
    @FXML private TableColumn<Joueur, String> telephoneColumn;
    @FXML private TableColumn<Joueur, String> profilePictureColumn;
    @FXML private TableColumn<Joueur, Void> modifierColumn;
    @FXML private TableColumn<Joueur, Void> deleteColumn;

    private ObservableList<Joueur> joueurList = FXCollections.observableArrayList();
    private JoueurService joueurService = new JoueurService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleAddJoueurButton() {
        loadScene("/joueur/AjoutJoueur.fxml", addJoueurButton);
    }

    @FXML
    private void handleTrackPlayers() {
        loadScene("/joueur/TrackPlayers.fxml", trackPlayersButton);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", e.getMessage());
        }
    }

    private void openModifyWindow(Joueur joueur, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierJoueur.fxml"));
            Parent root = loader.load();
            ModifyJoueur controller = loader.getController();
            controller.setJoueurToModify(joueur);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadJoueurs());
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idJoueur"));
        idSportColumn.setCellValueFactory(new PropertyValueFactory<>("idSport"));
        nomSportColumn.setCellValueFactory(new PropertyValueFactory<>("nomSport"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(dateFormat.format(cellData.getValue().getDateNaissance())));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        tailleColumn.setCellValueFactory(new PropertyValueFactory<>("taille"));
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("poids"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        profilePictureColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(item));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("profilePictureUrl"));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Modifier ce joueur ?", "Joueur: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom(), () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedJoueur, stage);
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Supprimer ce joueur ?", "Joueur: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom(), () -> {
                        joueurService.supprimer(selectedJoueur);
                        loadJoueurs();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadJoueurs();
    }

    public void loadJoueurs() {
        joueurList.clear();
        joueurList.addAll(joueurService.recherche());
        tableView.setItems(joueurList);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String header, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}