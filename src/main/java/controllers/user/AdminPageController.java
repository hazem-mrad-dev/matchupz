package controllers.user;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.properties.UnitValue;
import javafx.scene.control.Alert;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminPageController {

    @FXML
    private ImageView Home;

    @FXML
    private Button log_out;
    @FXML
    private Label nom_user;

    @FXML
    private TableColumn<User, String> imagecol;

    @FXML
    private Button button_ajout;

    @FXML
    private TableColumn<User, Void> expocol;
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
    private ImageView logogmatchupz;


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
    private ImageView profileImageView;
    @FXML
    private Button bt_user;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView icon3;

    @FXML
    private ImageView icon4;

    @FXML
    private ImageView icon5;

    @FXML
    private TableColumn<User, Void> modifcol;

    @FXML
    private TableColumn<User, Void> suppcol;
    @FXML
    private Button dashboard;
    @FXML
    private Button teams;
    @FXML
    private TextField searchField;

    @FXML
    private Button espace;

    @FXML
    private Button sponsor;

    @FXML
    private Button logistique;
    @FXML
    private Button match;

    @FXML
    private ImageView icon6;

    @FXML
    private ImageView icon7;
    @FXML
    void dashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void log_out(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void pageuser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
            Stage stage = (Stage) bt_user.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }


    @FXML
    void teams(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
            Stage stage = (Stage) teams.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void espace(ActionEvent event) {

    }

    @FXML
    void logistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void match(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
            Stage stage = (Stage) match.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }



    }

    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void Home(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/log-in.fxml"));
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }


    private ObservableList<User> userList = FXCollections.observableArrayList();
    private UserService userService = new UserService();
    Connection connection = MyDataSource.getInstance().getConn();
    private UserService user;

    public AdminPageController() {
        this.user = new UserService();
    }
    private void searchUtilisateur(String motCle) {
        List<User> resultatRecherche = user.rechercherParMotCle(motCle.toLowerCase());

        ObservableList<User> data = FXCollections.observableArrayList(resultatRecherche);
        table_user_view.setItems(data);
    }

    @FXML
    public void initialize() {
        Home.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/home.png"));
        icon2.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/logistics.png"));
        icon3.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/espaces.png"));
        icon4.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/team.png"));
        logogmatchupz.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/matchupz.png"));
        icon6.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/vs.png"));
        icon5.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/sponsor.png"));
        icon7.setImage(new Image("file:/C:/Users/MSI/Desktop/Avant integration/CRUD/src/main/resources/icons/user.png"));


        User user = SessionManager.getCurrentUser();
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }

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

                    try {

                        User selectedUser = getTableView().getItems().get(getIndex());


                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
                        Parent parent = loader.load();


                        ModifyUserController controller = loader.getController();
                        controller.initData(selectedUser);


                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                        stage.setOnHidden(event1 -> loadUsers());

                    } catch (IOException ex) {
                        Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

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

      /*  expocol.setCellFactory(param -> new TableCell<>() {
            private final Button exportButton = new Button("Exporter PDF");

            {
                exportButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px;");
                exportButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    try {
                        exportUserToPDF(selectedUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation en PDF: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : exportButton);
            }
        });*/
        expocol.setCellFactory(param -> new TableCell<>() {
            private final Button exportButton = new Button("Exporter PDF");

            {
                exportButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px;");
                exportButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    System.out.println("Bouton Exporter cliqué pour l'utilisateur : " + selectedUser.getNom());
                    try {
                        exportUserToPDF(selectedUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation en PDF: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : exportButton);
            }
        });

        loadUsers();


        // Ajout d'un listener pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchUtilisateur(newValue));




    }


    private void exportUserToPDF(User user) throws Exception {
        String fileName = "C:/Users/MSI/Desktop/User_" + user.getId_user() + "_" + user.getNom() + ".pdf";
        System.out.println("Nom du fichier : " + fileName);

        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Informations de l'utilisateur : ")
                .setBold()
                .setFontSize(14));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("ID: " + user.getId_user()));
        document.add(new Paragraph("Nom: " + user.getNom()));
        document.add(new Paragraph("Prénom: " + user.getPrenom()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Genre: " + user.getGenre()));
        document.add(new Paragraph("Rôle: " + user.getRole()));
        document.add(new Paragraph("Date de naissance: " + user.getDate_de_naissance()));
        document.add(new Paragraph("Téléphone: " + user.getNum_telephone()));
        document.add(new Paragraph("image: " + user.getImage()));

// Ajout de la photo au lieu de l'URL



        document.close();
        System.out.println("PDF généré avec succès : " + fileName);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le PDF a été généré avec succès: " + fileName);
    }



    private void loadUsers() {
        userList.clear();
        userList.addAll(userService.recherche());
        table_user_view.setItems(userList);
    }

    @FXML
    void Ajouter(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/useradd.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) button_ajout.getScene().getWindow();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }



    private void openModifyWindow(User user) {
        try {

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


    private void showAlert(Alert.AlertType alertType, String erreur, String s) {

    }

    @FXML
    private Button sortButton; // Assure-toi que le bouton est bien défini dans le contrôleur

    @FXML
    void sortUsersByPrenom(ActionEvent event) {
        FXCollections.sort(userList, (u1, u2) -> u1.getPrenom().compareToIgnoreCase(u2.getPrenom()));
        table_user_view.refresh(); // Rafraîchir l'affichage du tableau
    }
    @FXML
    void Update(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
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

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            profileImageView.setImage(image);

        }
    }
    @FXML
    private void CurrentUser(ActionEvent event) {
        // Code à exécuter
    }
}

