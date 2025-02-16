package services;

import models.HistoriqueClub;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueClubService implements IService<HistoriqueClub> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(HistoriqueClub historique) {
        String req = "INSERT INTO `historiqueclub` (id_joueur, nom_club, saison_debut, saison_fin) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, historique.getIdJoueur());
            ps.setString(2, historique.getNomClub());
            ps.setDate(3, new java.sql.Date(historique.getSaisonDebut().getTime()));
            ps.setDate(4, new java.sql.Date(historique.getSaisonFin().getTime()));
            ps.executeUpdate();
            System.out.println("Historique du club ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(HistoriqueClub historique) {
        String req = "UPDATE `historiqueclub` SET id_joueur=?, nom_club=?, saison_debut=?, saison_fin=? WHERE id_historique=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, historique.getIdJoueur());
            ps.setString(2, historique.getNomClub());
            ps.setDate(3, new java.sql.Date(historique.getSaisonDebut().getTime()));
            ps.setDate(4, new java.sql.Date(historique.getSaisonFin().getTime()));
            ps.setInt(5, historique.getIdHistorique());
            ps.executeUpdate();
            System.out.println("Historique du club modifié");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(HistoriqueClub historique) {
        String req = "DELETE FROM `historiqueclub` WHERE id_historique=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, historique.getIdHistorique());
            ps.executeUpdate();
            System.out.println("Historique du club supprimé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HistoriqueClub> rechercher() {
        String req = "SELECT * FROM `historiqueclub`";
        List<HistoriqueClub> historiques = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HistoriqueClub historique = new HistoriqueClub();
                historique.setIdHistorique(rs.getInt("id_historique"));
                historique.setIdJoueur(rs.getInt("id_joueur"));
                historique.setNomClub(rs.getString("nom_club"));
                historique.setSaisonDebut(rs.getDate("saison_debut"));
                historique.setSaisonFin(rs.getDate("saison_fin"));
                historiques.add(historique);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiques;
    }
}