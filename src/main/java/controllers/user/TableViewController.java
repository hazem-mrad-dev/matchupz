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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.utilisateur.Role;
import models.match.TheImages;
import models.utilisateur.User;
import services.user.UserService;
import utils.MyDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableViewController {

    @FXML
    private TableColumn<User, LocalDate> datecol;

    @FXML
    private TableColumn<User, Void> modifcol;

    @FXML
    private TableColumn<User, Void> suppcol;


    @FXML
    private TableColumn<User, String> emailcol;

    @FXML
    private TableColumn<User, String> genrecol;

    @FXML
    private TableColumn<User, Integer> idcol;

    @FXML
    private TableColumn<User,String> imagecol;

    @FXML
    private TableColumn<User, String> mdpcol;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TableColumn<User, String> prenomcol;

    @FXML
    private TableColumn<User, Role> rolecol;

    @FXML
    private TableView<User> table_user_view;

    @FXML
    private TableColumn<User, Integer> telcol;

    @FXML
    private Button bt_add;


    @FXML
    private Button button_ajout;
    @FXML
    private Button bt_refresh;

    ObservableList<User> UserList = FXCollections.observableArrayList();
    Connection connection = MyDataSource.getInstance().getConn();

  /*  @FXML
    void initialize() {
        // Définir la cellule pour afficher l'image dans la TableView
        imagecol.setCellValueFactory(param -> {
            String imageUrl = param.getValue().getImage(); // récupérer l'URL de l'image
            Image image = new Image("file:" + imageUrl);  // Créer une instance Image à partir de l'URL
            ImageView imageView = new ImageView(image);  // Créer une ImageView avec l'image
            return new SimpleObjectProperty<>(imageView);  // Retourner l'ImageView
        });

        // Personnaliser l'affichage de l'image dans la cellule
        imagecol.setCellFactory(param -> new TableCell<User, ImageView>() {
            @Override
            protected void updateItem(ImageView imageView, boolean empty) {
                super.updateItem(imageView, empty);

                if (empty || imageView == null) {
                    setGraphic(null); // Si l'élément est vide, ne rien afficher
                } else {
                    imageView.setFitHeight(50); // Ajuste la hauteur de l'image
                    imageView.setFitWidth(50);  // Ajuste la largeur de l'image
                    setGraphic(imageView); // Afficher l'ImageView dans la cellule
                }
            }
        });
    }
*/
   /* @FXML
    public void initialize() {
        System.out.println("🔄 Initialisation du TableView...");

        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Ajouter un utilisateur fictif pour tester l'affichage
        UserList.add(new User(1, "Test", "Utilisateur", "test@email.com", "password", 12345678, LocalDate.now(), "Homme", Role.ADMIN, "image.jpg"));

        table_user_view.setItems(UserList);
    }*/

    /*
  @FXML
  void refreshTable(MouseEvent event) {
      try {
          System.out.println("🔄 Chargement des utilisateurs...");

          String query = "SELECT * FROM `user`";
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          ResultSet resultSet = preparedStatement.executeQuery();

          boolean hasUsers = false; // Variable pour savoir si on récupère des données
          while (resultSet.next()) {
              hasUsers = true;
              User user = new User(
                      resultSet.getInt("id_user"),
                      resultSet.getString("nom"),
                      resultSet.getString("prenom"),
                      resultSet.getString("email"),
                      resultSet.getString("password"),
                      resultSet.getInt("num_telephone"),
                      resultSet.getDate("date_de_naissance").toLocalDate(),
                      resultSet.getString("genre"),
                      Role.fromString(resultSet.getString("role")),
                      resultSet.getString("image")  // L'URL de l'image
              );
              UserList.add(user);

              // Debugging: Afficher chaque utilisateur chargé
              System.out.println("✅ Utilisateur chargé : " + user.getNom() + " " + user.getPrenom());
          }

          // Vérifier si la liste contient bien des éléments après la boucle
          if (!hasUsers) {
              System.out.println("⚠️ Aucun utilisateur trouvé dans la base de données !");
          } else {
              System.out.println("✅ Nombre total d'utilisateurs chargés : " + UserList.size());
          }

          // Ajouter les utilisateurs au TableView
          table_user_view.setItems(UserList);
          idcol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
          nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
          prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
          emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
          mdpcol.setCellValueFactory(new PropertyValueFactory<>("password"));
          telcol.setCellValueFactory(new PropertyValueFactory<>("num_telephone"));
          datecol.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
          genrecol.setCellValueFactory(new PropertyValueFactory<>("genre"));
          rolecol.setCellValueFactory(new PropertyValueFactory<>("role"));

          // Pour l'image, nous devons utiliser un cell factory qui créera un ImageView
          imagecol.setCellValueFactory(new PropertyValueFactory<>("image"));
          imagecol.setCellFactory(param -> new TableCell<User, String>() {
              @Override
              protected void updateItem(String imageUrl, boolean empty) {
                  super.updateItem(imageUrl, empty);
                  if (empty || imageUrl == null) {
                      setGraphic(null);
                  } else {
                      TheImages theImage = new TheImages(imageUrl);  // Créer une instance de TheImages
                      setGraphic(theImage.getImages());  // Mettre l'image dans la cellule
                  }
              }
          });

          System.out.println("✅ TableView mis à jour avec " + UserList.size() + " utilisateurs.");

      } catch (SQLException ex) {
          Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
 */
    @FXML
    void refreshTable(MouseEvent event) {
        try {
            System.out.println("🔄 Chargement des utilisateurs...");

            String query = "SELECT * FROM `user`";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean hasUsers = false; // Variable pour savoir si on récupère des données
            UserList.clear(); // Vider la liste avant de recharger les données
            while (resultSet.next()) {
                hasUsers = true;
                User user = new User(
                        resultSet.getInt("id_user"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getInt("num_telephone"),
                        resultSet.getDate("date_de_naissance").toLocalDate(),
                        resultSet.getString("genre"),
                        Role.fromString(resultSet.getString("role")),
                        resultSet.getString("image")  // L'URL de l'image
                );
                UserList.add(user);
                System.out.println("✅ Utilisateur chargé : " + user.getNom() + " " + user.getPrenom());
            }

            if (!hasUsers) {
                System.out.println("⚠️ Aucun utilisateur trouvé dans la base de données !");
            } else {
                System.out.println("✅ Nombre total d'utilisateurs chargés : " + UserList.size());
            }

            table_user_view.setItems(UserList);
            idcol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
            mdpcol.setCellValueFactory(new PropertyValueFactory<>("password"));
            telcol.setCellValueFactory(new PropertyValueFactory<>("num_telephone"));
            datecol.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
            genrecol.setCellValueFactory(new PropertyValueFactory<>("genre"));
            rolecol.setCellValueFactory(new PropertyValueFactory<>("role"));


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
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/updateUtilisateur.fxml"));
                            Parent parent = loader.load();

                            // Récupérer le contrôleur et lui passer l'utilisateur sélectionné
                            UpdateUtilisateurController controller = loader.getController();
                            controller.initData(selectedUser);

                            // Afficher la fenêtre de modification
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
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
                private final Button deleteButton = new Button("Supprimer");

                {
                    deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 12px;");
                    /*deleteButton.setOnAction(event -> {
                       // User user = getTableView().getItems().get(getIndex());
                       // deleteUser(user);
                    });*/
                    deleteButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        UserService userService = new UserService();

                        // Demander confirmation avant suppression (optionnel)
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation de suppression");
                        alert.setHeaderText("Voulez-vous vraiment supprimer cet utilisateur ?");
                        alert.setContentText("Cette action est irréversible.");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            userService.supprimer(user);


                            getTableView().getItems().remove(user);
                            System.out.println("Utilisateur supprimé : " + user.getNom());
                        }
                    });
                }

                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });

            System.out.println("✅ TableView mis à jour avec " + UserList.size() + " utilisateurs.");

        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/user/addUtilisateur.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/addUtilisateur.fxml"));
            Stage stage = (Stage) button_ajout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }


}
