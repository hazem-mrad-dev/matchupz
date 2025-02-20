package services;
import models.Match;
import models.Personne;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchService implements  IService<Match>{
    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Match match) {
//        String req = "INSERT INTO `personne`( `nom`, `prenom`, `age`) VALUES ('"+personne.getNom()+"','"+personne.getPrenom()+"','"+personne.getAge()+"')";
//
//        try {
//            Statement st = connection.createStatement();
//            st.executeUpdate(req);
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//
//        }

        String req = "INSERT INTO `Matchs`( `C1`, `C2`,`SportType`) VALUES (?,?,?)";

        try {
            PreparedStatement ms = connection.prepareStatement(req);
            ms.setString(1,match.getC1());
            ms.setString(2, match.getC2());
            ms.setString(3, match.getSportType());
            ms.executeUpdate();
            System.out.println("Match ajouté");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Match match) {
        String req = "UPDATE `matchs` SET C1=?,C2=?,SportType=? WHERE idMatch =?";
        try {
            PreparedStatement ms = connection.prepareStatement(req);
            ms.setString(1, match.getC1());
            ms.setString(2, match.getC2());
            ms.setString(3, match.getSportType());
            ms.setInt(4, match.getIdMatch());
            ms.executeUpdate();
            System.out.println("Match modifié");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void supprimer(Match match) {
        String req = "DELETE FROM `Matchs` WHERE idMatch =?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1,match.getIdMatch());
            ps.executeUpdate();
            System.out.println("Match supprimé");
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public List<Match> rechercher() {
        String req = "SELECT * FROM `Matchs`";
        List<Match> matchs = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Match match = new Match();
                match.setIdMatch(rs.getInt("idMatch"));
                match.setC1(rs.getString("C1"));
                match.setC2(rs.getString("C2"));
                match.setSportType(rs.getString("SportType"));
                matchs.add(match);

            }
            System.out.println(matchs);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  matchs;
    }
}
