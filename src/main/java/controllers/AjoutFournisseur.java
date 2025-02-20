package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Fournisseur;
import services.FournisseurService;
import tests.MainFX;

public class AjoutFournisseur {

    @FXML
    private Button btnAjout;

    @FXML
    private TextField textFieldNom, textFieldEmail, textFieldAdresse;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private TableView<Fournisseur> fournisseurTable;

    @FXML
    private TableColumn<Fournisseur, String> colNom, colEmail, colAdresse, colCategorie;

    @FXML
    private TableColumn<Fournisseur, Void> colModifier, colSupprimer;

    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();

    private FournisseurService fournisseurService = new FournisseurService();

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
        // Remplir les catégories
        comboBoxCategorie.getItems().addAll(
                "EQUIPEMENT_SPORTIF", "ACCESSOIRE_ENTRAINEMENT", "MATERIEL_JEU",
                "TENUE_SPORTIVE", "EQUIPEMENT_PROTECTION", "INFRASTRUCTURE"
        );
        comboBoxCategorie.setValue("EQUIPEMENT_SPORTIF");

        // Lier les colonnes aux données
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie_produit()));

        // Ajouter boutons Modifier et Supprimer
        addActionButtons();

        // Charger les fournisseurs depuis la base de données
        fournisseurList.addAll(fournisseurService.rechercher());
        fournisseurTable.setItems(fournisseurList);
    }

    // Ajouter Fournisseur
    @FXML
    void ajouterFournisseur(ActionEvent event) {
        if (textFieldNom.getText().isEmpty() || textFieldEmail.getText().isEmpty() ||
                textFieldAdresse.getText().isEmpty() || comboBoxCategorie.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
        } else {
            Fournisseur newFournisseur = new Fournisseur(
                    textFieldNom.getText(), textFieldEmail.getText(),
                    textFieldAdresse.getText(), comboBoxCategorie.getValue()
            );

            fournisseurList.add(newFournisseur);
            fournisseurService.ajouter(newFournisseur);

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Fournisseur ajouté avec succès !");
            clearFields();
        }
    }

    // Ajouter boutons Modifier et Supprimer
    private void addActionButtons() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btnModify = new Button("✏️");

            {
                btnModify.setOnAction(event -> {
                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    modifyFournisseur(fournisseur);
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
                    Fournisseur fournisseur = getTableView().getItems().get(getIndex());
                    deleteFournisseur(fournisseur);
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

    // Modifier un fournisseur
    private void modifyFournisseur(Fournisseur fournisseur) {
        textFieldNom.setText(fournisseur.getNom());
        textFieldEmail.setText(fournisseur.getEmail());
        textFieldAdresse.setText(fournisseur.getAdresse());
        comboBoxCategorie.setValue(fournisseur.getCategorie_produit());

        btnAjout.setText("Modifier");

        btnAjout.setOnAction(event -> {
            fournisseur.setNom(textFieldNom.getText());
            fournisseur.setEmail(textFieldEmail.getText());
            fournisseur.setAdresse(textFieldAdresse.getText());
            fournisseur.setCategorie_produit(comboBoxCategorie.getValue());

            fournisseurService.modifier(fournisseur);
            fournisseurTable.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Modification", "Fournisseur modifié avec succès !");
            btnAjout.setText("Ajouter");
            clearFields();
            btnAjout.setOnAction(this::ajouterFournisseur);
        });
    }

    // Supprimer un fournisseur
    private void deleteFournisseur(Fournisseur fournisseur) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce fournisseur ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                fournisseurService.supprimer(fournisseur);
                fournisseurList.remove(fournisseur);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Fournisseur supprimé avec succès !");
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
        textFieldEmail.clear();
        textFieldAdresse.clear();
        comboBoxCategorie.setValue("EQUIPEMENT_SPORTIF");
    }

}
