package controllers.user;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
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
import models.utilisateur.Role;
import models.match.SessionManager;
import models.utilisateur.User;
import services.user.EmailService;
import services.user.UserService;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;

public class InterfaceAController  {
    @FXML
    private ImageView image;

    @FXML
    private Button button_gmail;


    @FXML
    private Button button_login;
    @FXML
    private TextField tf_email;
    @FXML
    private Button button_signup;
    @FXML
    private TextField tf_mot_de_passe;

    @FXML
    private Button button_mdp_oublié;
    private static final String CLIENT_SECRETS_PATH = "/credentials.json"; // Chemin vers credentials.json
    private static final File DATA_STORE_DIR = new File("tokens"); // Stockage des jetons
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final java.util.List<String> SCOPES = Collections.singletonList("email profile");
    private final UserService userService = new UserService();
    @FXML

    void gmail(ActionEvent event) {


    }
    private void redirectAfterGoogleLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml")); // Redirigez où vous voulez
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page après connexion Google.");
        }
    }


    @FXML
    private void mdp_oublié(ActionEvent event) {
        String email =  tf_email.getText();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez entrer votre adresse email.");
            return;
        }

        boolean emailExists = userService.checkIfEmailExists(email);  // Vérifie si l'email existe dans la base de données
        if (emailExists) {
            // Générer un code de réinitialisation de 4 chiffres
            String resetCode = userService.generateResetCode(email);

            // Envoyer le code à l'email de l'utilisateur
            boolean emailSent = sendPasswordResetCode(email, resetCode);

            if (emailSent) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Un code de réinitialisation a été envoyé à votre adresse email.");

                // Rediriger vers code.fxml après l'envoi du mail
                redirectToCodePage(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'envoi de l'email.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun utilisateur trouvé avec cet email.");
        }
    }

    private boolean sendPasswordResetCode(String email, String resetCode) {
        String subject = "Code de réinitialisation de mot de passe";
        String message = "Voici votre code de réinitialisation : \n" + resetCode;

        try {
            // Utiliser un service d'email pour envoyer le message
            EmailService.sendEmail(email, subject, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void redirectToCodePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/code.fxml"));
            Parent root = loader.load();

            // 🔹 Récupérer le contrôleur de code.fxml
            Code codeController = loader.getController();

            // 🔹 Passer l'email saisi au contrôleur Code
            codeController.setUserEmail(tf_email.getText().trim());

            // 🔹 Afficher la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Code de Réinitialisation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page du code.");
        }
    }
/*
    @FXML
    void mdp_oublié(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/code.fxml"));
            Stage stage = (Stage) button_signup.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }*/
    @FXML
    private void initialize() {
        image.setImage(new Image("file:/C:/Users/MSI/Desktop/image.png"));
        
    }
    @FXML
    void sinscrire(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/sign-up.fxml"));
            Stage stage = (Stage) button_signup.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }
    @FXML
    void seconnecter(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_mot_de_passe.getText();
        User user1 = userService.verifierUtilisateur(email, password);
        System.out.println("Résultat de la vérification : " + user1);


        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return;
        }


        User user = userService.verifierUtilisateur(email, password);

        if (user != null) {

            SessionManager.setCurrentUser(user);


            if (user.getRole() == Role.RESPONSABLE_LOGISTIQUE) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            } else if (user.getRole() == Role.ADMIN) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            }
            else if (user.getRole() == Role.RESPONSABLE_SPONSORING) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {
                    // Charger la page tableView.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            }
            else if (user.getRole() == Role.RESPONSABLE_SPORTIF) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            }
            else if (user.getRole() == Role.RESPONSABLE_ESPACE_SPORTIF) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            }

            else if (user.getRole() == Role.RESPONSABLE_COACH) {
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page tableView.");
                }
            }

            else if (user.getRole() == Role.UTILISATEUR) {

                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/log-in.fxml"));
                    Stage stage = (Stage) button_login.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }
    }




    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private record GoogleAuthorizationCodeInstalledApp<GoogleAuthorizationCodeFlow>(GoogleAuthorizationCodeFlow flow, LocalServerReceiver build) {
    }
}

