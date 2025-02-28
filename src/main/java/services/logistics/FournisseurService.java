package services.logistics;

import models.logistics.Fournisseur;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurService implements IService<Fournisseur> {


    Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(Fournisseur fournisseur) {
        String req = "INSERT INTO fournisseur (nom, email, adresse, categorie_produit) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getEmail());
            ps.setString(3, fournisseur.getAdresse());
            ps.setString(4, fournisseur.getCategorie_produit());
            ps.executeUpdate();
            System.out.println("✅ Fournisseur ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Fournisseur fournisseur) {
        String req = "UPDATE fournisseur SET nom=?, email=?, adresse=?, categorie_produit=? WHERE id_fournisseur=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getEmail());
            ps.setString(3, fournisseur.getAdresse());
            ps.setString(4, fournisseur.getCategorie_produit());
            ps.setInt(5, fournisseur.getId_fournisseur());
            ps.executeUpdate();
            System.out.println("✅ Fournisseur modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Fournisseur fournisseur) {
        String req = "DELETE FROM fournisseur WHERE id_fournisseur = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, fournisseur.getId_fournisseur());
            ps.executeUpdate();
            System.out.println("✅ Fournisseur supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Fournisseur> recherche() {
        String req = "SELECT * FROM fournisseur";
        List<Fournisseur> fournisseurs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        rs.getInt("id_fournisseur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getString("categorie_produit")
                );
                fournisseurs.add(fournisseur);
            }
            System.out.println("🔎 Liste des fournisseurs : " + fournisseurs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurs;
    }
    public int getLastInsertedId() {
        String req = "SELECT id_fournisseur FROM fournisseur ORDER BY id_fournisseur DESC LIMIT 1";
        int lastId = -1;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(req);
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }
    public List<Fournisseur> getFournisseursByCategory(String category) {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String query = "SELECT * FROM fournisseur WHERE categorie_produit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setId_fournisseur(rs.getInt("id_fournisseur"));
                f.setNom(rs.getString("nom"));
                f.setEmail(rs.getString("email"));
                f.setAdresse(rs.getString("adresse"));
                f.setCategorie_produit(rs.getString("categorie_produit"));
                fournisseurs.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurs;
    }
    public boolean exists(String nom) {
        String query = "SELECT COUNT(*) FROM fournisseur WHERE nom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
