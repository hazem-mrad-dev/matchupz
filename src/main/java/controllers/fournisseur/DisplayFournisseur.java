package controllers.fournisseur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.logistics.EtatMateriel;
import models.logistics.Fournisseur;
import models.logistics.Materiel;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.logistics.FournisseurService;
import services.logistics.MaterielService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayFournisseur {

    @FXML private javafx.scene.control.Button Home;
    @FXML private javafx.scene.control.Button fournisseurButton;
    @FXML private javafx.scene.control.Button materielButton;
    @FXML private javafx.scene.control.Button addFournisseurButton;
    @FXML private javafx.scene.control.Button addMaterielButton;
    @FXML private javafx.scene.control.Button log_out;
    @FXML private TabPane tabPane;
    @FXML private Tab fournisseurTab;
    @FXML private Tab materielTab;
    @FXML private TextField fournisseurSearchField;
    @FXML private ImageView fournisseurSearchIcon;
    @FXML private javafx.scene.control.Button exportFournisseurButton;
    @FXML private TableView<Fournisseur> tableView;
    @FXML private TableColumn<Fournisseur, String> nomColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;
    @FXML private TableColumn<Fournisseur, String> categorieProduitColumn;
    @FXML private TableColumn<Fournisseur, Void> modifierColumn;
    @FXML private TableColumn<Fournisseur, Void> deleteColumn;
    @FXML private TextField materielSearchField;
    @FXML private ImageView materielSearchIcon;
    @FXML private javafx.scene.control.Button exportMaterielButton;
    @FXML private javafx.scene.control.Button scanBarcodeButton;
    @FXML private TableView<Materiel> materielTableView;
    @FXML private TableColumn<Materiel, String> materielNomColumn;
    @FXML private TableColumn<Materiel, String> typeColumn;
    @FXML private TableColumn<Materiel, Integer> quantiteColumn;
    @FXML private TableColumn<Materiel, String> etatColumn;
    @FXML private TableColumn<Materiel, Float> prixUnitaireColumn;
    @FXML private TableColumn<Materiel, String> fournisseurNomColumn;
    @FXML private TableColumn<Materiel, String> barcodeColumn;
    @FXML private TableColumn<Materiel, Void> materielModifierColumn;
    @FXML private TableColumn<Materiel, Void> materielDeleteColumn;
    @FXML private TableColumn<Materiel, byte[]> materielImageColumn; // Column for image data

    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private FilteredList<Fournisseur> filteredFournisseurList;
    private FilteredList<Materiel> filteredMaterielList;
    private FournisseurService fournisseurService = new FournisseurService();
    private MaterielService materielService = new MaterielService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean scanning = false;

    private static final PDColor HEADER_COLOR = new PDColor(new float[]{0f / 255f, 51f / 255f, 102f / 255f}, PDDeviceRGB.INSTANCE);
    private static final PDColor ROW_COLOR_EVEN = new PDColor(new float[]{240f / 255f, 240f / 255f, 240f / 255f}, PDDeviceRGB.INSTANCE);
    private static final PDColor ROW_COLOR_ODD = new PDColor(new float[]{255f / 255f, 255f / 255f, 255f / 255f}, PDDeviceRGB.INSTANCE);
    private static final PDColor TEXT_COLOR = new PDColor(new float[]{0f / 255f, 0f / 255f, 0f / 255f}, PDDeviceRGB.INSTANCE);
    private static final PDColor WHITE_TEXT = new PDColor(new float[]{1f, 1f, 1f}, PDDeviceRGB.INSTANCE);



    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;

    @FXML
    private Button match;
    @FXML
    private Button sponsor;

    @FXML
    private Button teams;

    @FXML
    private Button bt_user;
    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                    Stage stage = (Stage) espace.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }

    @FXML
    void logistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }

    @FXML
    void match(ActionEvent event) {

    }

    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                    Stage stage = (Stage) bt_user.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }
    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {

    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger Home.fxml", e);
        }
    }

    @FXML
    private void handleFournisseur() {
        loadFournisseurs();
        // Use fournisseurButton to silence the "never accessed" warning
        System.out.println("Handling Fournisseur via button: " + fournisseurButton.getText());
    }

    @FXML
    private void handleMateriel() {
        loadMateriels();
        // Use materielButton to silence the "never accessed" warning
        System.out.println("Handling Materiel via button: " + materielButton.getText());
    }

    @FXML
    private void handleAddFournisseurButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AjoutFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addFournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger AjoutFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleAddMaterielButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AjoutMateriel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addMaterielButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger AjoutMateriel.fxml", e);
        }
    }

    private void openModifyWindow(Fournisseur fournisseur, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/modifierFournisseur.fxml"));
            Parent root = loader.load();
            ModifyFournisseur controller = loader.getController();
            controller.setFournisseurToModify(fournisseur);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadFournisseurs());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifyFournisseur.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openMaterielModifyWindow(Materiel materiel, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/ModifierMateriel.fxml"));
            Parent root = loader.load();
            ModifyMateriel controller = loader.getController();
            controller.setMaterielToModify(materiel);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadMateriels());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifyMateriel.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray n'est pas supporté sur ce système.");
        }

        // Fournisseur TableView setup
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        categorieProduitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie_produit()));

        modifierColumn.setCellFactory(param -> new TableCell<Fournisseur, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Fournisseur selectedFournisseur = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Modifier ce fournisseur ?");
                    alert.setContentText("Fournisseur : " + selectedFournisseur.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedFournisseur, stage);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }

        });

        deleteColumn.setCellFactory(param -> new TableCell<Fournisseur, Void>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Fournisseur selectedFournisseur = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer ce fournisseur ?");
                    alert.setContentText("Fournisseur : " + selectedFournisseur.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            fournisseurService.supprimer(selectedFournisseur);
                            loadFournisseurs();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Materiel TableView setup
        materielNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
        quantiteColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        prixUnitaireColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getPrix_unitaire()).asObject());
        fournisseurNomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomFournisseur()));
        barcodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBarcode()));

        // New Image Column for BLOB data
        materielImageColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImageData()));
        materielImageColumn.setCellFactory(param -> new TableCell<Materiel, byte[]>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(80); // Adjust size as needed
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] imageData, boolean empty) {
                super.updateItem(imageData, empty);
                if (empty || imageData == null) {
                    setGraphic(null); // Ne rien afficher si vide ou null
                    System.out.println("Aucune image pour cet élément : " + (getTableRow() != null && getTableRow().getItem() != null ? getTableRow().getItem().getNom() : "Inconnu"));
                } else {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
                        if (bufferedImage == null) {
                            System.err.println("Données d’image invalides ou corrompues pour : " + (getTableRow() != null && getTableRow().getItem() != null ? getTableRow().getItem().getNom() : "Inconnu"));
                            setGraphic(null); // Ne rien afficher si l’image ne peut pas être lue
                        } else {
                            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                            imageView.setImage(fxImage);
                            setGraphic(imageView);
                        }
                    } catch (IOException e) {
                        System.err.println("Erreur lors du chargement de l’image pour : " + (getTableRow() != null && getTableRow().getItem() != null ? getTableRow().getItem().getNom() : "Inconnu") + " : " + e.getMessage());
                        setGraphic(null); // Ne rien afficher en cas d’erreur
                    }
                }
            }

        });

        materielModifierColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Modifier ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openMaterielModifyWindow(selectedMateriel, stage);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        materielDeleteColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            materielService.supprimer(selectedMateriel);
                            loadMateriels();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Colorer les lignes en rouge pour les matériels ENDOMMAGE
        materielTableView.setRowFactory(tableView -> new TableRow<Materiel>() {
            @Override
            protected void updateItem(Materiel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getEtat() == EtatMateriel.ENDOMMAGE) {
                        setStyle("-fx-background-color: #ffcccc;"); // Rouge clair pour ENDOMMAGE
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Initialisation des listes et filtres
        fournisseurList.addAll(fournisseurService.recherche());
        filteredFournisseurList = new FilteredList<>(fournisseurList, p -> true);
        tableView.setItems(filteredFournisseurList);

        List<Materiel> materiels = materielService.recherche();
        for (Materiel m : materiels) {
            String fournisseurNom = fournisseurService.recherche().stream()
                    .filter(f -> f.getId_fournisseur() == m.getId_fournisseur())
                    .findFirst()
                    .map(Fournisseur::getNom)
                    .orElse("Unknown");
            m.setNomFournisseur(fournisseurNom);
            checkAndNotifyDamaged(m);
        }
        materielList.addAll(materiels);
        filteredMaterielList = new FilteredList<>(materielList, p -> true);
        materielTableView.setItems(filteredMaterielList);

        // Recherche dynamique pour Fournisseur
        fournisseurSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFournisseurList.setPredicate(fournisseur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return fournisseur.getNom().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Recherche dynamique pour Materiel
        materielSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMaterielList.setPredicate(materiel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return materiel.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        (materiel.getNomFournisseur() != null && materiel.getNomFournisseur().toLowerCase().contains(lowerCaseFilter));
            });
        });

        fournisseurTab.setText("Fournisseurs");
        materielTab.setText("Matériels");
        System.out.println("🔍 Fournisseur Tab Text: " + fournisseurTab.getText());
        System.out.println("🔍 Materiel Tab Text: " + materielTab.getText());

    }

    public void loadFournisseurs() {
        fournisseurList.clear();
        fournisseurList.addAll(fournisseurService.recherche());
        filteredFournisseurList = new FilteredList<>(fournisseurList, p -> true);
        tableView.setItems(filteredFournisseurList);
        fournisseurSearchField.clear();
    }

    public void loadMateriels() {
        materielList.clear();
        List<Materiel> materiels = materielService.recherche();
        for (Materiel m : materiels) {
            String fournisseurNom = fournisseurService.recherche().stream()
                    .filter(f -> f.getId_fournisseur() == m.getId_fournisseur())
                    .findFirst()
                    .map(Fournisseur::getNom)
                    .orElse("Unknown");
            m.setNomFournisseur(fournisseurNom);
            System.out.println("Materiel: " + m.getNom() + ", Barcode: " + m.getBarcode());
            checkAndNotifyDamaged(m);
        }
        materielList.addAll(materiels);
        filteredMaterielList = new FilteredList<>(materielList, p -> true);
        materielTableView.setItems(filteredMaterielList);
        materielSearchField.clear();
    }

    @FXML
    private void exportFournisseursToPDF() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String homeDir = System.getProperty("user.home");
            String fileName = homeDir + "/Downloads/Fournisseurs_" + timestamp + ".pdf";
            File file = new File(fileName);
            System.out.println("🔍 Saving PDF to: " + file.getAbsolutePath());

            try (PDDocument document = new PDDocument()) {
                float[] columnWidths = {100, 150, 200, 100};
                float tableWidth = 550;
                float startX = 30;
                float startY = 750;
                int itemsPerPage = 35;
                int totalItems = filteredFournisseurList.size();
                int currentItem = 0;

                while (currentItem < totalItems) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                        contentStream.setNonStrokingColor(HEADER_COLOR);
                        contentStream.newLineAtOffset(startX, startY);
                        contentStream.showText("Liste des Fournisseurs");
                        contentStream.endText();

                        float y = startY - 40;
                        contentStream.setNonStrokingColor(HEADER_COLOR);
                        drawRect(contentStream, startX, y, tableWidth, 20);
                        contentStream.setNonStrokingColor(WHITE_TEXT);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(startX + 5, y + 5);
                        contentStream.showText("Nom");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Email");
                        contentStream.newLineAtOffset(150, 0);
                        contentStream.showText("Adresse");
                        contentStream.newLineAtOffset(200, 0);
                        contentStream.showText("Catégorie");
                        contentStream.endText();
                        drawGridLines(contentStream, startX, y, columnWidths, 20, 1);

                        y -= 20;
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.setNonStrokingColor(TEXT_COLOR);
                        for (int i = currentItem; i < totalItems && i < currentItem + itemsPerPage; i++) {
                            Fournisseur f = filteredFournisseurList.get(i);
                            contentStream.setNonStrokingColor(i % 2 == 0 ? ROW_COLOR_EVEN : ROW_COLOR_ODD);
                            drawRect(contentStream, startX, y, tableWidth, 15);
                            contentStream.setNonStrokingColor(TEXT_COLOR);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(startX + 5, y + 3);
                            contentStream.showText(truncate(f.getNom(), 15));
                            contentStream.newLineAtOffset(100, 0);
                            contentStream.showText(truncate(f.getEmail(), 25));
                            contentStream.newLineAtOffset(150, 0);
                            contentStream.showText(truncate(f.getAdresse(), 30));
                            contentStream.newLineAtOffset(200, 0);
                            contentStream.showText(truncate(f.getCategorie_produit(), 15));
                            contentStream.endText();
                            drawGridLines(contentStream, startX, y, columnWidths, 15, 1);
                            y -= 15;
                        }

                        currentItem += itemsPerPage;
                    }
                }

                document.save(file);
                System.out.println("🔍 PDF saved successfully to: " + file.getAbsolutePath());
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Exportation réussie");
        } catch (IOException e) {
            showError("Erreur d'exportation", "Échec de la création du PDF", e);
        }
    }

    @FXML
    private void exportMaterielsToPDF() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String homeDir = System.getProperty("user.home");
            String fileName = homeDir + "/Downloads/Materiels_" + timestamp + ".pdf";
            File file = new File(fileName);
            System.out.println("🔍 Saving PDF to: " + file.getAbsolutePath());

            try (PDDocument document = new PDDocument()) {
                float[] columnWidths = {100, 100, 100, 80, 80, 80, 100}; // Adjusted for image column
                float tableWidth = 640; // Increased to accommodate image column
                float startX = 30;
                float startY = 750;
                int itemsPerPage = 35;
                int totalItems = filteredMaterielList.size();
                int currentItem = 0;

                while (currentItem < totalItems) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                        contentStream.setNonStrokingColor(HEADER_COLOR);
                        contentStream.newLineAtOffset(startX, startY);
                        contentStream.showText("Liste des Matériels");
                        contentStream.endText();

                        float y = startY - 40;
                        contentStream.setNonStrokingColor(HEADER_COLOR);
                        drawRect(contentStream, startX, y, tableWidth, 20);
                        contentStream.setNonStrokingColor(WHITE_TEXT);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(startX + 5, y + 5);
                        contentStream.showText("Image");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Nom");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Type");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Quantité");
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText("État");
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText("Prix");
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText("Fournisseur");
                        contentStream.endText();
                        drawGridLines(contentStream, startX, y, columnWidths, 20, 1);

                        y -= 20;
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.setNonStrokingColor(TEXT_COLOR);
                        for (int i = currentItem; i < totalItems && i < currentItem + itemsPerPage; i++) {
                            Materiel m = filteredMaterielList.get(i);
                            contentStream.setNonStrokingColor(i % 2 == 0 ? ROW_COLOR_EVEN : ROW_COLOR_ODD);
                            drawRect(contentStream, startX, y, tableWidth, 15);
                            contentStream.setNonStrokingColor(TEXT_COLOR);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(startX + 5, y + 3);
                            contentStream.showText(m.getImageData() != null ? "Image" : "Aucune image");
                            contentStream.newLineAtOffset(100, 0);
                            contentStream.showText(truncate(m.getNom(), 15));
                            contentStream.newLineAtOffset(100, 0);
                            contentStream.showText(truncate(m.getType().toString(), 15));
                            contentStream.newLineAtOffset(100, 0);
                            contentStream.showText(String.valueOf(m.getQuantite()));
                            contentStream.newLineAtOffset(80, 0);
                            contentStream.showText(truncate(m.getEtat().toString(), 10));
                            contentStream.newLineAtOffset(80, 0);
                            contentStream.showText(String.format("%.2f", m.getPrix_unitaire()));
                            contentStream.newLineAtOffset(80, 0);
                            contentStream.showText(truncate(m.getNomFournisseur() != null ? m.getNomFournisseur() : "Unknown", 15));
                            contentStream.endText();
                            drawGridLines(contentStream, startX, y, columnWidths, 15, 1);
                            y -= 15;
                        }

                        currentItem += itemsPerPage;
                    }
                }

                document.save(file);
                System.out.println("🔍 PDF saved successfully to: " + file.getAbsolutePath());
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Exportation réussie");
        } catch (IOException e) {
            showError("Erreur d'exportation", "Échec de la création du PDF", e);
        }
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }

    private void drawRect(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        contentStream.addRect(x, y, width, height);
        contentStream.fill();
    }

    private void drawGridLines(PDPageContentStream contentStream, float x, float y, float[] columnWidths, float height, int rows) throws IOException {
        contentStream.setLineWidth(0.5f);
        contentStream.setStrokingColor(TEXT_COLOR);
        float totalWidth = 0;
        for (float width : columnWidths) totalWidth += width;

        for (int i = 0; i <= rows; i++) {
            contentStream.moveTo(x, y + i * height);
            contentStream.lineTo(x + totalWidth, y + i * height);
            contentStream.stroke();
        }

        float currentX = x;
        for (float width : columnWidths) {
            contentStream.moveTo(currentX, y);
            contentStream.lineTo(currentX, y + height);
            contentStream.stroke();
            currentX += width;
        }
        contentStream.moveTo(currentX, y);
        contentStream.lineTo(currentX, y + height);
        contentStream.stroke();
    }

    @FXML
    private void log_out(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    private void checkAndNotifyDamaged(Materiel materiel) {
        if (materiel.getEtat() == EtatMateriel.ENDOMMAGE && !materiel.isRepairRequested()) {
            if (SystemTray.isSupported()) {
                try {
                    SystemTray tray = SystemTray.getSystemTray();
                    java.awt.Image image = Toolkit.getDefaultToolkit().createImage("src/main/resources/images/icon.png");
                    TrayIcon trayIcon = new TrayIcon(image, "Gestion Logistique");
                    trayIcon.setImageAutoSize(true);
                    tray.add(trayIcon);
                    trayIcon.displayMessage(
                            "Matériel Endommagé",
                            "Le matériel '" + materiel.getNom() + "' est endommagé. Une action est requise.",
                            TrayIcon.MessageType.WARNING
                    );
                    tray.remove(trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
            materiel.setRepairRequested(true);
        }
    }

    @FXML
    private void scanBarcode() {
        if (scanning) {
            scanning = false;
            showAlert(Alert.AlertType.INFORMATION, "Scan Arrêté", "Le scan a été arrêté.");
            return;
        }

        scanning = true;
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune webcam détectée.");
            scanning = false;
            return;
        }

        // Créer la fenêtre de scan
        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scan Code-Barres");

        // Vue caméra
        ImageView webcamView = new ImageView();
        webcamView.setFitWidth(640);
        webcamView.setFitHeight(480);

        // Rectangle de focus centré et large (presque la largeur de la fenêtre)
        double rectWidth = 600;  // Largeur presque égale à la fenêtre (640 pixels)
        double rectHeight = 200; // Hauteur augmentée pour capturer à distance
        Rectangle focusRect = new Rectangle(
                (640 - rectWidth) / 2,  // Centre horizontal
                (480 - rectHeight) / 2,  // Centre vertical
                rectWidth, rectHeight
        );
        focusRect.setFill(null);
        focusRect.setStroke(Color.GREEN);
        focusRect.setStrokeWidth(2);
        focusRect.getStrokeDashArray().addAll(10.0, 10.0); // Effet pointillé pour une apparence moderne

        // Animation de balayage (ligne horizontale dans le rectangle)
        Rectangle scanLine = new Rectangle(
                focusRect.getX(), focusRect.getY(),
                rectWidth, 2
        );
        scanLine.setFill(Color.RED);
        scanLine.setStroke(Color.RED);

        Timeline scanAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, e -> scanLine.setY(focusRect.getY())),
                new KeyFrame(Duration.seconds(2), e -> scanLine.setY(focusRect.getY() + rectHeight - 2))
        );
        scanAnimation.setCycleCount(Timeline.INDEFINITE);
        scanAnimation.setAutoReverse(true);
        scanAnimation.play();

        // Label pour les messages d’état
        Label statusLabel = new Label("Positionnez le code-barres dans le cadre...");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Bouton d’arrêt
        javafx.scene.control.Button stopButton = new javafx.scene.control.Button("Arrêter le Scan");
        stopButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        stopButton.setOnAction(e -> {
            scanning = false;
            scanStage.close();
        });

        // Mise en page avec superposition
        VBox vbox = new VBox(10.0, webcamView, statusLabel, stopButton);
        vbox.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 10;");

        // Pane pour superposer la caméra, le cadre et la ligne
        Pane overlayPane = new Pane(webcamView, focusRect, scanLine);
        overlayPane.setPrefSize(640.0, 480.0);

        // Scène avec le conteneur principal
        VBox mainVBox = new VBox(10.0, overlayPane, statusLabel, stopButton);
        mainVBox.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 10;");
        Scene scanScene = new Scene(mainVBox, 640, 600);
        scanStage.setScene(scanScene);

        webcam.open();
        scanStage.show();

        executor.submit(() -> {
            MultiFormatReader reader = new MultiFormatReader();
            while (scanning) {
                try {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        Image fxImage = SwingFXUtils.toFXImage(image, null);
                        Platform.runLater(() -> webcamView.setImage(fxImage));

                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        Result result = reader.decode(bitmap);
                        if (result != null) {
                            String barcode = result.getText();
                            Platform.runLater(() -> {
                                Materiel materiel = materielService.findByBarcode(barcode);
                                if (materiel != null) {
                                    statusLabel.setText("Matériel trouvé : " + materiel.getNom());
                                    statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                                    materielTableView.getSelectionModel().select(materiel);
                                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Matériel trouvé : " + materiel.getNom());
                                } else {
                                    statusLabel.setText("Aucun matériel trouvé pour : " + barcode);
                                    statusLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 14px;");
                                    showAlert(Alert.AlertType.WARNING, "Matériel Inconnu", "Aucun matériel trouvé pour le code-barres: " + barcode);
                                }
                                scanAnimation.stop();
                                new Timeline(new KeyFrame(Duration.seconds(3), e -> scanStage.close())).play(); // Délai de 3 secondes avant fermeture
                            });
                            scanning = false;
                            break;
                        }
                    }
                    Thread.sleep(50);
                } catch (NotFoundException e) {
                    // Pas de code-barres détecté encore
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        statusLabel.setText("Erreur : " + e.getMessage());
                        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                        showAlert(Alert.AlertType.ERROR, "Erreur de Scan", "Erreur: " + e.getMessage());
                        scanAnimation.stop();
                        new Timeline(new KeyFrame(Duration.seconds(2), ev -> scanStage.close())).play();
                    });
                    scanning = false;
                    break;
                }
            }
            webcam.close();
            if (!scanning) {
                Platform.runLater(scanStage::close);
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("Détails : " + e.getMessage());
        alert.showAndWait();
    }
}