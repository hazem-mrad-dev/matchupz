package services;

import models.Joueur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoueurService implements IService<Joueur> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Joueur joueur) {
        String req = "INSERT INTO `joueur` (nom, prenom, date_naissance, poste, taille, poids, statut, email, telephone, id_sport) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, joueur.getNom());
            ps.setString(2, joueur.getPrenom());
            ps.setDate(3, new java.sql.Date(joueur.getDateNaissance().getTime()));
            ps.setString(4, joueur.getPoste());
            ps.setFloat(5, joueur.getTaille());
            ps.setFloat(6, joueur.getPoids());
            ps.setString(7, joueur.getStatut());
            ps.setString(8, joueur.getEmail());
            ps.setString(9, joueur.getTelephone());
            ps.setInt(10, joueur.getIdSport()); // Include the sport ID
            ps.executeUpdate();
            System.out.println("Joueur ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Joueur joueur) {
        String req = "UPDATE `joueur` SET nom=?, prenom=?, date_naissance=?, poste=?, taille=?, poids=?, statut=?, email=?, telephone=?, id_sport=? WHERE id_joueur=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, joueur.getNom());
            ps.setString(2, joueur.getPrenom());
            ps.setDate(3, new java.sql.Date(joueur.getDateNaissance().getTime()));  // Correct usage
            ps.setString(4, joueur.getPoste());
            ps.setFloat(5, joueur.getTaille());
            ps.setFloat(6, joueur.getPoids());
            ps.setString(7, joueur.getStatut());
            ps.setString(8, joueur.getEmail());
            ps.setString(9, joueur.getTelephone());
            ps.setInt(10, joueur.getIdSport()); // Include the sport ID
            ps.setInt(11, joueur.getIdJoueur()); // Set the ID of the player for the WHERE clause
            ps.executeUpdate();
            System.out.println("Joueur modifié");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void supprimer(Joueur joueur) {
        String req = "DELETE FROM `joueur` WHERE id_joueur=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, joueur.getIdJoueur());
            ps.executeUpdate();
            System.out.println("Deleting joueur with id_joueur: " + joueur.getIdJoueur());
            System.out.println("Joueur supprimé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Joueur> rechercher() {
        String req = "SELECT * FROM `joueur`";
        List<Joueur> joueurs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Joueur joueur = new Joueur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance"),
                        rs.getString("poste"),
                        rs.getFloat("taille"),
                        rs.getFloat("poids"),
                        rs.getString("statut"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getInt("id_sport")
                );
                joueur.setIdJoueur(rs.getInt("id_joueur")); // Set ID separately if needed
                joueurs.add(joueur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joueurs;
    }

}
