package controllers.sponsor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Sponsor;
import services.SponsorService;

import java.io.IOException;
import java.util.List;

public class AfficherSponsor {

    @FXML
    private Button Home;

    @FXML
    private Button annulerButton;

    @FXML
    private Button addSponsorButton;

    @FXML
    private TableView<Sponsor> tableViewSponsors;

    @FXML
    private TableColumn<Sponsor, String> colNom;

    @FXML
    private TableColumn<Sponsor, String> colContact;

    @FXML
    private TableColumn<Sponsor, String> colPack;

    @FXML
    private TableColumn<Sponsor, Void> modifierColumn, deleteColumn; // Changed to Void since buttons don't need values

    private final SponsorService sponsorService = new SponsorService();
    private final ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colPack.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPack()));

        addActionsColumn();
        loadSponsors();
    }

    private void addActionsColumn() {
        modifierColumn.setCellFactory(new Callback<TableColumn<Sponsor, Void>, TableCell<Sponsor, Void>>() {
            @Override
            public TableCell<Sponsor, Void> call(TableColumn<Sponsor, Void> param) {
                return new TableCell<Sponsor, Void>() {
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

    @FXML
    private void addSponsor() throws IOException {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSponsor.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) addSponsorButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Ajouter un sponsor");
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

    private void loadSponsors() {
        sponsorList.clear(); // Prevent duplication
        List<Sponsor> sponsors = sponsorService.recherche();
        sponsorList.addAll(sponsors);
        tableViewSponsors.setItems(sponsorList);
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(e);
        }
    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(e);
        }
    }


    private void handleEdit(Sponsor sponsor) {
        // Confirmation dialog before modification
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to modify this sponsor?");
        alert.setContentText("Sponsor: " + sponsor.getNom());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Get the current Stage from the button that was clicked
                Stage stage = (Stage) tableViewSponsors.getScene().getWindow(); // Stage from TableView
                openModifyWindow(sponsor, stage); // Pass stage directly to open the modify window
            }
        });
    }



    private void openModifyWindow(Sponsor sponsor, Stage stage) {
        try {
            // Load ModifierJoueur.fxml for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSponsor.fxml"));
            Parent root = loader.load();

            controllers.ModifierSponsor controller = loader.getController();
            controller.setSponsorToModify(sponsor);

            // Set the scene for the modification window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Assume after some action (like entering data or focus lost), you want to switch to DisplayJoueur
            stage.setOnHidden(event -> {
                try {
                    // After modification, load DisplayJoueur.fxml
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("AfficherSponsor.fxml"));
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



    private void handleDelete(Sponsor sponsor) {
        if (sponsor == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le sponsor");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + sponsor.getNom() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                sponsorService.supprimer(sponsor);
                loadSponsors();
            }
        });
    }

    private void showErrorAlert(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to load the FXML file");
        alert.setContentText("Details: " + e.getMessage());
        alert.showAndWait();
    }

    @FXML
    private void refreshList() {
        loadSponsors();
    }
}
