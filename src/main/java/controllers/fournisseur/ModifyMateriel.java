package controllers.fournisseur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.event.ActionEvent;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class ModifyMateriel {

    @FXML private TextField nomField;
    @FXML private TextField quantiteField;
    @FXML private TextField prixUnitaireField;
    @FXML private TextField barcodeField;
    @FXML private ComboBox<String> comboBoxType;
    @FXML private ComboBox<String> comboBoxEtat;
    @FXML private ComboBox<String> comboBoxFournisseur;
    @FXML private Button modifierButton;
    @FXML private Button annulerButton;
    @FXML private Button materielButton;
    @FXML private Button Home;
    @FXML private Button scanButton;
    @FXML private Button uploadImageButton; // Bouton pour uploader l'image
    @FXML private ImageView imageView;      // ImageView pour afficher l'image actuelle

    private Materiel materielToModify;
    private FournisseurService fournisseurService = new FournisseurService();
    private MaterielService materielService = new MaterielService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean scanning = false;

    @FXML
    public void initialize() {
        System.out.println("✅ ModifyMateriel : initialize() exécuté !");
        System.out.println("🔍 comboBoxType = " + comboBoxType);
        if (barcodeField == null) {
            System.err.println("❌ barcodeField est null dans initialize ! Vérifiez le fichier FXML.");
        }
        if (scanButton == null) {
            System.err.println("❌ scanButton est null dans initialize ! Vérifiez le fichier FXML.");
        }
        if (uploadImageButton == null) {
            System.err.println("❌ uploadImageButton est null dans initialize ! Vérifiez le fichier FXML.");
        }
        if (imageView == null) {
            System.err.println("❌ imageView est null dans initialize ! Vérifiez le fichier FXML.");
        }

        if (comboBoxType == null || comboBoxEtat == null || comboBoxFournisseur == null) {
            System.out.println("⚠️ ERREUR : Problème d'injection FXML !");
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

        comboBoxEtat.getItems().addAll("NEUF", "USE", "ENDOMMAGE");

        comboBoxType.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateFournisseurComboBox(newValue);
            }
        });

        if (materielToModify != null) {
            setMaterielToModify(materielToModify);
        }
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
            if (materielToModify != null && materielToModify.getNomFournisseur() != null &&
                    matchingFournisseurs.stream().anyMatch(f -> f.getNom().equals(materielToModify.getNomFournisseur()))) {
                comboBoxFournisseur.setValue(materielToModify.getNomFournisseur());
            } else {
                comboBoxFournisseur.setValue(matchingFournisseurs.get(0).getNom());
            }
        }
    }

    public void setMaterielToModify(Materiel materiel) {
        this.materielToModify = materiel;
        nomField.setText(materiel.getNom());
        quantiteField.setText(String.valueOf(materiel.getQuantite()));
        prixUnitaireField.setText(String.valueOf(materiel.getPrix_unitaire()));
        barcodeField.setText(materiel.getBarcode() != null ? materiel.getBarcode() : "");
        comboBoxType.setValue(materiel.getType().toString());
        comboBoxEtat.setValue(materiel.getEtat().toString());

        // Charger l'image existante si elle existe
        if (materiel.getImageData() != null) {
            try {
                Image image = new Image(new ByteArrayInputStream(materiel.getImageData()));
                imageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image existante : " + e.getMessage());
            }
        }

        String fournisseurNom = fournisseurService.recherche().stream()
                .filter(f -> f.getId_fournisseur() == materiel.getId_fournisseur())
                .findFirst()
                .map(Fournisseur::getNom)
                .orElse("Unknown");
        materielToModify.setNomFournisseur(fournisseurNom);

        updateFournisseurComboBox(materiel.getType().toString());

        System.out.println("✅ Matériel chargé : " + materiel.getNom());
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
    private void handleMateriel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) materielButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleAnnulerButton() {
        System.out.println("🔍 handleAnnulerButton triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find /fournisseur/DisplayFournisseur.fxml");
            }
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            DisplayFournisseur controller = loader.getController();
            controller.loadFournisseurs();
            controller.loadMateriels();
            stage.show();
            System.out.println("🔍 Returned to DisplayFournisseur.fxml");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de DisplayFournisseur.fxml : " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) uploadImageButton.getScene().getWindow();
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                if (bufferedImage == null) {
                    throw new IOException("Le fichier sélectionné n’est pas une image valide ou n’est pas pris en charge. Assurez-vous que c’est un fichier .jpg, .png ou .jpeg.");
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos); // Convertir en JPG pour uniformité
                byte[] imageData = baos.toByteArray();
                System.out.println("Taille de l’image uploadée : " + imageData.length + " octets");
                materielToModify.setImageData(imageData);

                // Afficher l’image dans l’ImageView
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image téléchargée avec succès");
            } catch (IOException e) {
                System.err.println("Erreur lors du téléchargement de l’image : " + e.getMessage());
                showError("Erreur", "Échec du téléchargement de l’image : " + e.getMessage(), e);
            }
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() || quantiteField.getText().trim().isEmpty() ||
                prixUnitaireField.getText().trim().isEmpty() || barcodeField.getText().trim().isEmpty() ||
                comboBoxType.getValue() == null || comboBoxEtat.getValue() == null || comboBoxFournisseur.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs obligatoires manquants");
            return;
        }

        if (!nomField.getText().matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Nom invalide");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(quantiteField.getText());
            if (quantite < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité invalide");
            return;
        }

        float prixUnitaire;
        try {
            prixUnitaire = Float.parseFloat(prixUnitaireField.getText());
            if (prixUnitaire < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Prix unitaire invalide");
            return;
        }

        String selectedFournisseurNom = comboBoxFournisseur.getValue();
        int idFournisseur = fournisseurService.getFournisseursByCategory(comboBoxType.getValue())
                .stream()
                .filter(f -> f.getNom().equals(selectedFournisseurNom))
                .findFirst()
                .map(Fournisseur::getId_fournisseur)
                .orElse(materielToModify.getId_fournisseur());

        materielToModify.setNom(nomField.getText().trim());
        materielToModify.setType(TypeMateriel.valueOf(comboBoxType.getValue()));
        materielToModify.setQuantite(quantite);
        EtatMateriel newEtat = EtatMateriel.valueOf(comboBoxEtat.getValue());
        if (materielToModify.getEtat() == EtatMateriel.ENDOMMAGE && newEtat != EtatMateriel.ENDOMMAGE) {
            materielToModify.setRepairRequested(false);
        }
        materielToModify.setEtat(newEtat);
        materielToModify.setPrix_unitaire(prixUnitaire);
        materielToModify.setBarcode(barcodeField.getText().trim());
        materielToModify.setId_fournisseur(idFournisseur);
        materielToModify.setNomFournisseur(selectedFournisseurNom);
        // Les données de l'image sont déjà mises à jour via handleUploadImage

        try {
            materielService.modifier(materielToModify);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification réussie");
            handleAnnulerButton();
        } catch (Exception e) {
            showError("Erreur lors de la modification", "Échec de la mise à jour dans la base de données", e);
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

        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scan Code-Barres");

        ImageView webcamView = new ImageView();
        webcamView.setFitWidth(640);
        webcamView.setFitHeight(480);

        double rectWidth = 600;
        double rectHeight = 200;
        Rectangle focusRect = new Rectangle(
                (640 - rectWidth) / 2,
                (480 - rectHeight) / 2,
                rectWidth, rectHeight
        );
        focusRect.setFill(null);
        focusRect.setStroke(Color.GREEN);
        focusRect.setStrokeWidth(2);
        focusRect.getStrokeDashArray().addAll(10.0, 10.0);

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

        Label statusLabel = new Label("Positionnez le code-barres dans le cadre...");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button stopButton = new Button("Arrêter le Scan");
        stopButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        stopButton.setOnAction(e -> {
            scanning = false;
            scanStage.close();
        });

        VBox vbox = new VBox(10.0, webcamView, statusLabel, stopButton);
        vbox.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 10;");

        Pane overlayPane = new Pane(webcamView, focusRect, scanLine);
        overlayPane.setPrefSize(640.0, 480.0);

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
                                new Timeline(new KeyFrame(Duration.seconds(1), e -> scanStage.close())).play();
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