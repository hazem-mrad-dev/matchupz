package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.user.UserService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class SingUpController {

    @FXML
    private Button button_se_connecter;

    @FXML
    private Button button_sinscrire;

    @FXML
    private ComboBox<String> combo_role;

    @FXML
    private DatePicker date_date_naiss;

    @FXML
    private RadioButton rb_femme;

    @FXML
    private RadioButton rb_homme;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_image;

    @FXML
    private TextField tf_mot_de_passe;

    @FXML
    private TextField tf_nom;


    @FXML
    private TextField tf_prenom;


    @FXML
    private ImageView image;
    @FXML
    private TextField tf_tel;

    private ToggleGroup toggleGroup;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Z][a-zA-Z ]*");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{8}$");

    @FXML
    private void initialize() {
        image.setImage(new Image("file:/C:/Users/MSI/Desktop/image.png"));

        toggleGroup = new ToggleGroup();
        rb_homme.setToggleGroup(toggleGroup);
        rb_femme.setToggleGroup(toggleGroup);


        for (Role role : Role.values()) {
            combo_role.getItems().add(role.getValue());
        }


        tf_image.setEditable(false);
        tf_image.setOnMouseClicked(event -> choisirImage());
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            tf_image.setText(file.toURI().toString());
        }
    }

    @FXML
    void seconnecter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) button_se_connecter.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    public void sinscrire() {
        try {
            if (tf_nom.getText().isEmpty() || tf_prenom.getText().isEmpty() || tf_email.getText().isEmpty()
                    || tf_mot_de_passe.getText().isEmpty() || tf_tel.getText().isEmpty() || date_date_naiss.getValue() == null
                    || combo_role.getValue() == null || toggleGroup.getSelectedToggle() == null || tf_image.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
                return;
            }

            if (!NAME_PATTERN.matcher(tf_nom.getText()).matches()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit commencer par une majuscule et contenir uniquement des lettres.");
                return;
            }

            if (!NAME_PATTERN.matcher(tf_prenom.getText()).matches()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom doit commencer par une majuscule et contenir uniquement des lettres.");
                return;
            }

            if (!EMAIL_PATTERN.matcher(tf_email.getText()).matches()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'email doit être sous la forme exemple@gmail.com");
                return;
            }

            if (!PHONE_PATTERN.matcher(tf_tel.getText()).matches()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
                return;
            }


            String genre = rb_homme.isSelected() ? "Homme" : "Femme";
           LocalDate dateNaissance = date_date_naiss.getValue();
            Role role = Role.fromString(combo_role.getValue());
            int tel = Integer.parseInt(tf_tel.getText());

            User user = new User(
                    tf_nom.getText(),
                    tf_prenom.getText(),
                    tf_email.getText(),
                    tf_mot_de_passe.getText(),
                    tel,
                    dateNaissance,
                    genre,
                    role,
                    tf_image.getText()
            );

            UserService ps = new UserService();
            ps.ajouter(user);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
                Stage stage = (Stage) button_sinscrire.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur inscrit avec succès !");
            resetFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void resetFields() {
        tf_nom.clear();
        tf_prenom.clear();
        tf_email.clear();
        tf_mot_de_passe.clear();
        tf_tel.clear();
        tf_image.clear();
        date_date_naiss.setValue(null);
        combo_role.setValue(Role.UTILISATEUR.name());
        toggleGroup.selectToggle(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}


