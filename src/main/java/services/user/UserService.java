package services.user;

import models.EspaceSportif.EspaceSportif;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.IService;
import utils.MyDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserService implements IService<User> {

    Connection connection = MyDataSource.getInstance().getConn();

    public String generateResetCode(String email) {
        String resetCode = String.format("%04d", new Random().nextInt(10000));
        saveResetCode(email, resetCode);
        return resetCode;
    }

    private void saveResetCode(String email, String resetCode) {
        String query = "UPDATE user SET reset_code = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, resetCode);
            ps.setString(2, email);
            ps.executeUpdate();
            System.out.println("✅ Reset code stored for " + email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean verifyResetCode(String email, String enteredCode) {
        String query = "SELECT reset_code FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedCode = rs.getString("reset_code");
                System.out.println("Stored code: " + storedCode);
                return storedCode != null && storedCode.trim().equals(enteredCode.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("❌ Code verification failed.");
        return false;
    }


    public boolean updatePassword(String email, String newPassword) {
     //   String hashedPassword = hashPassword(newPassword); // Hacher le mot de passe

        String query = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
          //  ps.setString(1, hashedPassword);
            ps.setString(1, newPassword);
            ps.setString(2, email);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


/*
    public boolean emailExiste(String email) {
        String req = "SELECT COUNT(*) FROM user WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Si COUNT(*) > 0, l'email existe
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }

        return false; // En cas d'erreur, on suppose que l'email n'existe pas
    }
*/
public boolean checkIfEmailExists(String email) {
    // Ensure that the query correctly searches for the email in the database
    String query = "SELECT * FROM user WHERE email = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs.next();  // If there is a matching email, it will return true
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}




    public List<User> rechercherParMotCle(String motCle) {
        // Use backticks to escape the reserved keyword `password`
        String req = "SELECT id_user, nom, prenom, email, `password`, num_telephone, date_de_naissance, genre, role, image FROM user " +
                "WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR `password` LIKE ? OR num_telephone LIKE ? OR date_de_naissance LIKE ? OR genre LIKE ? OR role LIKE ? OR image LIKE ?";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            // Set the search keyword for each parameter
            String likeKeyword = "%" + motCle + "%";
            ps.setString(1, likeKeyword); // nom
            ps.setString(2, likeKeyword); // prenom
            ps.setString(3, likeKeyword); // email
            ps.setString(4, likeKeyword); // password
            ps.setString(5, likeKeyword); // num_telephone
            ps.setString(6, likeKeyword); // date_de_naissance
            ps.setString(7, likeKeyword); // genre
            ps.setString(8, likeKeyword); // role
            ps.setString(9, likeKeyword); // image

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId_user(rs.getInt("id_user"));
                    u.setNom(rs.getString("nom"));
                    u.setPrenom(rs.getString("prenom"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setNum_telephone(rs.getInt("num_telephone"));
                    u.setDate_de_naissance(rs.getDate("date_de_naissance").toLocalDate());
                    u.setGenre(rs.getString("genre"));
                    u.setRole(Role.fromString(rs.getString("role")));
                    u.setImage(rs.getString("image"));
                    users.add(u);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs par mot-clé : " + e.getMessage());
        }

        return users;
    }


  /*  @Override
    public void ajouter(User user) {


        String req = " INSERT INTO `user`(`nom`, `prenom`, `email`,`password`,`num_telephone`,`date_de_naissance`,`genre`,`role`,`image`) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            //ps.setString(8, user.getRole());
            ps.setString(9, user.getImage());

            ps.executeUpdate();
            System.out.println("Add  established successfully");

        } catch (Exception e) {
            e.getMessage();
        }


    }
*/



    // eli kenet
    /*
  @Override
  public void ajouter(User user) {
      if (emailExiste(user.getEmail())) {
          System.err.println("Erreur : Cet email est déjà utilisé !");
          return; // Empêche l'ajout si l'email existe
      }

      String req = "INSERT INTO `user`(`nom`, `prenom`, `email`, `password`, `num_telephone`, `date_de_naissance`, `genre`, `role`, `image`) VALUES(?,?,?,?,?,?,?,?,?)";

      try (PreparedStatement ps = connection.prepareStatement(req)) {
          ps.setString(1, user.getNom());
          ps.setString(2, user.getPrenom());
          ps.setString(3, user.getEmail());
          ps.setString(4, user.getPassword());
          ps.setInt(5, user.getNum_telephone());
          ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
          ps.setString(7, user.getGenre());
          ps.setString(8, user.getRole().getValue());
          ps.setString(9, user.getImage());

          int rowsInserted = ps.executeUpdate();
          if (rowsInserted > 0) {
              System.out.println("Utilisateur ajouté avec succès !");
          } else {
              System.err.println("Échec de l'ajout de l'utilisateur.");
          }
      } catch (SQLException e) {
          System.err.println("Erreur lors de l'ajout : " + e.getMessage());
      }
  }
*/
    @Override
    public void ajouter(User user) {
        String req = "INSERT INTO `user`(`nom`, `prenom`, `email`,`password`,`num_telephone`,`date_de_naissance`,`genre`,`role`,`image`,`reset_code`) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠️ Connexion à la base de données non établie !");
                return;
            }

            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            ps.setString(9, user.getImage());
            ps.setString(10, "0"); // Définir reset_code à "0"
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Utilisateur ajouté avec succès !");
            } else {
                System.out.println("❌ Aucun utilisateur ajouté !");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Voir l'erreur dans la console
        }
    }


    /*
  @Override
  public void ajouter(User user) {
      if (checkIfEmailExists(user.getEmail())) {
          System.err.println("Email détecté comme déjà utilisé : " + user.getEmail());
          return;
      } else {
          System.out.println("L'email est disponible : " + user.getEmail());
      }

      String req = "INSERT INTO `user`(`nom`, `prenom`, `email`, `password`, `num_telephone`, `date_de_naissance`, `genre`, `role`, `image`) VALUES(?,?,?,?,?,?,?,?,?)";

      try (PreparedStatement ps = connection.prepareStatement(req)) {
          ps.setString(1, user.getNom());
          ps.setString(2, user.getPrenom());
          ps.setString(3, user.getEmail());
          ps.setString(4, user.getPassword());
          ps.setInt(5, user.getNum_telephone());
          ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
          ps.setString(7, user.getGenre());
          ps.setString(8, user.getRole().getValue());
          ps.setString(9, user.getImage());

          int rowsInserted = ps.executeUpdate();
          if (rowsInserted > 0) {
              System.out.println("Utilisateur ajouté avec succès !");
          } else {
              System.err.println("Échec de l'ajout de l'utilisateur.");
          }
      } catch (SQLException e) {
          System.err.println("Erreur lors de l'ajout : " + e.getMessage());
      }
  }*/

    @Override
    public void supprimer(User user) {

        String req = " DELETE FROM `user` WHERE `id_user` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);

            ps.setInt(1, user.getId_user());
            ps.executeUpdate();
            System.out.println("Delete established successfully");

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void modifier(User user) {
        String req = "UPDATE `user` SET `nom`=?, `prenom`=?, `email`=?, `password`=?, `num_telephone`=?, `date_de_naissance`=?, `genre`=?, `role`=?, `image`=? WHERE id_user = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            ps.setString(9, user.getImage());
            ps.setInt(10, user.getId_user());

            System.out.println("Modification en cours pour ID: " + user.getId_user()); // 🔍 Vérification

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie !");
            } else {
                System.out.println("Aucune mise à jour effectuée !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@Override
    public void modifier(User user) {

        String req ="UPDATE `user` SET `nom`=?,`prenom`=?,`email`=?,`password`=?,`num_telephone`=?,`date_de_naissance`=?,`genre`=?,`role`=?,`image`=? WHERE id_user =?";
        try
        {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getNum_telephone());
            ps.setDate(6, java.sql.Date.valueOf(user.getDate_de_naissance()));
            ps.setString(7, user.getGenre());
            ps.setString(8, user.getRole().getValue());
            //ps.setString(8, user.getRole());
            ps.setString(9, user.getImage());
            ps.setInt(10, user.getId_user());
            ps.executeUpdate();
            System.out.println("Modifier ");
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée !");
            }


        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }*/

    @Override
    public List<User> recherche() {
        String req = "SELECT * FROM `user`";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId_user(rs.getInt("id_user"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setNum_telephone(rs.getInt("num_telephone"));
                u.setDate_de_naissance(rs.getDate("date_de_naissance").toLocalDate());
                u.setGenre(rs.getString("genre"));
                u.setRole(Role.fromString(rs.getString("role")));
                //u.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
                // u.setRole(rs.getString("role"));
                u.setImage(rs.getString("image"));
                users.add(u);

            }
            System.out.println(users);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }


    public User verifierUtilisateur(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("num_telephone"),
                        rs.getDate("date_de_naissance").toLocalDate(),
                        rs.getString("genre"),
                        models.utilisateur.Role.fromString(rs.getString("role")),
                        rs.getString("image")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // L'utilisateur n'existe pas
    }
}

