package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/sample.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter");
            stage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }
}

