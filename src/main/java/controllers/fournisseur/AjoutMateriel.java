package controllers.fournisseur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.logistics.Fournisseur;
import models.logistics.Materiel;
import models.logistics.TypeMateriel;
import models.logistics.EtatMateriel;
import services.logistics.FournisseurService;
import services.logistics.MaterielService;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AjoutMateriel {

    @FXML private Button annulerButton;
    @FXML private Button btnAjout;
    @FXML private TextField textFieldNom, textFieldQuantite, textFieldPrixUnitaire, barcodeField; // Added barcodeField
    @FXML private ComboBox<String> comboBoxType, comboBoxEtat, comboBoxFournisseur;
    @FXML private Button scanButton; // Added scanButton
    @FXML private Button uploadImageButton; // Button for uploading image

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private MaterielService materielService = new MaterielService();
    private FournisseurService fournisseurService = new FournisseurService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean scanning = false;
    private Materiel materielToAdd; // Field to hold the Materiel being added

    @FXML
    public void initialize() {
        System.out.println("Méthode initialize() exécutée pour AjoutMateriel !");
        if (comboBoxType == null || comboBoxEtat == null || comboBoxFournisseur == null ||
                barcodeField == null || scanButton == null || uploadImageButton == null) {
            System.err.println("Problème d'injection FXML ! Vérifiez les IDs dans le fichier FXML.");
            return;
        }

        comboBoxType.getItems().addAll(
                "EQUIPEMENT_SPORTIF",
                "ACCESSOIRE_ENTRAINEMENT",
                "MATERIEL_JEU",
                "TENUE_SPORTIVE",
                "EQUIPEMENT_PROTECTION",
                "INFRASTRUCTURE"
        );
        comboBoxType.setValue("MATERIEL_JEU");

        comboBoxEtat.getItems().addAll("NEUF", "USE", "ENDOMMAGE");
        comboBoxEtat.setValue("NEUF");

        updateFournisseurComboBox("MATERIEL_JEU");

        comboBoxType.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateFournisseurComboBox(newValue);
            }
        });

        materielList.addAll(materielService.recherche());
        System.out.println("Matériels chargés : " + materielList.size());

        // Initialize materielToAdd as a new Materiel object
        materielToAdd = new Materiel(0, "", TypeMateriel.MATERIEL_JEU, 0, EtatMateriel.NEUF, 0.0f, ""); // Using the 7-parameter constructor
    }

    private void updateFournisseurComboBox(String typeMateriel) {
        comboBoxFournisseur.getItems().clear();
        List<Fournisseur> matchingFournisseurs = fournisseurService.getFournisseursByCategory(typeMateriel);
        if (matchingFournisseurs.isEmpty()) {
            comboBoxFournisseur.getItems().add("Aucun fournisseur disponible");
            comboBoxFournisseur.setValue("Aucun fournisseur disponible");
        } else {
            for (Fournisseur f : matchingFournisseurs) {
                comboBoxFournisseur.getItems().add(f.getNom());
            }
            comboBoxFournisseur.setValue(matchingFournisseurs.get(0).getNom());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de DisplayFournisseur.fxml : " + e.getMessage());
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (textFieldNom.getText().trim().isEmpty() || textFieldQuantite.getText().trim().isEmpty() ||
                textFieldPrixUnitaire.getText().trim().isEmpty() || barcodeField.getText().trim().isEmpty() ||
                comboBoxType.getValue() == null || comboBoxEtat.getValue() == null || comboBoxFournisseur.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(textFieldQuantite.getText());
            if (quantite < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un entier positif !");
            return;
        }

        float prixUnitaire;
        try {
            prixUnitaire = Float.parseFloat(textFieldPrixUnitaire.getText());
            if (prixUnitaire < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix unitaire doit être un nombre positif !");
            return;
        }

        String nom = textFieldNom.getText().trim();
        String type = comboBoxType.getValue();

        if (materielService.exists(nom, type)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Matériel déjà existant");
            return;
        }

        String selectedFournisseurNom = comboBoxFournisseur.getValue();
        int idFournisseur = fournisseurService.getFournisseursByCategory(type)
                .stream()
                .filter(f -> f.getNom().equals(selectedFournisseurNom))
                .findFirst()
                .map(Fournisseur::getId_fournisseur)
                .orElse(1);

        materielToAdd.setId_fournisseur(idFournisseur);
        materielToAdd.setNom(nom);
        materielToAdd.setType(TypeMateriel.valueOf(type));
        materielToAdd.setQuantite(quantite);
        materielToAdd.setEtat(EtatMateriel.valueOf(comboBoxEtat.getValue()));
        materielToAdd.setPrix_unitaire(prixUnitaire);
        materielToAdd.setBarcode(barcodeField.getText());

        materielList.add(materielToAdd);
        materielService.ajouter(materielToAdd);

        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Matériel ajouté avec succès !");
        clearFields();
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

        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scan Code-Barres");

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
        Button stopButton = new Button("Arrêter le Scan");
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
                                barcodeField.setText(barcode);
                                statusLabel.setText("Code-barres détecté : " + barcode);
                                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
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

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        // Use the current stage or create a new one safely
        Stage stage = (Stage) uploadImageButton.getScene().getWindow();
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                byte[] imageData = imageToBytes(imageFile);
                materielToAdd.setImageData(imageData); // Set the image data for the new material
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image téléchargée avec succès");
            } catch (IOException e) {
                showError("Erreur", "Échec du téléchargement de l'image", e);
            }
        }
    }

    private byte[] imageToBytes(File imageFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        // Redimensionner l'image si nécessaire (par exemple, à 80x80 pixels)
        BufferedImage resizedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, 80, 80, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos); // Format fixe pour éviter des problèmes
        return baos.toByteArray();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
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

    private void clearFields() {
        textFieldNom.clear();
        textFieldQuantite.clear();
        textFieldPrixUnitaire.clear();
        barcodeField.clear();
        comboBoxType.setValue("MATERIEL_JEU");
        comboBoxEtat.setValue("NEUF");
        updateFournisseurComboBox("MATERIEL_JEU");
    }
}