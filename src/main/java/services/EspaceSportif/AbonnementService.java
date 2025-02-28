package services.EspaceSportif;

import models.EspaceSportif.Abonnement;
import models.joueur.Club; // Import the Club model instead of Sport
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements EspaceService<Abonnement> {
    private Connection connection;

    public AbonnementService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouter(Abonnement abonnement) {
        try {
            String sql = "INSERT INTO abonnement (id_club, type_abonnement, date_debut, date_fin, tarif, etat) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdClub());
                stmt.setString(2, abonnement.getTypeAbonnement());
                stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
                stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
                stmt.setDouble(5, abonnement.getTarif());
                stmt.setString(6, abonnement.getEtat());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier(Abonnement abonnement) {
        try {
            String sql = "UPDATE abonnement SET id_club=?, type_abonnement=?, date_debut=?, date_fin=?, tarif=?, etat=? WHERE id_abonnement=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdClub());
                stmt.setString(2, abonnement.getTypeAbonnement());
                stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
                stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
                stmt.setDouble(5, abonnement.getTarif());
                stmt.setString(6, abonnement.getEtat());
                stmt.setInt(7, abonnement.getIdAbonnement());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(Abonnement abonnement) {
        try {
            String sql = "DELETE FROM abonnement WHERE id_abonnement=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdAbonnement());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Abonnement> rechercher() {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT a.id_abonnement, a.id_club, c.nom_club AS nomClub, a.type_abonnement, a.date_debut, a.date_fin, a.tarif, a.etat " +
                "FROM abonnement a " +
                "JOIN club c ON a.id_club = c.id_club";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Abonnement abonnement = new Abonnement(
                        rs.getInt("id_abonnement"),
                        rs.getInt("id_club"),
                        rs.getString("nomClub"),
                        rs.getString("type_abonnement"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getDouble("tarif"),
                        rs.getString("etat")
                );
                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des abonnements : " + e.getMessage(), e);
        }
        return abonnements;
    }

    public List<Abonnement> rechercher(int idClub) {
        List<Abonnement> abonnements = new ArrayList<>();
        try {
            String sql = "SELECT a.id_abonnement, a.id_club, c.nom_club AS nomClub, a.type_abonnement, a.date_debut, a.date_fin, a.tarif, a.etat " +
                    "FROM abonnement a " +
                    "JOIN club c ON a.id_club = c.id_club WHERE a.id_club = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, idClub);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Abonnement abonnement = new Abonnement(
                                rs.getInt("id_abonnement"),
                                rs.getInt("id_club"),
                                rs.getString("nomClub"),
                                rs.getString("type_abonnement"),
                                rs.getDate("date_debut"),
                                rs.getDate("date_fin"),
                                rs.getDouble("tarif"),
                                rs.getString("etat")
                        );
                        abonnements.add(abonnement);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des abonnements par club : " + e.getMessage(), e);
        }
        return abonnements;
    }
}