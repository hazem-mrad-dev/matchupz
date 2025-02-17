package services;

import models.Sponsor;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SponsorService implements IService<Sponsor> {

    Connection connection = MyDatabase.getInstance().getConn();

    public SponsorService() {}

    @Override
    public void ajouter(Sponsor sponsor) {
        String checkNameQuery = "SELECT COUNT(*) FROM `sponsors` WHERE `nom` = ?";
        String insertQuery = "INSERT INTO `sponsors`( `nom`, `contact`, `pack`) VALUES (?,?,?)";

        try {
            PreparedStatement checkPs = this.connection.prepareStatement(checkNameQuery);
            checkPs.setString(1, sponsor.getNom());
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                System.out.println("Error: Sponsor name doit etre unique!");
                return;
            }

            if (!sponsor.getContact().matches("\\d{8}")) {
                System.out.println("Error: Contact doit etre 8 digits!");
                return;
            }

            if (!sponsor.getPack().equalsIgnoreCase("bronze") &&
                    !sponsor.getPack().equalsIgnoreCase("silver") &&
                    !sponsor.getPack().equalsIgnoreCase("gold")) {
                System.out.println("Error: Les packs doivent etre soit bronze, silver, ou gold!");
                return;
            }

            PreparedStatement ps = this.connection.prepareStatement(insertQuery);
            ps.setString(1, sponsor.getNom());
            ps.setString(2, sponsor.getContact());
            ps.setString(3, sponsor.getPack());
            ps.executeUpdate();
            System.out.println("Sponsor ajoute!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void modifier(Sponsor sponsor) {
        String checkNameQuery = "SELECT COUNT(*) FROM `sponsors` WHERE `nom` = ? AND `id_sponsor` != ?";
        String updateQuery = "UPDATE `sponsors` SET nom=?, contact=?, pack=? WHERE id_sponsor=?";

        try {
            PreparedStatement checkPs = this.connection.prepareStatement(checkNameQuery);
            checkPs.setString(1, sponsor.getNom());
            checkPs.setInt(2, sponsor.getId_sponsor());
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                System.out.println("Error: Sponsor name doit etre unique!");
                return;
            }

            if (!sponsor.getContact().matches("\\d{8}")) {
                System.out.println("Error: Contact doit etre 8 digits!");
                return;
            }

            if (!sponsor.getPack().equalsIgnoreCase("bronze") &&
                    !sponsor.getPack().equalsIgnoreCase("silver") &&
                    !sponsor.getPack().equalsIgnoreCase("gold")) {
                System.out.println("Error: Les packs doivent etre soit bronze, silver, ou gold!");
                return;
            }

            PreparedStatement ps = this.connection.prepareStatement(updateQuery);
            ps.setString(1, sponsor.getNom());
            ps.setString(2, sponsor.getContact());
            ps.setString(3, sponsor.getPack());
            ps.setInt(4, sponsor.getId_sponsor());
            ps.executeUpdate();
            System.out.println("Sponsor modifie!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void supprimer(Sponsor sponsor) {

        String req = "DELETE FROM `sponsors` WHERE id_sponsor =?";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, sponsor.getId_sponsor());
            ps.executeUpdate();
            System.out.println("Sponsor supprime!");
        } catch (Exception var4) {
            Exception e = var4;
            e.printStackTrace();
        }


    }

    @Override
    public List<Sponsor> rechercher() {
        String req = "SELECT * FROM `sponsors`";
        List<Sponsor> sponsors = new ArrayList();

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setId_sponsor(rs.getInt("id_sponsor"));
                sponsor.setNom(rs.getString("nom"));
                sponsor.setContact(rs.getString("contact"));
                sponsor.setPack(rs.getString("pack"));
                sponsors.add(sponsor);
            }

            System.out.println(sponsors);
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
        }

        return sponsors;
    }

}
