package services.joueur;

import models.joueur.PerformanceJoueur;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceJoueurService implements IService<PerformanceJoueur> {

    Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(PerformanceJoueur performance) {
        // Exclude id_performance from the column list since it's auto-generated
        String req = "INSERT INTO `performancejoueur` (id_joueur, saison, nombre_matches, minutes_jouees, buts_marques, passes_decisives, cartons_jaunes, cartons_rouges) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // Use Statement.RETURN_GENERATED_KEYS if you want to capture the auto-generated id_performance
            PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, performance.getIdJoueur());
            ps.setString(2, performance.getSaison());
            ps.setInt(3, performance.getNombreMatches());
            ps.setInt(4, performance.getMinutesJouees());
            ps.setInt(5, performance.getButsMarques());
            ps.setInt(6, performance.getPassesDecisives());
            ps.setInt(7, performance.getCartonsJaunes());
            ps.setInt(8, performance.getCartonsRouges());
            ps.executeUpdate();
            System.out.println("Performance ajoutée");

            // Optionally retrieve and set the generated id_performance
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                performance.setIdPerformance(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(PerformanceJoueur performance) {
        String req = "UPDATE `performancejoueur` SET id_joueur=?, saison=?, nombre_matches=?, minutes_jouees=?, buts_marques=?, passes_decisives=?, cartons_jaunes=?, cartons_rouges=? WHERE id_performance=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, performance.getIdJoueur());
            ps.setString(2, performance.getSaison());
            ps.setInt(3, performance.getNombreMatches());
            ps.setInt(4, performance.getMinutesJouees());
            ps.setInt(5, performance.getButsMarques());
            ps.setInt(6, performance.getPassesDecisives());
            ps.setInt(7, performance.getCartonsJaunes());
            ps.setInt(8, performance.getCartonsRouges());
            ps.setInt(9, performance.getIdPerformance());
            ps.executeUpdate();
            System.out.println("Performance modifiée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(PerformanceJoueur performance) {
        String req = "DELETE FROM `performancejoueur` WHERE id_performance=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, performance.getIdPerformance());
            ps.executeUpdate();
            System.out.println("Performance supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PerformanceJoueur> recherche() {
        String req = "SELECT * FROM `performancejoueur`";
        List<PerformanceJoueur> performances = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PerformanceJoueur performance = new PerformanceJoueur();
                performance.setIdPerformance(rs.getInt("id_performance"));
                performance.setIdJoueur(rs.getInt("id_joueur"));
                performance.setSaison(rs.getString("saison"));
                performance.setNombreMatches(rs.getInt("nombre_matches"));
                performance.setMinutesJouees(rs.getInt("minutes_jouees"));
                performance.setButsMarques(rs.getInt("buts_marques"));
                performance.setPassesDecisives(rs.getInt("passes_decisives"));
                performance.setCartonsJaunes(rs.getInt("cartons_jaunes"));
                performance.setCartonsRouges(rs.getInt("cartons_rouges"));
                performances.add(performance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return performances;
    }
}