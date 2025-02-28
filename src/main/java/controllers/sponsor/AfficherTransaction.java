package controllers.sponsor;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Contrat;
import models.Transaction;
import services.sponsor.TransactionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AfficherTransaction {

    @FXML
    private Button btnSupprimerTransaction, btnActualiserTransaction, btnModifier;

    @FXML
    private TableView<Transaction> tableViewTransactions;

    @FXML
    private TableColumn<Transaction, String> colType, colDate, colDescription;

    @FXML
    private TableColumn<Transaction, Float> colMontant;

    @FXML
    private TableColumn<Transaction, Void> colActions;

    private final TransactionService transactionService = new TransactionService();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        colMontant.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getSommeArgent()).asObject());
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));


        tableViewTransactions.setItems(transactionList);
        addActionsColumn();
        loadTransactions();

    }

    private void addActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Transaction, Void> call(TableColumn<Transaction, Void> param) {
                return new TableCell<Transaction, Void>() {
                    private final Button btnModifier = new Button("Modifier");
                    private final Button btnSupprimer = new Button("Supprimer");

                    {
                        btnModifier.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        btnSupprimer.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsBox = new HBox(btnModifier, btnSupprimer);
                            buttonsBox.setSpacing(5);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        });
        }


        private void handleEdit(Transaction transaction) {
        if (transaction == null) return;
        System.out.println("Modifier " + transaction.getType());
    }

    private void handleDelete(Transaction transaction) {
        if (transaction == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la transaction");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + transaction.getType() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                transactionService.supprimer(transaction); // Suppression en base de données
                transactionList.remove(transaction); // Supprime l'élément de la liste observable
                tableViewTransactions.refresh(); // Rafraîchir la TableView après suppression
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Transaction supprimée avec succès !");
            }
        });
    }




    private void loadTransactions() {
        transactionList.clear();
        List<Transaction> transactions = transactionService.recherche();
        transactionList.addAll(transactions);
    }


    @FXML
    private void refreshList() {
        loadTransactions();
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTransaction.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Transaction");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AjouterTransaction.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
