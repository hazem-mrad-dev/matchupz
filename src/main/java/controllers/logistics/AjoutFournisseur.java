package controllers.logistics;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.logistics.Fournisseur;
import services.logistics.FournisseurService;

import java.io.IOException;

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
    private AnchorPane mainContainer; // This should be the root container in the FXML file

    @FXML
    void switchToFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutFournisseur.fxml"));
            AnchorPane newView = loader.load();

            // Replace the content inside mainContainer with the new FXML view
            mainContainer.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void switchToMateriel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutMateriel.fxml"));
            AnchorPane newView = loader.load();

            // Replace the content inside mainContainer with the new FXML view
            mainContainer.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        fournisseurList.addAll(fournisseurService.recherche());
        fournisseurTable.setItems(fournisseurList);
    }

    // Ajouter Fournisseur
    @FXML
    void ajouterFournisseur(ActionEvent event) {
        // Vérifier si les champs sont vides
        if (textFieldNom.getText().trim().isEmpty() || textFieldEmail.getText().trim().isEmpty() ||
                textFieldAdresse.getText().trim().isEmpty() || comboBoxCategorie.getValue() == null) {

            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que le nom contient uniquement des lettres et des espaces
        if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
            return;
        }

        // Vérifier que l'email est valide
        if (!textFieldEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email est invalide ! Utilisez un format correct (ex: exemple@mail.com).");
            return;
        }

        // Vérifier que l'adresse est suffisamment longue
        if (textFieldAdresse.getText().length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'adresse doit contenir au moins 5 caractères !");
            return;
        }

        // Si tout est valide, ajouter le fournisseur
        Fournisseur newFournisseur = new Fournisseur(
                textFieldNom.getText().trim(),
                textFieldEmail.getText().trim(),
                textFieldAdresse.getText().trim(),
                comboBoxCategorie.getValue()
        );

        fournisseurList.add(newFournisseur);
        fournisseurService.ajouter(newFournisseur);

        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Fournisseur ajouté avec succès !");
        clearFields();
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


    private void modifyFournisseur(Fournisseur fournisseur) {
        textFieldNom.setText(fournisseur.getNom());
        textFieldEmail.setText(fournisseur.getEmail());
        textFieldAdresse.setText(fournisseur.getAdresse());
        comboBoxCategorie.setValue(fournisseur.getCategorie_produit());

        btnAjout.setText("Modifier");

        btnAjout.setOnAction(event -> {
            // Vérification des champs avant modification
            if (textFieldNom.getText().trim().isEmpty() || textFieldEmail.getText().trim().isEmpty() ||
                    textFieldAdresse.getText().trim().isEmpty() || comboBoxCategorie.getValue() == null) {

                showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
                return;
            }

            // Vérifier que le nom contient uniquement des lettres et des espaces
            if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
                return;
            }

            // Vérifier que l'email est valide
            if (!textFieldEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'email est invalide ! Utilisez un format correct (ex: exemple@mail.com).");
                return;
            }

            // Vérifier que l'adresse est suffisamment longue
            if (textFieldAdresse.getText().length() < 5) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'adresse doit contenir au moins 5 caractères !");
                return;
            }

            // Mettre à jour les informations du fournisseur
            fournisseur.setNom(textFieldNom.getText().trim());
            fournisseur.setEmail(textFieldEmail.getText().trim());
            fournisseur.setAdresse(textFieldAdresse.getText().trim());
            fournisseur.setCategorie_produit(comboBoxCategorie.getValue());

            // Appliquer la modification
            fournisseurService.modifier(fournisseur);
            fournisseurTable.refresh();

            // Afficher un message de confirmation
            showAlert(Alert.AlertType.INFORMATION, "Modification", "Fournisseur modifié avec succès !");

            // Réinitialiser le bouton et le formulaire
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
