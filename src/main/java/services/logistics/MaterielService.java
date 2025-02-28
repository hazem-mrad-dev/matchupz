package services.logistics;

import models.logistics.Materiel;
import models.logistics.TypeMateriel;
import models.logistics.EtatMateriel;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements IService<Materiel> {

    private Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(Materiel materiel) {
        String sql = "INSERT INTO materiel (id_fournisseur, nom, type, quantite, etat, prix_unitaire, barcode,image_data) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, materiel.getId_fournisseur());
            pstmt.setString(2, materiel.getNom());
            pstmt.setString(3, materiel.getType().name());
            pstmt.setInt(4, materiel.getQuantite());
            pstmt.setString(5, materiel.getEtat().name());
            pstmt.setFloat(6, materiel.getPrix_unitaire());
            pstmt.setString(7, materiel.getBarcode());
            pstmt.setBytes(8, materiel.getImageData());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Materiel materiel) {
        String sql = "UPDATE materiel SET id_fournisseur = ?, nom = ?, type = ?, quantite = ?, etat = ?, prix_unitaire = ?, barcode = ?,image_data = ? WHERE id_materiel = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, materiel.getId_fournisseur());
            pstmt.setString(2, materiel.getNom());
            pstmt.setString(3, materiel.getType().name());
            pstmt.setInt(4, materiel.getQuantite());
            pstmt.setString(5, materiel.getEtat().name());
            pstmt.setFloat(6, materiel.getPrix_unitaire());
            pstmt.setString(7, materiel.getBarcode());
            pstmt.setBytes(8, materiel.getImageData());
            pstmt.setInt(9, materiel.getId_materiel());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Matériel modifié avec succès : " + materiel.getNom());
            } else {
                System.out.println("⚠️ Aucun matériel modifié (id_materiel introuvable) : " + materiel.getId_materiel());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Échec de la mise à jour du matériel dans la base de données", e);
        }
    }

    @Override
    public void supprimer(Materiel materiel) {
        String req = "DELETE FROM materiel WHERE id_materiel=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, materiel.getId_materiel());
            ps.executeUpdate();
            System.out.println("✅ Matériel supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Materiel> recherche() {
        String req = "SELECT * FROM materiel";
        List<Materiel> materiels = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Materiel materiel = new Materiel(
                        rs.getInt("id_materiel"),
                        rs.getInt("id_fournisseur"),
                        rs.getString("nom"),
                        TypeMateriel.valueOf(rs.getString("type")),
                        rs.getInt("quantite"),
                        EtatMateriel.valueOf(rs.getString("etat")),
                        rs.getFloat("prix_unitaire"),
                        rs.getString("barcode"),
                        rs.getBytes("image_data")
                );
                materiels.add(materiel);
            }
            System.out.println("🔎 Liste des matériels : " + materiels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiels;
    }

    public boolean exists(String nom, String type) {
        String query = "SELECT COUNT(*) FROM materiel WHERE nom = ? AND type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Materiel findByBarcode(String barcode) {
        String query = "SELECT * FROM materiel WHERE barcode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Materiel m = new Materiel();
                m.setId_materiel(rs.getInt("id_materiel"));
                m.setId_fournisseur(rs.getInt("id_fournisseur"));
                m.setNom(rs.getString("nom"));
                m.setType(TypeMateriel.valueOf(rs.getString("type")));
                m.setQuantite(rs.getInt("quantite"));
                m.setEtat(EtatMateriel.valueOf(rs.getString("etat")));
                m.setPrix_unitaire(rs.getFloat("prix_unitaire"));
                m.setBarcode(rs.getString("barcode"));
                rs.getBytes("image_data");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}