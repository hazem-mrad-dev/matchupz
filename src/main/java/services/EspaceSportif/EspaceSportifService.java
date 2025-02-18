package services.EspaceSportif;

import models.EspaceSportif.EspaceSportif;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceSportifService implements EspaceService<EspaceSportif> {

    private final Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(EspaceSportif espaceSportif) {
        String req = "INSERT INTO `espacesportif` (`nom_espace`, `adresse`, `categorie`, `capacite`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif ajouté !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(EspaceSportif espaceSportif) {
        String req = "UPDATE `espacesportif` SET nom_espace=?, adresse=?, categorie=?, capacite=? WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.setInt(5, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif modifié !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(EspaceSportif espaceSportif) {
        String req = "DELETE FROM `espacesportif` WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif supprimé !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<EspaceSportif> rechercher() {
        String req = "SELECT * FROM `espacesportif`";
        List<EspaceSportif> espacesSportifs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EspaceSportif espaceSportif = new EspaceSportif();
                espaceSportif.setIdLieu(rs.getInt("id_lieu"));
                espaceSportif.setNomEspace(rs.getString("nom_espace"));
                espaceSportif.setAdresse(rs.getString("adresse"));
                espaceSportif.setCategorie(rs.getString("categorie"));
                espaceSportif.setCapacite(rs.getFloat("capacite"));
                espacesSportifs.add(espaceSportif);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return espacesSportifs;
    }


    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SHOW COLUMNS FROM `espacesportif` WHERE Field = 'categorie'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String enumValues = rs.getString("Type");
                enumValues = enumValues.substring(enumValues.indexOf("(") + 1, enumValues.lastIndexOf(")"));
                String[] values = enumValues.replace("'", "").split(",");
                categories.addAll(List.of(values));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }
}
