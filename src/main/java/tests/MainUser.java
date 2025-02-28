package tests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUser extends Application {


    @Override


    public void start(Stage stage)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/sign-up.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("sign-up");
            stage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }



   /*public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(   "/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();


    }}*/

   /* public void start(Stage stage)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sign-up.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter");
            stage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }}



*/
}


/*package tests;
import models.Role;
import models.User;
import utils.MyDataSource;
import services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainUser {
    public static void main(String[] args) {

        MyDataSource.getInstance();
        UserService u1= new UserService();



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateNaissance = LocalDate.parse("06/07/2003", formatter);


        //User user = new User("Khamlia", "Feriel", "FerielKhamlia@esprit.tn", "123777", 27002735,dateNaissance, "femme", Role.ADMIN, "mmm");
        //u1.ajouter(user);
        //u1.ajouter(new User("Khamlia","Malek","MalekKhamlia@gmail.com","666777",98008989,dateNaissance,"femme",Role.RESPONSABLE_SPONSORING,"url2"));
        // u1.supprimer(new User(2));

       // u1.modifier(new User(4,"Khamlia", "Farah", "FarahKhamlia@gmail.com", "123231", 27002735,dateNaissance, "femme", Role.ADMIN, "url image "));
       // u1.modifier(new User(3,"Khamlia","Malek","MalekKhamlia@gmail.com","666777",98008989,dateNaissance,"femme",Role.RESPONSABLE_SPONSORING,"url2"));
        User user = new User("Alex", "Alex", "Alex@esprit.tn", "123777", 90787878,dateNaissance, "Homme", Role.ADMIN, "IMAGE");
        u1.ajouter(user);

        u1.recherche();


    }
}
*/