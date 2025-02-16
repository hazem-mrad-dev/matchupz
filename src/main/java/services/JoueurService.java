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
        String req = "INSERT INTO `joueur` (nom, prenom, date_naissance, poste, taille, poids, statut, email, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.executeUpdate();
            System.out.println("Joueur ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Joueur joueur) {
        String req = "UPDATE `joueur` SET nom=?, prenom=?, date_naissance=?, poste=?, taille=?, poids=?, statut=?, email=?, telephone=? WHERE id_joueur=?";
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
            ps.setInt(10, joueur.getIdJoueur());
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
                Joueur joueur = new Joueur();
                joueur.setIdJoueur(rs.getInt("id_joueur"));
                joueur.setNom(rs.getString("nom"));
                joueur.setPrenom(rs.getString("prenom"));
                joueur.setDateNaissance(rs.getDate("date_naissance"));
                joueur.setPoste(rs.getString("poste"));
                joueur.setTaille(rs.getFloat("taille"));
                joueur.setPoids(rs.getFloat("poids"));
                joueur.setStatut(rs.getString("statut"));
                joueur.setEmail(rs.getString("email"));
                joueur.setTelephone(rs.getString("telephone"));
                joueurs.add(joueur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joueurs;
    }
}
