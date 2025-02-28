package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.user.UserService;
import java.io.File;
import java.util.regex.Pattern;

public class UpdateUtilisateurController {

    @FXML
    private Button button_annuler_user, button_modifier_user, button_retour;

    @FXML
    private ComboBox<String> combo_role;

    @FXML
    private DatePicker date_date_naiss;

    @FXML
    private RadioButton rb_femme, rb_homme;

    @FXML
    private TextField tf_email, tf_image, tf_mot_de_passe, tf_nom, tf_prenom, tf_tel;

    private User currentUser;
    private ToggleGroup toggleGroup;

    public void setUserToModify(User user) {
        this.currentUser = user;


        tf_nom.setText(user.getNom());
        tf_prenom.setText(user.getPrenom());
        tf_email.setText(user.getEmail());
        tf_mot_de_passe.setText(user.getPassword());
        tf_tel.setText(String.valueOf(user.getNum_telephone()));
        tf_image.setText(user.getImage());
        date_date_naiss.setValue(user.getDate_de_naissance());

        if ("Homme".equals(user.getGenre())) {
            rb_homme.setSelected(true);
        } else {
            rb_femme.setSelected(true);
        }


        combo_role.setValue(user.getRole().getValue());
    }


    public void initData(User user) {
        this.currentUser = user;
        tf_nom.setText(user.getNom());
        tf_prenom.setText(user.getPrenom());
        tf_email.setText(user.getEmail());
        tf_mot_de_passe.setText(user.getPassword());
        tf_tel.setText(String.valueOf(user.getNum_telephone()));
        tf_image.setText(user.getImage());
        date_date_naiss.setValue(user.getDate_de_naissance());
        if ("Homme".equals(user.getGenre())) rb_homme.setSelected(true);
        else rb_femme.setSelected(true);
        combo_role.setValue(user.getRole().getValue());
    }

    @FXML
    private void initialize() {
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
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            tf_image.setText(file.toURI().toString());
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        if (!validerSaisie()) return;

        currentUser.setNom(tf_nom.getText());
        currentUser.setPrenom(tf_prenom.getText());
        currentUser.setEmail(tf_email.getText());
        currentUser.setPassword(tf_mot_de_passe.getText());
        currentUser.setNum_telephone(Integer.parseInt(tf_tel.getText()));
        currentUser.setImage(tf_image.getText());
        currentUser.setDate_de_naissance(date_date_naiss.getValue());
        currentUser.setGenre(rb_homme.isSelected() ? "Homme" : "Femme");
        currentUser.setRole(Role.fromString(combo_role.getValue()));

        UserService userService = new UserService();
        userService.modifier(currentUser);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur mis à jour avec succès !");
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private boolean validerSaisie() {
        if (tf_nom.getText().trim().isEmpty() || tf_prenom.getText().trim().isEmpty()
                || tf_email.getText().trim().isEmpty() || tf_mot_de_passe.getText().isEmpty()
                || tf_tel.getText().trim().isEmpty() || date_date_naiss.getValue() == null
                || combo_role.getValue() == null || toggleGroup.getSelectedToggle() == null
                || tf_image.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return false;
        }
        if (!Pattern.matches("^[A-Z][a-zA-Z ]*$", tf_nom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit commencer par une majuscule et contenir uniquement des lettres et des espaces !");
            return false;
        }
        if (!Pattern.matches("^[A-Z][a-zA-Z ]*$", tf_prenom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom doit commencer par une majuscule et contenir uniquement des lettres et des espaces !");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$", tf_email.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse e-mail invalide !");
            return false;
        }
        if (!Pattern.matches("^\\d{8}$", tf_tel.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void retour(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void annuler(ActionEvent event) {
        tf_nom.clear();
        tf_prenom.clear();
        tf_email.clear();
        tf_mot_de_passe.clear();
        tf_tel.clear();
        tf_image.clear();
        date_date_naiss.setValue(null);
        combo_role.setValue(null);
        toggleGroup.selectToggle(null);
    }



    public void setUserData(User user) {
        this.currentUser = user;


        tf_nom.setText(user.getNom());
        tf_email.setText(user.getEmail());


        tf_prenom.setText(user.getPrenom());

        tf_mot_de_passe.setText(user.getPassword());
        tf_tel.setText(String.valueOf(user.getNum_telephone()));
        tf_image.setText(user.getImage());
        date_date_naiss.setValue(user.getDate_de_naissance());
        if ("Homme".equals(user.getGenre())) rb_homme.setSelected(true);
        else rb_femme.setSelected(true);
        combo_role.setValue(user.getRole().getValue());



    }
}


