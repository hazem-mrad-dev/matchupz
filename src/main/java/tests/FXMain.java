package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FXMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String fxmlPath = "/joueur/DisplayJoueur.fxml"; // Ensure this path is correct
        URL fxmlUrl = getClass().getResource(fxmlPath);

        if (fxmlUrl == null) {
            System.err.println("ERROR: FXML file not found at " + fxmlPath);
            return; // Stop execution if file is missing
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter");
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load FXML file.");
            e.printStackTrace(); // Print full error details
        } catch (RuntimeException e) {
            System.err.println("ERROR: Unexpected issue in JavaFX application.");
            e.printStackTrace();
        }
    }
}
