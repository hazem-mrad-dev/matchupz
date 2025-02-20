package services;

import models.Personne;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PersonneService implements IService<Personne> {

    Connection connection = MyDatabase.getInstance().getConnection();


    @Override
    public void ajouter(Personne personne) {
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

        String req = "INSERT INTO `personne`( `nom`, `prenom`, `age`) VALUES (?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,personne.getNom());
            ps.setString(2,personne.getPrenom());
            ps.setInt(3,personne.getAge());
            ps.executeUpdate();
            System.out.println("Personne ajouté");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void modifier(Personne personne) {
        String req = "UPDATE `personne` SET nom=?,prenom=?,age =? WHERE id =?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,personne.getNom());
            ps.setString(2,personne.getPrenom());
            ps.setInt(3,personne.getAge());
            ps.setInt(4,personne.getId());
            ps.executeUpdate();
            System.out.println("Personne modifié");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void supprimer(Personne personne) {
        String req = "DELETE FROM `personne` WHERE id =?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1,personne.getId());
            ps.executeUpdate();
            System.out.println("Personne supprimé");
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public List<Personne> rechercher() {
        String req = "SELECT * FROM `personne`";
        List<Personne> personnes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Personne personne = new Personne();
                personne.setId(rs.getInt("id"));
                personne.setNom(rs.getString("nom"));
                personne.setPrenom(rs.getString("prenom"));
                personne.setAge(rs.getInt("age"));
                personnes.add(personne);

            }
            System.out.println(personnes);

        }
        catch (Exception e) {
        e.printStackTrace();
        }

        return  personnes;
    }
}
