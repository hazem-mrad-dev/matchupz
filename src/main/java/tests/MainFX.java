package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
            Parent root = loader.load();

            // Définition de la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Espaces Sportifs");
            stage.setResizable(false); // Empêche le redimensionnement de la fenêtre
            stage.show();
        } catch (Exception e) {
            System.err.println("⚠ Erreur lors du chargement du fichier FXML !");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
