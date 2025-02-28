package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.user.UserService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserAddController {

    @FXML
    private Button button_ajout_user;

    @FXML
    private Button button_annuler_user;

    @FXML
    private Button sponsor;

    @FXML
    private Button teams;
    @FXML
    private Button match;

    @FXML
    private ImageView icon4;

    @FXML
    private ImageView icon5;

    @FXML
    private ImageView icon6;

    @FXML
    private ImageView icon7;

    @FXML
    private Button logistique;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView back;

    @FXML
    private ImageView icon3;


    @FXML
    private Button dashboard;

    @FXML
    private ImageView Home;
    @FXML
    private Label nom_user;

    @FXML
    private Button espace;


    @FXML
    private ComboBox<String> combo_role;

    @FXML
    private DatePicker date_date_naiss;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private RadioButton rb_femme;

    @FXML
    private RadioButton rb_homme;

    @FXML
    private TextField tf_email;


    @FXML
    private TextField tf_image;

    @FXML
    private PasswordField tf_mot_de_passe;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_prenom;

    @FXML
    private TextField tf_tel;

    @FXML
    private Button bt_user;
    private ToggleGroup toggleGroup;

    private void afficherProfil(User user) {

        if (user.getImage() != null && !user.getImage().isEmpty()) {

            String name = user.getPrenom();
            nom_user.setText(name);


        }
    }
    @FXML
    private void initialize() {


        User user = SessionManager.getCurrentUser();

        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }

        toggleGroup = new ToggleGroup();
        rb_homme.setToggleGroup(toggleGroup);
        rb_femme.setToggleGroup(toggleGroup);

        for (Role role : Role.values()) {
            combo_role.getItems().add(role.getValue());
        }

        tf_image.setEditable(false);
        tf_image.setOnMouseClicked(event -> choisirImage());
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            if (!validerChamps()) return;

            String genre = rb_homme.isSelected() ? "Homme" : "Femme";
            LocalDate dateNaissance = date_date_naiss.getValue();
            if (dateNaissance == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de naissance.");
                return;
            }

            Role role = Role.fromString(combo_role.getValue());
            if (role == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un rôle valide.");
                return;
            }

            int tel;
            try {
                tel = Integer.parseInt(tf_tel.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre valide.");
                return;
            }

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

            new UserService().ajouter(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Utilisateur inscrit avec succès !");
            resetFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private boolean validerChamps() {
        if (tf_nom.getText().isEmpty() || tf_prenom.getText().isEmpty() || tf_email.getText().isEmpty()
                || tf_mot_de_passe.getText().isEmpty() || tf_tel.getText().isEmpty() || date_date_naiss.getValue() == null
                || combo_role.getValue() == null || toggleGroup.getSelectedToggle() == null || tf_image.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return false;
        }

        if (!Pattern.matches("[A-Z][a-zA-Z ]*", tf_nom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit commencer par une majuscule et ne contenir que des lettres et espaces !");
            return false;
        }

        if (!Pattern.matches("[A-Z][a-zA-Z ]*", tf_prenom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom doit commencer par une majuscule et ne contenir que des lettres et espaces !");
            return false;
        }

        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$", tf_email.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email doit être sous la forme exemple@gmail.com");
            return false;
        }

        if (!Pattern.matches("\\d{8}", tf_tel.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }
        if (date_date_naiss.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date de naissance.");
            return false;
        }

        if (!date_date_naiss.getValue().isBefore(LocalDate.now()) && !date_date_naiss.getValue().isEqual(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de naissance ne peut pas être dans le futur !");
            return false;
        }



        return true;
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

    @FXML
    void annuler(ActionEvent event) {
        resetFields();

    }

    @FXML
    void pageuser(ActionEvent event) {
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



    @FXML
    void login(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }


    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {

    }



    @FXML
    void logistique(ActionEvent event) {

    }

    @FXML
    void match(ActionEvent event) {

    }



    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {

    }
    @FXML
    void back(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }


    @FXML
    void Home(MouseEvent event) {

    }
}
