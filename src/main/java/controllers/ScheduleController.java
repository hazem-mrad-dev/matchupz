package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import models.EspaceSportif;
import models.Match;
import models.Schedule;
import services.MatchService;
import services.ScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ScheduleController {

    // Match Section
    @FXML
    private TableColumn<Match, Integer> colId;
    @FXML
    private TableColumn<Match, String> colC1, colC2, colSportType;
    @FXML
    private TextField textFieldC1, textFieldC2, textFieldSportType;
    @FXML
    private ComboBox<String> comboBoxSportType;
    @FXML
    private Button btnAjoutMatch, btnAnnulerMatch, btnActualiserMatch;
    @FXML
    private TableView<Match> tableViewMatches;
    @FXML
    private TableColumn<Match, Void> colActions;

    private final MatchService matchService = new MatchService();
    private ObservableList<Match> matchList = FXCollections.observableArrayList();
    private Match selectedMatch = null;

    // Schedule Section
    @FXML
    private TableColumn<Schedule, Integer> colIdSchedule, colIdMatchFK, colIdLieu;
    @FXML
    private TableColumn<Schedule, LocalDate> colDateMatch;
    @FXML
    private TableColumn<Schedule, LocalTime> colStartTime, colEndTime;
    @FXML
    private TableColumn<Schedule, Void> colActionsSchedule;
    @FXML
    private TableView<Schedule> tableViewSchedules;
    @FXML
    private DatePicker datePickerDateMatch;
    @FXML
    private Spinner<LocalTime> spinnerStartTime, spinnerEndTime;
    @FXML
    private TextField textFieldIdMatchFK, textFieldIdLieu;
    @FXML
    private Button btnAjoutSchedule, btnAnnulerSchedule, btnActualiserSchedule;

    private final ScheduleService scheduleService = new ScheduleService();
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
    private Schedule selectedSchedule = null;

    @FXML
    public void initialize() {
        // Match Table Initialization
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatch()).asObject());
        colC1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC1()));
        colC2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC2()));
        colSportType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSportType()));
        comboBoxSportType.setItems(FXCollections.observableArrayList(
                "Football", "Basketball", "Tennis", "Volleyball", "Handball"
        ));

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
                    handleSupprimerMatch(match);
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

        // Schedule Table Initialization
        colIdSchedule.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSchedule()).asObject());
        colDateMatch.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateMatch()));
        colStartTime.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));
        colEndTime.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndTime()));
        colIdMatchFK.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatchFK()).asObject());
        colIdLieu.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdLieu()).asObject());
        colActionsSchedule.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox container = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setOnAction(event -> {
                    Schedule schedule = getTableView().getItems().get(getIndex());
                    remplirChampsPourModificationSchedule(schedule);
                });

                btnSupprimer.setOnAction(event -> {
                    Schedule schedule = getTableView().getItems().get(getIndex());
                    handleSupprimerSchedule(schedule);
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

        tableViewSchedules.setItems(scheduleList);
        loadSchedules();

        // Initialize spinners for Schedule
        initializeTimeSpinners();

        // Event Listeners
        btnAjoutMatch.setOnAction(event -> handleAjouterOuModifierMatch());
        btnAnnulerMatch.setOnAction(event -> clearFieldsMatch());
        btnActualiserMatch.setOnAction(event -> loadMatches());

        btnAjoutSchedule.setOnAction(event -> handleAjouterOuModifierSchedule());
        btnAnnulerSchedule.setOnAction(event -> clearFieldsSchedule());
        btnActualiserSchedule.setOnAction(event -> loadSchedules());
    }

    // Match Methods
    private void loadMatches() {
        matchList.clear();
        List<Match> matches = matchService.rechercher();
        matchList.addAll(matches);
    }

    private void handleAjouterOuModifierMatch() {
        if (!validerChampsMatch()) {
            return;
        }

        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        //String sportType = textFieldSportType.getText().trim();
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
        //textFieldSportType.setText(match.getSportType());
        comboBoxSportType.setValue(match.getSportType());

    }

    private void clearFieldsMatch() {
        textFieldC1.clear();
        textFieldC2.clear();
        //textFieldSportType.clear();
        comboBoxSportType.setValue(null);
        selectedMatch = null;
    }

    private boolean validerChampsMatch() {
        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        //String sportType = textFieldSportType.getText().trim();
        String sportType = comboBoxSportType.getValue();


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

    // Schedule Methods
    private void loadSchedules() {
        scheduleList.clear();
        List<Schedule> schedules = scheduleService.rechercher();
        scheduleList.setAll(schedules); // This is similar but sometimes more reliable
        tableViewSchedules.refresh(); // Explicitly refresh the TableView
    }


    private void handleAjouterOuModifierSchedule() {
        if (!validerChampsSchedule()) {
            return;
        }

        LocalDate dateMatch = datePickerDateMatch.getValue();
        LocalTime startTime = spinnerStartTime.getValue();
        LocalTime endTime = spinnerEndTime.getValue();
        int idMatchFK = Integer.parseInt(textFieldIdMatchFK.getText().trim());
        int idLieu = Integer.parseInt(textFieldIdLieu.getText().trim());

        if (selectedSchedule == null) {
            Schedule schedule = new Schedule(dateMatch, startTime, endTime, idMatchFK, idLieu);
            scheduleService.ajouter(schedule);
            scheduleList.add(schedule);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Schedule ajouté avec succès !");
        } else {
            selectedSchedule.setDateMatch(dateMatch);
            selectedSchedule.setStartTime(startTime);
            selectedSchedule.setEndTime(endTime);
            selectedSchedule.setIdMatchFK(idMatchFK);
            selectedSchedule.setIdLieu(idLieu);
            scheduleService.modifier(selectedSchedule);
            loadSchedules();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Schedule modifié avec succès !");
        }

        clearFieldsSchedule();
    }

    private void handleSupprimerSchedule(Schedule schedule) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce schedule ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            scheduleService.supprimer(schedule);
            scheduleList.remove(schedule);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Schedule supprimé !");
        }
    }

    private void remplirChampsPourModificationSchedule(Schedule schedule) {
        selectedSchedule = schedule;
        datePickerDateMatch.setValue(schedule.getDateMatch());
        spinnerStartTime.getValueFactory().setValue(schedule.getStartTime());
        spinnerEndTime.getValueFactory().setValue(schedule.getEndTime());
        textFieldIdMatchFK.setText(String.valueOf(schedule.getIdMatchFK()));
        textFieldIdLieu.setText(String.valueOf(schedule.getIdLieu()));
    }

    private void clearFieldsSchedule() {
        datePickerDateMatch.setValue(null);
        spinnerStartTime.getValueFactory().setValue(LocalTime.now().withMinute(0).withSecond(0));
        spinnerEndTime.getValueFactory().setValue(LocalTime.now().plusHours(1).withMinute(0).withSecond(0));
        textFieldIdMatchFK.clear();
        textFieldIdLieu.clear();
        selectedSchedule = null;
    }


    private boolean validerChampsSchedule() {
        if (datePickerDateMatch.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'Date Match' est obligatoire !");
            return false;
        }

        if (spinnerStartTime.getValue() == null || spinnerEndTime.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Les champs 'Start Time' et 'End Time' sont obligatoires !");
            return false;
        }

        // Validation: End time must be after Start time
        if (spinnerEndTime.getValue().isBefore(spinnerStartTime.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'heure de fin doit être après l'heure de début !");
            return false;
        }

        if (textFieldIdMatchFK.getText().trim().isEmpty() || textFieldIdLieu.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Les champs 'ID Match' et 'ID Lieu' sont obligatoires !");
            return false;
        }

        return true;
    }


    // Utility Methods
    private void initializeTimeSpinners() {
        SpinnerValueFactory<LocalTime> startTimeFactory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int steps) {
                setValue(getValue().minusMinutes(steps * 30));
            }

            @Override
            public void increment(int steps) {
                setValue(getValue().plusMinutes(steps * 30));
            }
        };
        startTimeFactory.setValue(LocalTime.now().withMinute(0).withSecond(0));
        spinnerStartTime.setValueFactory(startTimeFactory);

        SpinnerValueFactory<LocalTime> endTimeFactory = new SpinnerValueFactory<>() {
            @Override
            public void decrement(int steps) {
                setValue(getValue().minusMinutes(steps * 30));
            }

            @Override
            public void increment(int steps) {
                setValue(getValue().plusMinutes(steps * 30));
            }
        };
        endTimeFactory.setValue(LocalTime.now().plusHours(1).withMinute(0).withSecond(0));
        spinnerEndTime.setValueFactory(endTimeFactory);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }





}

