package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.match.SessionManager;
import models.utilisateur.User;
import java.io.IOException;

public class LogInController {

    @FXML
    private Button Current_User;

    @FXML
    private Button button_logout;
    @FXML
    private ImageView profileImageView;




    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
    }


    @FXML
    void Update(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/updateUtilisateur.fxml"));
            Parent parent = loader.load();

            UpdateUtilisateurController controller = loader.getController();

            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.initData(user);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
    }


    private void afficherProfil(User user) {
     //   tfnom.setText(user.getNom());  // Affiche le nom de l'utilisateur
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            profileImageView.setImage(image);
        }
    }

    @FXML
    void deconnection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) button_logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }

}
