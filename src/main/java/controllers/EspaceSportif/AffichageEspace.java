package controllers.EspaceSportif;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.util.List;

public class AffichageEspace {

    @FXML
    private TableView<EspaceSportif> tableView;

    @FXML
    private TableColumn<EspaceSportif, Integer> colId;

    @FXML
    private TableColumn<EspaceSportif, String> colNom;

    @FXML
    private TableColumn<EspaceSportif, String> colAdresse;

    @FXML
    private TableColumn<EspaceSportif, String> colCategorie;

    @FXML
    private TableColumn<EspaceSportif, Float> colCapacite;

    @FXML
    private TableColumn<EspaceSportif, String> colActions;

    @FXML
    private TableColumn<EspaceSportif, String> colClimat;

    @FXML
    private TableColumn<EspaceSportif, String> colMap;

    @FXML
    private TextField searchField;

    private final EspaceSportifService espaceService;

    public AffichageEspace() {
        this.espaceService = new EspaceSportifService();
    }

    @FXML
    public void initialize() {
        if (tableView == null || colNom == null || colClimat == null || colMap == null) {
            System.err.println("⚠ Erreur : Vérifie ton fichier FXML ! Les éléments n'ont pas été correctement injectés.");
            return;
        }

        // Configuration des colonnes existantes
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdLieu()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());

