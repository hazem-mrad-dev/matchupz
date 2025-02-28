package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class VerificationController {

    @FXML
    private TextField codeField;

    @FXML
    private TextField newPasswordField;

    private String email;
    private String verificationCode;

    public void setEmailAndCode(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    @FXML
    void bt_submit(ActionEvent event) {
        String enteredCode = codeField.getText();
        String newPassword = newPasswordField.getText();

        if (enteredCode.equals(verificationCode)) {
            // Update the user's password in the database
            updatePassword(email, newPassword);
            showAlert(AlertType.INFORMATION, "Success", "Password Updated", "Your password has been updated successfully.");
        } else {
            showAlert(AlertType.ERROR, "Error", "Invalid Code", "The verification code is incorrect.");
        }
    }

    private void updatePassword(String email,String newPassword) {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Database Error", "An error occurred while updating the password.");
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}