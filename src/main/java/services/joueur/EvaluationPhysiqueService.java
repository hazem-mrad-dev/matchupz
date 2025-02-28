package services.joueur;

import models.joueur.EvaluationPhysique;
import services.IService;
import utils.MyDataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluationPhysiqueService implements IService<EvaluationPhysique> {

    Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(EvaluationPhysique evaluation) {
        String req = "INSERT INTO `evaluationphysique` (id_joueur, date_evaluation, niveau_endurance, force_physique, vitesse, etat_blessure) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, evaluation.getIdJoueur());
            ps.setDate(2, new java.sql.Date(evaluation.getDateEvaluation().getTime()));
            ps.setFloat(3, evaluation.getNiveauEndurance());
            ps.setFloat(4, evaluation.getForcePhysique());
            ps.setFloat(5, evaluation.getVitesse());
            ps.setString(6, evaluation.getEtatBlessure());
            ps.executeUpdate();
            System.out.println("Évaluation physique ajoutée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(EvaluationPhysique evaluation) {
        String req = "UPDATE `evaluationphysique` SET id_joueur=?, date_evaluation=?, niveau_endurance=?, force_physique=?, vitesse=?, etat_blessure=? WHERE id_evaluation=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, evaluation.getIdJoueur());
            ps.setDate(2, new java.sql.Date(evaluation.getDateEvaluation().getTime()));
            ps.setFloat(3, evaluation.getNiveauEndurance());
            ps.setFloat(4, evaluation.getForcePhysique());
            ps.setFloat(5, evaluation.getVitesse());
            ps.setString(6, evaluation.getEtatBlessure());
            ps.setInt(7, evaluation.getIdEvaluation());
            ps.executeUpdate();
            System.out.println("Évaluation physique modifiée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(EvaluationPhysique evaluation) {
        String req = "DELETE FROM `evaluationphysique` WHERE id_evaluation=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, evaluation.getIdEvaluation());
            ps.executeUpdate();
            System.out.println("Évaluation physique supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EvaluationPhysique> recherche() {
        String req = "SELECT * FROM `evaluationphysique`";
        List<EvaluationPhysique> evaluations = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EvaluationPhysique evaluation = new EvaluationPhysique();
                evaluation.setIdEvaluation(rs.getInt("id_evaluation"));
                evaluation.setIdJoueur(rs.getInt("id_joueur"));
                evaluation.setDateEvaluation(rs.getDate("date_evaluation"));
                evaluation.setNiveauEndurance(rs.getFloat("niveau_endurance"));
                evaluation.setForcePhysique(rs.getFloat("force_physique"));
                evaluation.setVitesse(rs.getFloat("vitesse"));
                evaluation.setEtatBlessure(rs.getString("etat_blessure"));
                evaluations.add(evaluation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evaluations;
    }
}
