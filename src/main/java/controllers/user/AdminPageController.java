package controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.match.SessionManager;
import models.match.TheImages;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.user.UserService;
import utils.MyDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminPageController {

    @FXML
    private Button log_out;

    @FXML
    private TableColumn<User, String> imagecol;

    @FXML
    private Button button_ajout;

    @FXML
    private TableView<User> table_user_view;

    @FXML
    private TableColumn<User, Integer> idcol;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TableColumn<User, String> prenomcol;

    @FXML
    private TableColumn<User, String> emailcol;

    @FXML
    private TableColumn<User, String> genrecol;

    @FXML
    private TableColumn<User, Role> rolecol;

    @FXML
    private TableColumn<User, String> mdpcol;

    @FXML
    private TableColumn<User, LocalDate> datecol;

    @FXML
    private TableColumn<User, Integer> telcol;

    @FXML
    private TableColumn<User, Void> modifcol;

    @FXML
    private TableColumn<User, Void> suppcol;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private UserService userService = new UserService();
    Connection connection = MyDataSource.getInstance().getConn();

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser(); // Récupérer l'utilisateur connecté
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
        // Initialiser les colonnes
        idcol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genrecol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        rolecol.setCellValueFactory(new PropertyValueFactory<>("role"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        telcol.setCellValueFactory(new PropertyValueFactory<>("num_telephone"));
        mdpcol.setCellValueFactory(new PropertyValueFactory<>("password"));



        imagecol.setCellValueFactory(new PropertyValueFactory<>("image"));
        imagecol.setCellFactory(param -> new TableCell<User, String>() {

            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    TheImages theImage = new TheImages(imageUrl);
                    setGraphic(theImage.getImages());
                }
            }
        });
        modifcol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px;");
                editButton.setOnAction(event -> {
                       /* try {
                            Parent parent = FXMLLoader.load(getClass().getResource("/updateUtilisateur.fxml"));
                            Scene scene = new Scene(parent);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                    try {
                        // Récupérer l'utilisateur sélectionné
                        User selectedUser = getTableView().getItems().get(getIndex());

                        // Charger le fichier FXML de modification
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyuser.fxml"));
                        Parent parent = loader.load();

                        // Récupérer le contrôleur et lui passer l'utilisateur sélectionné
                        ModifyUserController controller = loader.getController();
                        controller.initData(selectedUser);

                        // Afficher la fenêtre de modification
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                        stage.setOnHidden(event1 -> loadUsers());

                    } catch (IOException ex) {
                        Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // User user = getTableView().getItems().get(getIndex());
                    // goToEditPage(user);
                });
            }


            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });




        suppcol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    userService.supprimer(selectedUser);
                    loadUsers();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userService.recherche());
        table_user_view.setItems(userList);
    }

    @FXML
    void Ajouter(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/useradd.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) button_ajout.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void openModifyWindow(User user) {
        try {
          //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ModifierUser.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
            Parent root = loader.load();

            UpdateUtilisateurController controller = loader.getController();
            controller.setUserToModify(user);

            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier Utilisateur");
            modifyStage.setScene(new Scene(root));
            modifyStage.setOnHidden(event -> loadUsers());
            modifyStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void log_out(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaceA.fxml"));
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button bt_user;
    @FXML
    void pageuser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminpage.fxml"));
            Stage stage = (Stage) bt_user.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {

    }




    @FXML
    private void CurrentUser(ActionEvent event) {
        // Code à exécuter
    }
    @FXML
    void Update(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyuser.fxml"));
            Parent parent = loader.load();


            ModifyUserController controller = loader.getController();


            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.initData(user);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
    }

    private void afficherProfil(User user) {
        //   tfnom.setText(user.getNom());  // Affiche le nom de l'utilisateur
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            profileImageView.setImage(image);
        }
    }

}

