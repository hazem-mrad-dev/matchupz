package services;

import models.Sport;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportService implements IService<Sport> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Sport sport) {
        String req = "INSERT INTO `sport` (nom, description, type) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, sport.getNom());
            ps.setString(2, sport.getDescription());
            ps.setString(3, sport.getType());
            ps.executeUpdate();
            System.out.println("Sport ajouté: " + sport.getNom());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Sport sport) {
        String req = "UPDATE `sport` SET nom=?, description=?, type=? WHERE id_sport=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, sport.getNom());
            ps.setString(2, sport.getDescription());
            ps.setString(3, sport.getType());
            ps.setInt(4, sport.getIdSport());
            ps.executeUpdate();
            System.out.println("Sport modifié: " + sport.getNom());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Sport sport) {
        String req = "DELETE FROM `sport` WHERE id_sport=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, sport.getIdSport());
            ps.executeUpdate();
            System.out.println("Sport supprimé avec ID: " + sport.getIdSport());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sport> rechercher() {
        String req = "SELECT * FROM `sport`";
        List<Sport> sports = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sport sport = new Sport();
                sport.setIdSport(rs.getInt("id_sport"));
                sport.setNom(rs.getString("nom"));
                sport.setDescription(rs.getString("description"));
                sport.setType(rs.getString("type"));
                sports.add(sport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sports;
    }
}
