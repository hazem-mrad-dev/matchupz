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
import models.joueur.Club;
import models.joueur.HistoriqueClub;
import services.joueur.ClubService;
import services.joueur.HistoriqueClubService;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class DisplayHistorique {
    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addHistoriqueButton;
    @FXML private Button searchButton; // Added for search functionality
    @FXML private TextField searchField; // Added for search functionality
    @FXML private TableView<HistoriqueClub> tableView;
    @FXML private TableColumn<HistoriqueClub, Integer> idHistoriqueColumn;
    @FXML private TableColumn<HistoriqueClub, Integer> idJoueurColumn;
    @FXML private TableColumn<HistoriqueClub, String> nomClubColumn;
    @FXML private TableColumn<HistoriqueClub, String> saisonDebutColumn;
    @FXML private TableColumn<HistoriqueClub, String> saisonFinColumn;
    @FXML private TableColumn<HistoriqueClub, Void> modifierColumn;
    @FXML private TableColumn<HistoriqueClub, Void> deleteColumn;

    private ObservableList<HistoriqueClub> historiqueList = FXCollections.observableArrayList();
    private HistoriqueClubService historiqueClubService = new HistoriqueClubService();
    private ClubService clubService = new ClubService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", homeButton);
    }

    @FXML
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleAddHistoriqueButton() {
        loadScene("/joueur/AjoutHistorique.fxml", addHistoriqueButton);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the FXML file", "Details: " + e.getMessage());
        }
    }

    private void openModifyWindow(HistoriqueClub historique, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierHistorique.fxml"));
            Parent root = loader.load();
            ModifyHistorique controller = loader.getController();
            controller.setHistoriqueToModify(historique);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadHistorique());
        } catch (IOException e) {
            showAlert("Error", "Failed to load ModifyHistorique.fxml", "Details: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        idHistoriqueColumn.setCellValueFactory(cellData -> cellData.getValue().idHistoriqueProperty().asObject());
        idJoueurColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());

        // Dynamically fetch nomClub based on idClub
        nomClubColumn.setCellValueFactory(cellData -> {
            int idClub = cellData.getValue().getIdClub();
            String nomClub = clubService.recherche().stream()
                    .filter(club -> club.getIdClub() == idClub)
                    .map(Club::getNomClub)
                    .findFirst()
                    .orElse("Unknown Club");
            return new SimpleStringProperty(nomClub);
        });

        // Format dates for display
        saisonDebutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                dateFormat.format(cellData.getValue().getSaisonDebut())
        ));
        saisonFinColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getSaisonFin() != null ? dateFormat.format(cellData.getValue().getSaisonFin()) : ""
        ));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    HistoriqueClub selectedHistorique = getTableView().getItems().get(getIndex());
                    String nomClub = clubService.recherche().stream()
                            .filter(club -> club.getIdClub() == selectedHistorique.getIdClub())
                            .map(Club::getNomClub)
                            .findFirst()
                            .orElse("Unknown Club");
                    showConfirmation("Confirmation", "Modifier cet historique ?", "Club: " + nomClub, () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedHistorique, stage);
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
                    HistoriqueClub selectedHistorique = getTableView().getItems().get(getIndex());
                    String nomClub = clubService.recherche().stream()
                            .filter(club -> club.getIdClub() == selectedHistorique.getIdClub())
                            .map(Club::getNomClub)
                            .findFirst()
                            .orElse("Unknown Club");
                    showConfirmation("Confirmation", "Supprimer cet historique ?", "Club: " + nomClub, () -> {
                        historiqueClubService.supprimer(selectedHistorique);
                        loadHistorique();
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

        // Real-time search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterHistorique(newValue));

        loadHistorique();
    }

    public void loadHistorique() {
        historiqueList.clear();
        historiqueList.addAll(historiqueClubService.recherche());
        tableView.setItems(historiqueList);
    }

    @FXML
    private void handleSearch() {
        filterHistorique(searchField.getText());
    }

    private void filterHistorique(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(historiqueList);
        } else {
            ObservableList<HistoriqueClub> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (HistoriqueClub historique : historiqueList) {
                String nomClub = clubService.recherche().stream()
                        .filter(club -> club.getIdClub() == historique.getIdClub())
                        .map(Club::getNomClub)
                        .findFirst()
                        .orElse("Unknown Club").toLowerCase();
                String saisonFin = historique.getSaisonFin() != null ? dateFormat.format(historique.getSaisonFin()) : "";
                if (String.valueOf(historique.getIdJoueur()).contains(lowerCaseFilter) ||
                        nomClub.contains(lowerCaseFilter) ||
                        dateFormat.format(historique.getSaisonDebut()).contains(lowerCaseFilter) ||
                        saisonFin.contains(lowerCaseFilter)) {
                    filteredList.add(historique);
                }
            }
            tableView.setItems(filteredList);
        }
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