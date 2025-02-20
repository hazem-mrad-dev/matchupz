package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EtatMateriel;
import models.Materiel;
import models.TypeMateriel;
import services.MaterielService;
import tests.MainFX;

public class AjoutMateriel {

    @FXML
    private Button btnAjout;

    @FXML
    private TextField textFieldNom, textFieldQuantite, textFieldPrix;

    @FXML
    private ComboBox<TypeMateriel> comboBoxType;

    @FXML
    private ComboBox<EtatMateriel> comboBoxEtat;

    @FXML
    private TableView<Materiel> materielTable;

    @FXML
    private TableColumn<Materiel, String> colNom, colType, colEtat;

    @FXML
    private TableColumn<Materiel, Integer> colQuantite;

    @FXML
    private TableColumn<Materiel, Float> colPrix;

    @FXML
    private TableColumn<Materiel, Void> colModifier, colSupprimer;

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();

    private MaterielService materielService = new MaterielService();

    @FXML
    void switchToFournisseur() {
        MainFX.loadScene("AjoutFournisseur.fxml");
    }

    @FXML
    void switchToMateriel() {
        MainFX.loadScene("AjoutMateriel.fxml");
    }

    @FXML
    public void initialize() {
        // Remplir les ComboBox
        comboBoxType.getItems().addAll(TypeMateriel.values());
        comboBoxEtat.getItems().addAll(EtatMateriel.values());

        // Lier les colonnes aux données
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().name()));
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().name()));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_unitaire"));

        // Ajouter boutons Modifier et Supprimer
        addActionButtons();

        // Charger les matériels depuis la base de données
        materielList.addAll(materielService.rechercher());
        materielTable.setItems(materielList);
    }

    // Ajouter Materiel
    @FXML
    void ajouterMateriel(ActionEvent event) {
        if (textFieldNom.getText().isEmpty() || textFieldQuantite.getText().isEmpty() || textFieldPrix.getText().isEmpty() ||
                comboBoxType.getValue() == null || comboBoxEtat.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
        } else {
            try {
                int quantite = Integer.parseInt(textFieldQuantite.getText());
                float prix = Float.parseFloat(textFieldPrix.getText());

                Materiel newMateriel = new Materiel(
                        1, // ID fournisseur temporaire
                        textFieldNom.getText(),
                        comboBoxType.getValue(),
                        quantite,
                        comboBoxEtat.getValue(),
                        prix
                );

                materielList.add(newMateriel);
                materielService.ajouter(newMateriel);

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Matériel ajouté avec succès !");
                clearFields();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité et prix doivent être des nombres valides !");
            }
        }
    }

    // Ajouter boutons Modifier et Supprimer
    private void addActionButtons() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btnModify = new Button("✏️");

            {
                btnModify.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    modifyMateriel(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnModify);
                }
            }
        });

        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("🗑️");

            {
                btnDelete.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    deleteMateriel(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });
    }

    // Modifier un matériel
    private void modifyMateriel(Materiel materiel) {
        textFieldNom.setText(materiel.getNom());
        textFieldQuantite.setText(String.valueOf(materiel.getQuantite()));
        textFieldPrix.setText(String.valueOf(materiel.getPrix_unitaire()));
        comboBoxType.setValue(materiel.getType());
        comboBoxEtat.setValue(materiel.getEtat());

        btnAjout.setText("Modifier");

        btnAjout.setOnAction(event -> {
            try {
                materiel.setNom(textFieldNom.getText());
                materiel.setQuantite(Integer.parseInt(textFieldQuantite.getText()));
                materiel.setPrix_unitaire(Float.parseFloat(textFieldPrix.getText()));
                materiel.setType(comboBoxType.getValue());
                materiel.setEtat(comboBoxEtat.getValue());

                materielService.modifier(materiel);
                materielTable.refresh();

                showAlert(Alert.AlertType.INFORMATION, "Modification", "Matériel modifié avec succès !");
                btnAjout.setText("Ajouter");
                clearFields();
                btnAjout.setOnAction(this::ajouterMateriel);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité et prix doivent être des nombres valides !");
            }
        });
    }

    // Supprimer un matériel
    private void deleteMateriel(Materiel materiel) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce matériel ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                materielService.supprimer(materiel);
                materielList.remove(materiel);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Matériel supprimé avec succès !");
            }
        });
    }

    // Afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Réinitialiser les champs
    private void clearFields() {
        textFieldNom.clear();
        textFieldQuantite.clear();
        textFieldPrix.clear();
        comboBoxType.setValue(null);
        comboBoxEtat.setValue(null);
    }
}
