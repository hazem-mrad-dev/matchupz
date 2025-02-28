package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.user.UserService;

import java.io.IOException;

public class Code {
    @FXML
    private ImageView logo;
    @FXML
    private ImageView image;
    @FXML
    public void initialize() {
        image.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/user/motpasse.png"));
        logo.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/matchupz.png"));
    }

        @FXML
    private TextField codeField;

    @FXML
    private Button submitButton;

    private String userEmail; // This should be set when the user enters their email
    private final UserService userService = new UserService();

    public void setUserEmail(String email) {
        this.userEmail = email;
        System.out.println("✅ userEmail set in Code controller: " + email);
    }


    @FXML
    private void handleSubmit(ActionEvent event) {
        String enteredCode = codeField.getText().trim();

        // 🔍 Debugging: Print values before verification
        System.out.println("Entered code in UI: " + enteredCode);
        System.out.println("Stored email: " + userEmail);

        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("⚠️ ERROR: userEmail is NULL or empty!");
            showAlert("Erreur", "L'email de l'utilisateur n'est pas défini. Veuillez réessayer.");
            return;
        }

        if (userService.verifyResetCode(userEmail, enteredCode)) {
            System.out.println("✅ Code verification succeeded. Redirecting to new password page.");
            redirectToNewPassword(event);
        } else {
            System.out.println("❌ Code verification failed in UI.");
            showAlert("Code incorrect", "Le code que vous avez saisi est incorrect.");
        }
    }

    private void redirectToNewPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/newpassword.fxml"));
            Parent root = loader.load();


            // Pass userEmail to the new controller if needed
            newpassword controller = loader.getController();
            controller.setUserEmail(userEmail); // Assuming you need this in newpassword.fxml

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page du nouveau mot de passe.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}