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

public class ScheduleController {

    @FXML
    private ComboBox<String> comboBoxSportType;
    @FXML
    private TextField textFieldC1, textFieldC2;
    @FXML
    private Button btnAjoutMatch, btnAnnulerMatch, btnActualiserMatch;
    @FXML
    private TableView<Match> tableViewMatches;
    @FXML
    private TableColumn<Match, Integer> colId;
    @FXML
    private TableColumn<Match, String> colC1, colC2, colSportType;
    @FXML
    private TableColumn<Match, Void> colActions;

    private final MatchService matchService = new MatchService();
    private ObservableList<Match> matchList = FXCollections.observableArrayList();
    private Match selectedMatch = null;

    @FXML
    public void initialize() {
        comboBoxSportType.setItems(FXCollections.observableArrayList(
                "Football", "Basketball", "Tennis", "Volleyball", "Handball", "Boxing", "Wrestling"
        ));

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatch()).asObject());
        colC1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC1()));
        colC2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC2()));
        colSportType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSportType()));
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox container = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setOnAction(event -> remplirChampsPourModification(getTableView().getItems().get(getIndex())));
                btnSupprimer.setOnAction(event -> handleSupprimerMatch(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        tableViewMatches.setItems(matchList);
        loadMatches();

        btnAjoutMatch.setOnAction(event -> handleAjouterOuModifierMatch());
        btnAnnulerMatch.setOnAction(event -> clearFieldsMatch());
        btnActualiserMatch.setOnAction(event -> loadMatches());
    }

    private void loadMatches() {
        matchList.clear();
        matchList.addAll(matchService.rechercher());
    }

    private void handleAjouterOuModifierMatch() {
        if (!validerChampsMatch()) return;

        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        String sportType = comboBoxSportType.getValue();

        if (selectedMatch == null) {
            Match match = new Match(c1, c2, sportType);
            matchService.ajouter(match);
            matchList.add(match);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");
        } else {
            selectedMatch.setC1(c1);
            selectedMatch.setC2(c2);
            selectedMatch.setSportType(sportType);
            matchService.modifier(selectedMatch);
            loadMatches();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match modifié avec succès !");
        }

        clearFieldsMatch();
    }

    private void handleSupprimerMatch(Match match) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
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
        comboBoxSportType.setValue(match.getSportType());
    }

    private void clearFieldsMatch() {
        textFieldC1.clear();
        textFieldC2.clear();
        comboBoxSportType.setValue(null);
        selectedMatch = null;
    }

    private boolean validerChampsMatch() {
        if (textFieldC1.getText().trim().isEmpty() || textFieldC2.getText().trim().isEmpty() || comboBoxSportType.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
