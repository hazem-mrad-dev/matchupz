package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Use getClassLoader().getResource() to load the FXML file from the classpath
            URL fxmlLocation = getClass().getClassLoader().getResource("AffichageMatch.fxml");

            if (fxmlLocation == null) {
                // In case the resource is not found, print a more descriptive error
                throw new RuntimeException("FXML file 'AjouterMatch.fxml' not found in the classpath.");
            }

            // Load the FXML file using the URL
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Define the scene and set it to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Matchs");
            stage.setResizable(false);  // Prevent window resizing
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
