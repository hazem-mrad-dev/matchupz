package services.joueur;

import models.joueur.StatistiquesPostMatch;
import services.IService;
import utils.MyDataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistiquesPostMatchService implements IService<StatistiquesPostMatch> {

    Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(StatistiquesPostMatch stat) {
        String req = "INSERT INTO `statistiquespostmatch` (id_joueur, id_match, buts_marques, passes_decisives, distance_parcourue, tirs_cadres, note_match) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, stat.getIdJoueur());
            ps.setInt(2, stat.getIdMatch());
            ps.setInt(3, stat.getButsMarques());
            ps.setInt(4, stat.getPassesDecisives());
            ps.setFloat(5, stat.getDistanceParcourue());
            ps.setInt(6, stat.getTirsCadres());
            ps.setFloat(7, stat.getNoteMatch());
            ps.executeUpdate();
            System.out.println("Statistique ajoutée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(StatistiquesPostMatch stat) {
        String req = "UPDATE `statistiquespostmatch` SET id_joueur=?, id_match=?, buts_marques=?, passes_decisives=?, distance_parcourue=?, tirs_cadres=?, note_match=? WHERE id_stat_post_match=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, stat.getIdJoueur());
            ps.setInt(2, stat.getIdMatch());
            ps.setInt(3, stat.getButsMarques());
            ps.setInt(4, stat.getPassesDecisives());
            ps.setFloat(5, stat.getDistanceParcourue());
            ps.setInt(6, stat.getTirsCadres());
            ps.setFloat(7, stat.getNoteMatch());
            ps.setInt(8, stat.getIdStatPostMatch());
            ps.executeUpdate();
            System.out.println("Statistique modifiée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(StatistiquesPostMatch stat) {
        String req = "DELETE FROM `statistiquespostmatch` WHERE id_stat_post_match=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, stat.getIdStatPostMatch());
            ps.executeUpdate();
            System.out.println("Statistique supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<StatistiquesPostMatch> recherche() {
        String req = "SELECT * FROM `statistiquespostmatch`";
        List<StatistiquesPostMatch> statsList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StatistiquesPostMatch stat = new StatistiquesPostMatch();
                stat.setIdStatPostMatch(rs.getInt("id_stat_post_match"));
                stat.setIdJoueur(rs.getInt("id_joueur"));
                stat.setIdMatch(rs.getInt("id_match"));
                stat.setButsMarques(rs.getInt("buts_marques"));
                stat.setPassesDecisives(rs.getInt("passes_decisives"));
                stat.setDistanceParcourue(rs.getFloat("distance_parcourue"));
                stat.setTirsCadres(rs.getInt("tirs_cadres"));
                stat.setNoteMatch(rs.getFloat("note_match"));
                statsList.add(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statsList;
    }
}
