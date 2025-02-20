package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Match;
import services.MatchService;

import java.util.List;
import java.util.Optional;

public class AjouterMatch {

    @FXML
    private TableColumn<Match, Integer> colId;

    @FXML
    private TableColumn<Match, String> colC1, colC2, colSportType;

    @FXML
    private TextField textFieldC1, textFieldC2, textFieldSportType;

    @FXML
    private Button btnAjout, btnAnnuler, btnActualiser;

    @FXML
    private TableView<Match> tableViewMatches;

    @FXML
    private TableColumn<Match, Void> colActions;

    private final MatchService matchService = new MatchService();
    private ObservableList<Match> matchList = FXCollections.observableArrayList();
    private Match selectedMatch = null;

    @FXML
    public void initialize() {
        // Set up column bindings
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatch()).asObject());
        colC1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC1()));
        colC2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC2()));
        colSportType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSportType()));

        // Ajouter une colonne d'actions
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox container = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.getStyleClass().add("edit-button");
                btnSupprimer.getStyleClass().add("delete-button");

                btnModifier.setOnAction(event -> {
                    Match match = getTableView().getItems().get(getIndex());
                    remplirChampsPourModification(match);
                });

                btnSupprimer.setOnAction(event -> {
                    Match match = getTableView().getItems().get(getIndex());
                    handleSupprimer(match);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        tableViewMatches.setItems(matchList);
        loadMatches();

        // Event Listeners
        btnAjout.setOnAction(event -> handleAjouterOuModifier());
        btnAnnuler.setOnAction(event -> clearFields());
        btnActualiser.setOnAction(event -> loadMatches());
    }

    private void loadMatches() {
        matchList.clear();
        List<Match> matches = matchService.rechercher();  // Retrieve matches from database or service
        matchList.addAll(matches);
    }

    private void handleAjouterOuModifier() {
        if (!validerChamps()) {
            return;
        }

        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        String sportType = textFieldSportType.getText().trim();

        if (selectedMatch == null) {
            // Ajout d'un nouveau match
            Match match = new Match(c1, c2, sportType);
            matchService.ajouter(match);
            matchList.add(match);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
        } else {
            // Modification d'un match existant
            selectedMatch.setC1(c1);
            selectedMatch.setC2(c2);
            selectedMatch.setSportType(sportType);
            matchService.modifier(selectedMatch);
            loadMatches();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match modifié avec succès !");
        }

        clearFields();
    }

    private void handleSupprimer(Match match) {
        // Demande de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce match ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            matchService.supprimer(match);
            matchList.remove(match);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match supprimé !");
        }
    }

    private void remplirChampsPourModification(Match match) {
        selectedMatch = match;
        textFieldC1.setText(match.getC1());
        textFieldC2.setText(match.getC2());
        textFieldSportType.setText(match.getSportType());
    }

    private void clearFields() {
        textFieldC1.clear();
        textFieldC2.clear();
        textFieldSportType.clear();
        selectedMatch = null;
    }

    /**
     * Contrôle de saisie des champs
     */
    private boolean validerChamps() {
        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        String sportType = textFieldSportType.getText().trim();

        if (c1.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'C1' est obligatoire !");
            return false;
        }

        if (c2.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'C2' est obligatoire !");
            return false;
        }

        if (sportType.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'SportType' est obligatoire !");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
