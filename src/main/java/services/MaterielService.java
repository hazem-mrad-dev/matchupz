package services;

import models.Materiel;
import models.TypeMateriel;
import models.EtatMateriel;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements IService<Materiel> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Materiel materiel) {
        String req = "INSERT INTO materiel (id_fournisseur, nom, type, quantite, etat, prix_unitaire) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, materiel.getId_fournisseur());
            ps.setString(2, materiel.getNom());
            ps.setString(3, materiel.getType().name());  // Convert Enum -> String
            ps.setInt(4, materiel.getQuantite());
            ps.setString(5, materiel.getEtat().name());  // Convert Enum -> String
            ps.setFloat(6, materiel.getPrix_unitaire());
            ps.executeUpdate();
            System.out.println("✅ Matériel ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Materiel materiel) {
        String req = "UPDATE materiel SET id_fournisseur=?, nom=?, type=?, quantite=?, etat=?, prix_unitaire=? WHERE id_materiel=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, materiel.getId_fournisseur());
            ps.setString(2, materiel.getNom());
            ps.setString(3, materiel.getType().name());
            ps.setInt(4, materiel.getQuantite());
            ps.setString(5, materiel.getEtat().name());
            ps.setFloat(6, materiel.getPrix_unitaire());
            ps.setInt(7, materiel.getId_materiel());
            ps.executeUpdate();
            System.out.println("✅ Matériel modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Materiel materiel) {
        String req = "DELETE FROM materiel WHERE id_materiel=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, materiel.getId_materiel());
            ps.executeUpdate();
            System.out.println("✅ Matériel supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Materiel> rechercher() {
        String req = "SELECT * FROM materiel";
        List<Materiel> materiels = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Materiel materiel = new Materiel(
                        rs.getInt("id_materiel"),
                        rs.getInt("id_fournisseur"),
                        rs.getString("nom"),
                        TypeMateriel.valueOf(rs.getString("type")),  // Convert String -> Enum
                        rs.getInt("quantite"),
                        EtatMateriel.valueOf(rs.getString("etat")),  // Convert String -> Enum
                        rs.getFloat("prix_unitaire")
                );
                materiels.add(materiel);
            }
            System.out.println("🔎 Liste des matériels : " + materiels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiels;
    }
}
