package models;

import java.util.Date;

public class EvaluationPhysique {
    private int idEvaluation;
    private int idJoueur; // Foreign key reference
    private Date dateEvaluation;
    private float niveauEndurance;
    private float forcePhysique;
    private float vitesse;
    private String etatBlessure;

    public EvaluationPhysique(int idEvaluation, int idJoueur, Date dateEvaluation, float niveauEndurance,
                              float forcePhysique, float vitesse, String etatBlessure) {
        this.idEvaluation = idEvaluation;
        this.idJoueur = idJoueur;
        this.dateEvaluation = dateEvaluation;
        this.niveauEndurance = niveauEndurance;
        this.forcePhysique = forcePhysique;
        this.vitesse = vitesse;
        this.etatBlessure = etatBlessure;
    }

    public EvaluationPhysique() {

    }

    public int getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(int idEvaluation) {
        this.idEvaluation = idEvaluation;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public Date getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public float getNiveauEndurance() {
        return niveauEndurance;
    }

    public void setNiveauEndurance(float niveauEndurance) {
        this.niveauEndurance = niveauEndurance;
    }

    public float getForcePhysique() {
        return forcePhysique;
    }

    public void setForcePhysique(float forcePhysique) {
        this.forcePhysique = forcePhysique;
    }

    public float getVitesse() {
        return vitesse;
    }

    public void setVitesse(float vitesse) {
        this.vitesse = vitesse;
    }

    public String getEtatBlessure() {
        return etatBlessure;
    }

    public void setEtatBlessure(String etatBlessure) {
        this.etatBlessure = etatBlessure;
    }
}
