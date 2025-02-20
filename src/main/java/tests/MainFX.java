package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        loadScene("AjoutFournisseur.fxml");

        stage.setTitle("Gestion des Fournisseurs et Matériel");
        stage.show();
    }

    // Load new scene dynamically
    public static void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("Erreur de chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
