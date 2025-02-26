package services.joueur;

import models.joueur.HistoriqueClub;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueClubService implements IService<HistoriqueClub> {
    private Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(HistoriqueClub historique) {
        String req = "INSERT INTO historiqueclub (id_joueur, id_club, saison_debut, saison_fin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, historique.getIdJoueur());
            ps.setInt(2, historique.getIdClub());
            ps.setDate(3, new java.sql.Date(historique.getSaisonDebut().getTime()));
            ps.setDate(4, historique.getSaisonFin() != null ? new java.sql.Date(historique.getSaisonFin().getTime()) : null);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                historique.setIdHistorique(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(HistoriqueClub historique) {
        String req = "UPDATE historiqueclub SET id_joueur=?, id_club=?, saison_debut=?, saison_fin=? WHERE id_historique=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, historique.getIdJoueur());
            ps.setInt(2, historique.getIdClub());
            ps.setDate(3, new java.sql.Date(historique.getSaisonDebut().getTime()));
            ps.setDate(4, historique.getSaisonFin() != null ? new java.sql.Date(historique.getSaisonFin().getTime()) : null);
            ps.setInt(5, historique.getIdHistorique());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(HistoriqueClub historique) {
        String req = "DELETE FROM historiqueclub WHERE id_historique=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, historique.getIdHistorique());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HistoriqueClub> recherche() {
        List<HistoriqueClub> historiques = new ArrayList<>();
        String req = "SELECT * FROM historiqueclub";
        try (PreparedStatement ps = connection.prepareStatement(req); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HistoriqueClub historique = new HistoriqueClub(
                        rs.getInt("id_historique"),
                        rs.getInt("id_joueur"),
                        rs.getInt("id_club"),
                        rs.getDate("saison_debut"),
                        rs.getDate("saison_fin")
                );
                historiques.add(historique);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiques;
    }
}