package controllers.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.user.UserService;

import java.io.IOException;

public class newpassword {

    @FXML
    private ImageView logo;
    @FXML
    private ImageView image;
    @FXML
    public void initialize() {
        image.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/user/newpassword.png"));
        logo.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/matchupz.png"));

    }
    @FXML
    private TextField newPasswordField; // Champ du nouveau mot de passe

    private String userEmail; // Email de l'utilisateur (à récupérer depuis la scène précédente)

    public void setUserEmail(String email) {
        this.userEmail = email;
    }


    @FXML
    private void saveNewPassword() {
        String newPassword = newPasswordField.getText().trim();

        if (newPassword.isEmpty()) {
            showAlert("Erreur", "Le mot de passe ne peut pas être vide.");
            return;
        }

        UserService userService = new UserService();
        boolean updated = userService.updatePassword(userEmail, newPassword);

        if (updated) {
            showAlert("Succès", "Mot de passe mis à jour avec succès !");
            redirectToSignIn();


        } else {
            showAlert("Erreur", "Impossible de mettre à jour le mot de passe.");
        }
    }
    private void redirectToSignIn() {
        try {
            // Charger le fichier FXML de la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et fermer la fenêtre
            Stage stage = (Stage) newPasswordField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}