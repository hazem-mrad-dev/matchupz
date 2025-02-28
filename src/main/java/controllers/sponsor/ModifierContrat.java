package controllers.sponsor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Contrat;
import models.Sponsor;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.ContratService;
import services.SponsorService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ModifierContrat {

    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;
    @FXML
    private Label nom_user;
    private void afficherProfil(User user) {

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            // profileImageView.setImage(image);

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
    private Button btnAjouterContrat;

    @FXML
    private Button btnSupprimerContrat;

    @FXML
    private DatePicker dp_date_debut;

    @FXML
    private DatePicker dp_date_fin;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField signature;

    @FXML
    private TextField tx_contrat_montant;

    @FXML
    private ComboBox<Sponsor> tx_contrat_sponsor;

    @FXML
    private TextField tx_contrat_titre;

    private Contrat contratToModify;
    private final ContratService contratService = new ContratService();
    private final SponsorService sponsorService = new SponsorService();
    private ObservableList<Contrat> contratList = FXCollections.observableArrayList();
    private ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();

    // Define the date formatter once for reuse
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setContratToModify(Contrat contrat) {
        this.contratToModify = contrat;
        if (contrat != null) {
            tx_contrat_titre.setText(contrat.getTitre());
            tx_contrat_montant.setText(String.valueOf(contrat.getMontant()));
            dp_date_debut.setValue(LocalDate.parse(contrat.getDateDebut(), DATE_FORMATTER));
            dp_date_fin.setValue(LocalDate.parse(contrat.getDateFin(), DATE_FORMATTER));
            // Pre-selection of sponsor will be handled in initialize()
        }
    }


    @FXML
    void handleAnnulerButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherContrat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAjouterContrat.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des contrats.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'accueil.");
            e.printStackTrace();
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        // Vérification initiale des champs vides
        if (tx_contrat_titre.getText().trim().isEmpty() ||
                tx_contrat_montant.getText().trim().isEmpty() ||
                dp_date_debut.getValue() == null ||
                dp_date_fin.getValue() == null) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Veuillez remplir tous les champs avant de modifier un contrat.");
            return;
        }

        try {
            String newTitre = tx_contrat_titre.getText().trim();
            float montant = Float.parseFloat(tx_contrat_montant.getText().trim());
            LocalDate dateDebut = dp_date_debut.getValue();
            LocalDate dateFin = dp_date_fin.getValue();

            // Validation du format du titre (lettres et espaces uniquement)
            if (!newTitre.matches("[a-zA-Z\\s]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre doit contenir uniquement des lettres.");
                return;
            }

            // Validation de la logique des dates
            if (!dateFin.isAfter(dateDebut)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin doit être postérieure à la date de début.");
                return;
            }

            // Validation du montant (doit être positif)
            if (montant <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être supérieur à 0.");
                return;
            }

            // Vérification de l'unicité du titre si modifié
            String originalTitre = contratToModify.getTitre();
            if (!newTitre.equals(originalTitre)) {
                if (contratService.isContratTitreExists(newTitre)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Un contrat avec ce titre existe déjà.");
                    return;
                }
            }

            // Mise à jour de l'objet contratToModify
            String dateDebutStr = dateDebut.format(DATE_FORMATTER);
            String dateFinStr = dateFin.format(DATE_FORMATTER);
            contratToModify.setTitre(newTitre);
            contratToModify.setMontant(montant);
            contratToModify.setDateDebut(dateDebutStr);
            contratToModify.setDateFin(dateFinStr);

            // Appel du service pour modifier
            contratService.modifier(contratToModify);

            // Affichage du message de succès et retour à la liste
            showAlert(Alert.AlertType.INFORMATION,
                    "Succès",
                    "Les informations du contrat ont été modifiées avec succès.");
            handleAnnulerButton(event);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Le montant doit être un nombre valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Set up the DatePicker converters
        dp_date_debut.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, DATE_FORMATTER) : null;
            }
        });

        dp_date_fin.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, DATE_FORMATTER) : null;
            }
        });

        // Load sponsors into ComboBox
        if (tx_contrat_sponsor != null) {
            loadSponsors();
            if (sponsorList.isEmpty()) {
                System.out.println("Warning: No sponsors loaded into ComboBox.");
            }
            tx_contrat_sponsor.setItems(sponsorList);
            tx_contrat_sponsor.setPromptText("Sponsor bloqué");

            // Configure ComboBox to display sponsor names
            tx_contrat_sponsor.setCellFactory(param -> new ListCell<Sponsor>() {
                @Override
                protected void updateItem(Sponsor item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNom());
                }
            });

            tx_contrat_sponsor.setConverter(new StringConverter<Sponsor>() {
                @Override
                public String toString(Sponsor sponsor) {
                    return sponsor == null ? "" : sponsor.getNom();
                }

                @Override
                public Sponsor fromString(String string) {
                    return sponsorList.stream()
                            .filter(s -> s.getNom().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

            // Pre-select the sponsor for the contract being modified
            if (contratToModify != null) {
                Sponsor selectedSponsor = sponsorList.stream()
                        .filter(s -> s.getId_sponsor() == contratToModify.getId_sponsor())
                        .findFirst()
                        .orElse(null);
                if (selectedSponsor != null) {
                    tx_contrat_sponsor.setValue(selectedSponsor);
                } else {
                    System.out.println("No matching sponsor found for ID: " + contratToModify.getId_sponsor());
                }
            }

            // Disable the ComboBox to prevent modification
            tx_contrat_sponsor.setDisable(true);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadContrats() {
        contratList.clear();
        contratList.addAll(contratService.recherche());
    }

    private void loadSponsors() {
        sponsorList.clear();
        try {
            List<Sponsor> sponsors = sponsorService.recherche();
            sponsorList.addAll(sponsors);
            if (sponsors.isEmpty()) {
                System.out.println("No sponsors found in the database.");
            }
        } catch (Exception e) {
            System.out.println("Error loading sponsors: " + e.getMessage());
            e.printStackTrace();
        }
    }
}