        // Colonne pour afficher un bouton "Climat" qui ouvre une alerte
        colClimat.setCellValueFactory(cellData -> new SimpleStringProperty("Climat"));
        colClimat.setCellFactory(param -> new TableCell<>() {
            final Button button = new Button("Climat");

            {
                button.setOnAction(event -> {
                    EspaceSportif espace = getTableRow().getItem();
                    if (espace != null) {
                        showClimateDetails(espace);
                    }
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });

        // Colonne pour afficher un bouton "Carte" qui ouvre une carte dans le navigateur
        colMap.setCellValueFactory(cellData -> new SimpleStringProperty("Carte"));
        colMap.setCellFactory(param -> new TableCell<>() {
            final Button button = new Button("Carte");

            {
                button.setOnAction(event -> {
                    EspaceSportif espace = getTableRow().getItem();
                    if (espace != null) {
                        showMap(espace);
                    }
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });

        // Colonne Actions (modifier/supprimer)
        addActionsColumn();
        loadEspaces();

        // Listener pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchEspaceSportif(newValue));
    }

    private void loadEspaces() {
        List<EspaceSportif> espaces = espaceService.rechercher();
        if (espaces == null || espaces.isEmpty()) {
            System.err.println("⚠ Aucun espace sportif trouvé !");
            tableView.setItems(FXCollections.observableArrayList());
        } else {
            tableView.setItems(FXCollections.observableArrayList(espaces));
        }
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button("Modifier");
            final Button deleteButton = new Button("Supprimer");
            final HBox hBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void handleEdit(EspaceSportif espace) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEspace.fxml"));
            AnchorPane modifLayout = loader.load();
            ModifierEspace controller = loader.getController();
            controller.setEspaceToEdit(espace);

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.setTitle("Modifier un espace sportif");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de ModifierEspace.fxml");
        }
    }

    private void handleDelete(EspaceSportif espace) {
        if (espace == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'espace sportif");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + espace.getNomEspace() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                espaceService.supprimer(espace);
                loadEspaces();
            }
        });
    }

    @FXML
    private void addEspace() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEspace.fxml"));
            AnchorPane ajoutLayout = loader.load();

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(ajoutLayout));
            newStage.setTitle("Ajouter un espace sportif");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterEspace.fxml");
        }
    }

    @FXML
    private void refreshList() {
        loadEspaces();
    }

    private void searchEspaceSportif(String motCle) {
        List<EspaceSportif> resultatRecherche = espaceService.rechercherParMotCle(motCle.toLowerCase());
        tableView.setItems(FXCollections.observableArrayList(resultatRecherche));
    }

    @FXML
    private void goToEspaceSportif(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
        AnchorPane espaceSportifLayout = loader.load();
        Scene scene = new Scene(espaceSportifLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageReservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene reservationScene = new Scene(reservationLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(reservationScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de Reservation.fxml");
        }
    }

    @FXML
    public void goToAbonnement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des Abonnements.fxml");
        }
    }

    // Méthode pour afficher les détails climatiques dans une alerte
    private void showClimateDetails(EspaceSportif espace) {
        try {
            // Use the address as provided, expecting it to include city and country (e.g., "Tunis, Tunisie")
            String address = espace.getAdresse();
            if (!address.contains(",")) {
                address += ", Tunisie"; // Default to Tunisia if no country is specified
            }
            double[] coords = espaceService.getCoordonnes(address);
            if (coords != null) {
                String climat = espaceService.getClimat(coords[0], coords[1]);
                String mapUrl = espaceService.getMapUrl(coords[0], coords[1]);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Détails climatiques et emplacement");
                alert.setHeaderText("Informations pour " + espace.getNomEspace());
                alert.setContentText(
                        "Climat : " + climat + "\n" +
                                "Emplacement : Cliquez sur le lien pour voir la carte\n" +
                                "[Carte](" + mapUrl + ")"
                );

                Hyperlink mapLink = new Hyperlink("Ouvrir la carte");
                mapLink.setOnAction(e -> {
                    try {
                        Desktop.getDesktop().browse(URI.create(mapUrl));
                    } catch (IOException e1) {
                        System.err.println("Erreur lors de l'ouverture de la carte : " + e1.getMessage());
                    }
                });

                VBox content = new VBox(10, new Label("Climat : " + climat), mapLink);
                alert.getDialogPane().setContent(content);
                alert.showAndWait();
            } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Impossible de récupérer les coordonnées pour l'adresse : " + address);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données climatiques : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Format d'adresse incorrect : " + e.getMessage());
        }
    }

    private void showMap(EspaceSportif espace) {
        try {
            // Use the address as provided, expecting it to include city and country (e.g., "Tunis, Tunisie")
            String address = espace.getAdresse();
            if (!address.contains(",")) {
                address += ", Tunisie"; // Default to Tunisia if no country is specified
            }
            double[] coords = espaceService.getCoordonnes(address);
            if (coords != null) {
                String mapUrl = espaceService.getMapUrl(coords[0], coords[1]);
                Desktop.getDesktop().browse(URI.create(mapUrl));
            } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Impossible de récupérer les coordonnées pour l'adresse : " + address);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture de la carte : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Format d'adresse incorrect : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleExportEspaces() {
        ObservableList<EspaceSportif> espaces = tableView.getItems();
        if (espaces.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Espace", "Il n'y a aucun espace sportif à exporter !");
            return;
        }

        // Create a FileChooser to let the user select the save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des espaces sportifs");
        fileChooser.setInitialFileName("EspacesSportifs_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Show save dialog and get the selected file
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) {
            showAlert(Alert.AlertType.INFORMATION, "Annulé", "L'exportation a été annulée.");
            return; // User canceled the dialog
        }

        String fileName = file.getAbsolutePath();

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Espaces Sportifs", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"ID", "Nom", "Adresse", "Catégorie", "Capacité"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (EspaceSportif espace : espaces) {
                pdfTable.addCell(String.valueOf(espace.getIdLieu()));
                pdfTable.addCell(espace.getNomEspace());
                pdfTable.addCell(espace.getAdresse());
                pdfTable.addCell(espace.getCategorie());
                pdfTable.addCell(String.valueOf(espace.getCapacite()));
                pdfTable.addCell("N/A"); // Replace with actual climat data if available via espaceService
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Espaces Sportifs");
            footer1.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer1);

            Paragraph footer2 = new Paragraph("Nexus Team 2025 ©");
            footer2.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer2);

            document.add(new Paragraph(" "));
            InputStream inputStream = getClass().getResourceAsStream("/images/logo_horizantalDARK.jpg");
            if (inputStream == null) {
                throw new IOException("Image resource not found: /images/logo_horizantalDARK.jpg");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            byte[] imageBytes = baos.toByteArray();
            Image logo = Image.getInstance(imageBytes);
            logo.scaleToFit(184, 41);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les espaces sportifs ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des espaces sportifs : " + e.getMessage());
            e.printStackTrace();
        }
    }
}