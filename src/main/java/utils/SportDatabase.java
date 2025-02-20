package utils;

import models.Sport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDatabase {

    // Fetch all sports from the database
    public List<Sport> fetchSports() {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT id_sport, nom_sport, description FROM sport";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idSport = rs.getInt("nom_sport");
                if (rs.wasNull()) {
                    idSport = -1;  // Handle the case if the ID is null
                }
                // Create Sport object
                Sport sport = new Sport(
                        idSport,
                        rs.getString("nom_sport"),
                        rs.getString("description")
                );
                sports.add(sport);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sports;
    }

    // Fetch a single sport by ID
    public Sport getSportById(int idSport) {
        String sql = "SELECT id_sport, nom_sport, description FROM sport WHERE id_sport = ?";
        Sport sport = null;

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSport);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sport = new Sport(
                            rs.getInt("id_sport"),
                            rs.getString("nom_sport"),
                            rs.getString("description")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sport;
    }
}
