/*package controllers.user;

import com.google.api.client.http.javanet.NetHttpTransport;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Set;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class JarbyController {

    @FXML
    private Button button_mail;

    @FXML
    private TextField emailField;

    private Gmail gmailService;

    public JarbyController() {
        try {
            // Initialize the Gmail service
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            Credential credential = getCredentials(httpTransport, jsonFactory);
            gmailService = new Gmail.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("Test Mailer")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Gmail Service Initialization Failed", "Failed to initialize Gmail service: " + e.getMessage());
        }
    }

    @FXML
    void bt_envoyermail(ActionEvent event) {
        String email = emailField.getText();

        // Check if the user exists in the database
        if (checkUserExists(email)) {
            // Generate a verification code
            String verificationCode = generateVerificationCode();

            // Send the verification code via email
            sendVerificationCode(email, verificationCode);

            // Navigate to the verification page
            openVerificationPage(email, verificationCode);
        } else {
            // Show an error message if the user does not exist
            showAlert(AlertType.ERROR, "Error", "User not found!", "The email address does not exist in our database.");
        }
    }

    private boolean checkUserExists(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if the user exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit code
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void sendVerificationCode(String email, String code) {
        try {
            // Use the GMailer class to send the email
            GMailer gMailer = new GMailer(gmailService); // Pass the Gmail service object
            gMailer.sendEmail(email, "Verification Code", "Your verification code is: " + code);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to send email", "An error occurred while sending the verification code: " + e.getMessage());
        }
    }

    private void openVerificationPage(String email, String verificationCode) {
        try {
            // Load the verification page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/verificationPage.fxml"));
            Parent root = loader.load();

            // Pass the email and verification code to the verification page controller
            VerificationController verificationController = loader.getController();
            verificationController.setEmailAndCode(email, verificationCode);

            // Display the verification page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Verification");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to open verification page", "An error occurred while loading the verification page.");
        }
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory) throws IOException {
        // Specify the full path to the file in the Downloads directory
        //String filePath = "C:/Users/MSI/Downloads/client_secret_340679100619-i2ggmfo8rjjbm18pcngq9ubntnjg5hvu.apps.googleusercontent.com.json";
        String filePath = "C:/Users/MSI/Downloads/client_secret_891405955720-6sdgak0li87g5rm4vi950583r0ir31e7.apps.googleusercontent.com.json";
        // Use FileInputStream to read the file
        try (InputStream in = new FileInputStream(filePath)) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                    .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }
}*/