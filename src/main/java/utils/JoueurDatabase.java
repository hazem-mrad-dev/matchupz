package utils;

import models.Joueur;
import models.Sport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoueurDatabase {
    private SportDatabase sportDatabase = new SportDatabase(); // Assuming you have this class

    // Fetch joueurs method
    public List<Joueur> fetchJoueurs() {
        List<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT j.id_joueur, j.nom, j.prenom, j.date_naissance, j.poste, j.taille, j.poids, j.statut, j.email, j.telephone, j.id_sport, s.nom_sport " +
                "FROM joueur j " +
                "JOIN sport s ON j.id_sport = s.id_sport";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Debugging: Check if data is being retrieved correctly
                System.out.println("Fetched data for joueur ID: " + rs.getInt("id_joueur"));

                String sportName = rs.getString("nom_sport");
                Sport sport = new Sport(rs.getInt("id_sport"), sportName, "");
                Joueur joueur = new Joueur(
                        rs.getInt("id_joueur"),
                        sport,
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance"),
                        rs.getString("poste"),
                        rs.getFloat("taille"),
                        rs.getFloat("poids"),
                        rs.getString("statut"),
                        rs.getString("email"),
                        rs.getString("telephone")
                );
                joueurs.add(joueur);
            }


            // Debugging: Print fetched data
            for (Joueur joueur : joueurs) {
                System.out.println("Joueur: " + joueur.getNom() + ", Sport: " + joueur.getSport().getNomSport());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joueurs;
    }

    public void deleteJoueur(int joueurId) {
        String query = "DELETE FROM joueur WHERE id_joueur = ?";
        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, joueurId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean updateJoueur(Joueur joueur) {
        String sql = "UPDATE joueur SET nom = ?, prenom = ?, date_naissance = ?, poste = ?, taille = ?, poids = ?, statut = ?, email = ?, telephone = ?, id_sport = ? " +
                "WHERE id_joueur = ?";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set parameters for the update statement
            ps.setString(1, joueur.getNom());
            ps.setString(2, joueur.getPrenom());
            ps.setDate(3, new java.sql.Date(joueur.getDateNaissance().getTime())); // Assuming dateNaissance is a java.util.Date
            ps.setString(4, joueur.getPoste());
            ps.setFloat(5, joueur.getTaille());
            ps.setFloat(6, joueur.getPoids());
            ps.setString(7, joueur.getStatut());
            ps.setString(8, joueur.getEmail());
            ps.setString(9, joueur.getTelephone());
            ps.setInt(10, joueur.getSport().getIdSport()); // Update the sport ID
            ps.setInt(11, joueur.getIdJoueur()); // Ensure we update the correct joueur by ID

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // If rows are updated, return true

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if something goes wrong
        }
    }

    // Display joueur details (Simplified)
    public void displayJoueurs(List<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            System.out.println("ID: " + joueur.getIdJoueur());
            System.out.println("Nom: " + joueur.getNom());
            System.out.println("Prenom: " + joueur.getPrenom());
            System.out.println("Date Naissance: " + joueur.getDateNaissance());
            System.out.println("Poste: " + joueur.getPoste());
            System.out.println("Sport: " + joueur.getSport().getNomSport());
            System.out.println("Email: " + joueur.getEmail());
            System.out.println("------------------------------------------------");
        }
    }
}
