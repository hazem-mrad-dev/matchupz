package services.match;
import models.EspaceSportif.EspaceSportif;
import models.match.Schedule;
import services.IService;
import utils.MyDataSource;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class ScheduleService implements IService<Schedule> {
    Connection connection =  MyDataSource.getInstance().getConn();

    @Override
    /*public void ajouter(Schedule schedule) {
        String query = "INSERT INTO schedules (idSchedule, dateMatch, startTime, endTime, idMatchFK) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ss = connection.prepareStatement(query)) {
            ss.setInt(1, schedule.getIdSchedule());
            ss.setDate(2, java.sql.Date.valueOf(schedule.getDateMatch()));
            ss.setTime(3, java.sql.Time.valueOf(schedule.getStartTime()));
            ss.setTime(4, java.sql.Time.valueOf(schedule.getEndTime()));
            ss.setInt(5, schedule.getIdMatchFK());
            ss.executeUpdate();
            System.out.println("Planning ajouté .");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    public void ajouter(Schedule schedule) {
        if (schedule.getStartTime().isBefore(schedule.getEndTime())) {
            String query = "INSERT INTO schedules (idSchedule, dateMatch, startTime, endTime, idMatchFK, idLieu) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ss = connection.prepareStatement(query)) {
                ss.setInt(1, schedule.getIdSchedule());
                ss.setDate(2, java.sql.Date.valueOf(schedule.getDateMatch()));
                ss.setTime(3, java.sql.Time.valueOf(schedule.getStartTime()));
                ss.setTime(4, java.sql.Time.valueOf(schedule.getEndTime()));
                ss.setInt(5, schedule.getIdMatchFK());
                ss.setInt(6,schedule.getIdLieu());
                ss.executeUpdate();
                System.out.println("Planning ajouté.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur: L'heure de début doit être avant l'heure de fin.");
        }
    }


    @Override
    public void modifier(Schedule schedule) {
        String req = "UPDATE `schedules` SET dateMatch=?, startTime=?, endTime=?, idMatchFK=?, idLieu=? WHERE idSchedule=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setObject(1, schedule.getDateMatch());
            ps.setObject(2, schedule.getStartTime());
            ps.setObject(3, schedule.getEndTime());
            ps.setInt(4, schedule.getIdMatchFK());
            ps.setInt(5,schedule.getIdLieu());
            ps.setInt(6, schedule.getIdSchedule());
            ps.executeUpdate();
            System.out.println("Planning modifié");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Schedule schedule) {
        String req = "DELETE FROM `schedules` WHERE idSchedule =?";

        try {
            PreparedStatement ss = connection.prepareStatement(req);
            ss.setInt(1, schedule.getIdSchedule());
            ss.executeUpdate();
            System.out.println("Planning supprimé");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public List<Schedule> rechercher() {
        String req = "SELECT * FROM `schedules`";
        List<Schedule> schedules = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setIdSchedule(rs.getInt("idSchedule"));
                schedule.setDateMatch(rs.getObject("dateMatch", LocalDate.class)); // Using LocalDate for dateMatch
                schedule.setStartTime(rs.getObject("startTime", LocalTime.class)); // Using LocalTime for startTime
                schedule.setEndTime(rs.getObject("endTime", LocalTime.class)); // Using LocalTime for endTime
                schedule.setIdMatchFK(rs.getInt("idMatchFK")); // Foreign key for the match
                schedules.add(schedule);
            }
            System.out.println(schedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }*/
    /*public List<Schedule> rechercher() {
        String scheduleQuery = "SELECT * FROM `schedules`";
        List<Schedule> schedules = new ArrayList<>();

        try {
            PreparedStatement psSchedule = connection.prepareStatement(scheduleQuery);
            ResultSet rsSchedule = psSchedule.executeQuery();

            while (rsSchedule.next()) {
                Schedule schedule = new Schedule();
                schedule.setIdSchedule(rsSchedule.getInt("idSchedule"));
                schedule.setDateMatch(rsSchedule.getObject("dateMatch", LocalDate.class));
                schedule.setStartTime(rsSchedule.getObject("startTime", LocalTime.class));
                schedule.setEndTime(rsSchedule.getObject("endTime", LocalTime.class));
                schedule.setIdMatchFK(rsSchedule.getInt("idMatchFK"));

                // Fetch the corresponding Match just to display
                String matchQuery = "SELECT * FROM `matchs` WHERE idMatch = ?";
                PreparedStatement psMatch = connection.prepareStatement(matchQuery);
                psMatch.setInt(1, schedule.getIdMatchFK());
                ResultSet rsMatch = psMatch.executeQuery();

                String matchDisplay = "";
                if (rsMatch.next()) {
                    matchDisplay = "Match{idMatch=" + rsMatch.getInt("idMatch") +
                            ", C1='" + rsMatch.getString("C1") + '\'' +
                            ", C2='" + rsMatch.getString("C2") + '\'' +
                            ", SportType='" + rsMatch.getString("SportType") + '\'' +
                            '}';
                }

                // Display Schedule + Match details immediately
                System.out.println(schedule.toString() + " -> " + matchDisplay);

                schedules.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedules;
    }*/

    public List<Schedule> recherche() {
        String scheduleQuery = "SELECT * FROM `schedules`";
        List<Schedule> schedules = new ArrayList<>();

        try {
            PreparedStatement psSchedule = connection.prepareStatement(scheduleQuery);
            ResultSet rsSchedule = psSchedule.executeQuery();

            while (rsSchedule.next()) {
                Schedule schedule = new Schedule();
                schedule.setIdSchedule(rsSchedule.getInt("idSchedule"));
                schedule.setDateMatch(rsSchedule.getObject("dateMatch", LocalDate.class));
                schedule.setStartTime(rsSchedule.getObject("startTime", LocalTime.class));
                schedule.setEndTime(rsSchedule.getObject("endTime", LocalTime.class));
                schedule.setIdMatchFK(rsSchedule.getInt("idMatchFK"));
                schedule.setIdLieu(rsSchedule.getInt("idLieu"));

                String matchQuery = "SELECT * FROM `matchs` WHERE idMatch = ?";
                PreparedStatement psMatch = connection.prepareStatement(matchQuery);
                psMatch.setInt(1, schedule.getIdMatchFK());
                ResultSet rsMatch = psMatch.executeQuery();

                String matchDisplay = "";
                if (rsMatch.next()) {
                    matchDisplay = "Match{idMatch=" + rsMatch.getInt("idMatch") +
                            ", C1='" + rsMatch.getString("C1") + '\'' +
                            ", C2='" + rsMatch.getString("C2") + '\'' +
                            ", SportType='" + rsMatch.getString("SportType") + '\'' +
                            '}';
                }
                String lieuQuery = "SELECT * FROM espacesportif WHERE id_lieu = ?";
                PreparedStatement psLieu = connection.prepareStatement(lieuQuery);
                psLieu.setInt(1, schedule.getIdLieu());
                ResultSet rsLieu = psLieu.executeQuery();

                String lieuDisplay = "";
                if (rsLieu.next()) {
                    lieuDisplay = "Lieu{idLieu=" + rsLieu.getInt("id_lieu") +
                            ", Nom='" + rsLieu.getString("nom_espace") + '\'' +
                            ", Adresse='" + rsLieu.getString("adresse") + '\'' +
                            ", Categorie='" + rsLieu.getString("categorie") + '\'' +
                            ", Capacite=" + rsLieu.getInt("capacite") +
                            '}';
                }

                System.out.println(schedule.toString() + " -> " + matchDisplay + " -> " + lieuDisplay);
                schedules.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedules;
    }
    public List<EspaceSportif> getAllEspacesSportifs() {
        List<EspaceSportif> espacesList = new ArrayList<>();

        String query = "SELECT id_lieu, nom_espace FROM espacesportif";  // Modify the query as per your database schema
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id_lieu");
                String nom = rs.getString("nom_espace");
                EspaceSportif espace = new EspaceSportif(id, nom);
                espacesList.add(espace);
                System.out.println("Fetched espaces: " + espacesList);  // Debugging line

            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error properly in your real project
        }

        return espacesList;
    }











}
