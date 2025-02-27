package services.joueur;

import models.joueur.Joueur;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoueurService implements IService<Joueur> {
    private Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(Joueur joueur) {
        String req = "INSERT INTO joueur (id_sport, nomSport, nom, prenom, date_naissance, poste, taille, poids, statut, email, telephone, profile_picture_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, joueur.getIdSport());
            ps.setString(2, joueur.getNomSport());
            ps.setString(3, joueur.getNom());
            ps.setString(4, joueur.getPrenom());
            ps.setDate(5, new java.sql.Date(joueur.getDateNaissance().getTime()));
            ps.setString(6, joueur.getPoste());
            ps.setFloat(7, joueur.getTaille());
            ps.setFloat(8, joueur.getPoids());
            ps.setString(9, joueur.getStatut());
            ps.setString(10, joueur.getEmail());
            ps.setString(11, joueur.getTelephone());
            ps.setString(12, joueur.getProfilePictureUrl());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                joueur.setIdJoueur(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Joueur joueur) {
        String req = "UPDATE joueur SET id_sport=?, nomSport=?, nom=?, prenom=?, date_naissance=?, poste=?, taille=?, poids=?, statut=?, email=?, telephone=?, profile_picture_url=? WHERE id_joueur=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, joueur.getIdSport());
            ps.setString(2, joueur.getNomSport());
            ps.setString(3, joueur.getNom());
            ps.setString(4, joueur.getPrenom());
            ps.setDate(5, new java.sql.Date(joueur.getDateNaissance().getTime()));
            ps.setString(6, joueur.getPoste());
            ps.setFloat(7, joueur.getTaille());
            ps.setFloat(8, joueur.getPoids());
            ps.setString(9, joueur.getStatut());
            ps.setString(10, joueur.getEmail());
            ps.setString(11, joueur.getTelephone());
            ps.setString(12, joueur.getProfilePictureUrl());
            ps.setInt(13, joueur.getIdJoueur());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Joueur joueur) {
        String req = "DELETE FROM joueur WHERE id_joueur=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, joueur.getIdJoueur());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Joueur> recherche() {
        List<Joueur> joueurs = new ArrayList<>();
        String query = "SELECT * FROM joueur";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Joueur joueur = new Joueur();
                joueur.setIdJoueur(rs.getInt("id_joueur"));
                joueur.setIdSport(rs.getInt("id_sport"));
                joueur.setNomSport(rs.getString("nomSport")); // Fixed to match table
                joueur.setNom(rs.getString("nom"));
                joueur.setPrenom(rs.getString("prenom"));
                joueur.setDateNaissance(rs.getDate("date_naissance"));
                joueur.setPoste(rs.getString("poste"));
                joueur.setTaille(rs.getFloat("taille"));
                joueur.setPoids(rs.getFloat("poids"));
                joueur.setStatut(rs.getString("statut"));
                joueur.setEmail(rs.getString("email"));
                joueur.setTelephone(rs.getString("telephone"));
                joueur.setProfilePictureUrl(rs.getString("profile_picture_url")); // Fixed to match table
                joueurs.add(joueur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve joueurs: " + e.getMessage());
        }
        return joueurs;
    }
}