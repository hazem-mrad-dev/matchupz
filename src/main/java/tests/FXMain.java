package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MyDataSource;

public class FXMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyDataSource dataSource = MyDataSource.getInstance();
        if (dataSource.getConn() != null) {
            System.out.println("Connection test successful.");
        } else {
            System.err.println("Connection test failed!");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}