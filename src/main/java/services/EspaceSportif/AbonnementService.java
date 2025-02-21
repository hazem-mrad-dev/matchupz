package services.EspaceSportif;

import models.EspaceSportif.Abonnement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService {
    private Connection connection;

    public AbonnementService(Connection connection) {
        this.connection = connection;
    }

    public void ajouter(Abonnement abonnement) throws SQLException {
        String sql = "INSERT INTO abonnement (idSport, type_abonnement, date_debut, date_fin, tarif, etat) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, abonnement.getIdSport());
        stmt.setString(2, abonnement.getTypeAbonnement());
        stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
        stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
        stmt.setDouble(5, abonnement.getTarif());
        stmt.setString(6, abonnement.getEtat());
        stmt.executeUpdate();
    }

    public void modifier(Abonnement abonnement) throws SQLException {
        String sql = "UPDATE abonnement SET idSport=?, type_abonnement=?, date_debut=?, date_fin=?, tarif=?, etat=? WHERE id_abonnement=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, abonnement.getIdSport());
        stmt.setString(2, abonnement.getTypeAbonnement());
        stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
        stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
        stmt.setDouble(5, abonnement.getTarif());
        stmt.setString(6, abonnement.getEtat());
        stmt.setInt(7, abonnement.getIdAbonnement());
        stmt.executeUpdate();
    }

    public void supprimer(int idAbonnement) throws SQLException {
        String sql = "DELETE FROM abonnement WHERE id_abonnement=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idAbonnement);
        stmt.executeUpdate();
    }

    public List<Abonnement> lister() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Abonnement abonnement = new Abonnement(
                    rs.getInt("idSport"),
                    rs.getString("type_abonnement"),
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getDouble("tarif"),
                    rs.getString("etat")
            );
            abonnement.setIdAbonnement(rs.getInt("id_abonnement"));
            abonnements.add(abonnement);
        }
        return abonnements;
    }
}
