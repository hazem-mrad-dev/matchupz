package models.joueur;

import javafx.beans.property.*;

import java.util.Date;

public class EvaluationPhysique {
    private final IntegerProperty idEvaluation;
    private final IntegerProperty idJoueur;
    private final ObjectProperty<Date> dateEvaluation;
    private final FloatProperty niveauEndurance;
    private final FloatProperty forcePhysique;
    private final FloatProperty vitesse;
    private final StringProperty etatBlessure;

    // Constructors
    public EvaluationPhysique() {
        this.idEvaluation = new SimpleIntegerProperty();
        this.idJoueur = new SimpleIntegerProperty();
        this.dateEvaluation = new SimpleObjectProperty<>();
        this.niveauEndurance = new SimpleFloatProperty();
        this.forcePhysique = new SimpleFloatProperty();
        this.vitesse = new SimpleFloatProperty();
        this.etatBlessure = new SimpleStringProperty();
    }

    public EvaluationPhysique(int idJoueur, Date dateEvaluation, float niveauEndurance, float forcePhysique, float vitesse, String etatBlessure) {
        this.idEvaluation = new SimpleIntegerProperty(); // Set later or auto-generated
        this.idJoueur = new SimpleIntegerProperty(idJoueur);
        this.dateEvaluation = new SimpleObjectProperty<>(dateEvaluation);
        this.niveauEndurance = new SimpleFloatProperty(niveauEndurance);
        this.forcePhysique = new SimpleFloatProperty(forcePhysique);
        this.vitesse = new SimpleFloatProperty(vitesse);
        this.etatBlessure = new SimpleStringProperty(etatBlessure);
    }


    // Getters for JavaFX properties
    public IntegerProperty idEvaluationProperty() {
        return idEvaluation;
    }

    public IntegerProperty idJoueurProperty() {
        return idJoueur;
    }

    public ObjectProperty<Date> dateEvaluationProperty() {
        return dateEvaluation;
    }

    public FloatProperty niveauEnduranceProperty() {
        return niveauEndurance;
    }

    public FloatProperty forcePhysiqueProperty() {
        return forcePhysique;
    }

    public FloatProperty vitesseProperty() {
        return vitesse;
    }

    public StringProperty etatBlessureProperty() {
        return etatBlessure;
    }

    // Standard Getters and Setters
    public int getIdEvaluation() {
        return idEvaluation.get();
    }

    public void setIdEvaluation(int idEvaluation) {
        this.idEvaluation.set(idEvaluation);
    }

    public int getIdJoueur() {
        return idJoueur.get();
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur.set(idJoueur);
    }

    public Date getDateEvaluation() {
        return dateEvaluation.get();
    }

    public void setDateEvaluation(Date dateEvaluation) {
        this.dateEvaluation.set(dateEvaluation);
    }

    public float getNiveauEndurance() {
        return niveauEndurance.get();
    }

    public void setNiveauEndurance(float niveauEndurance) {
        this.niveauEndurance.set(niveauEndurance);
    }

    public float getForcePhysique() {
        return forcePhysique.get();
    }

    public void setForcePhysique(float forcePhysique) {
        this.forcePhysique.set(forcePhysique);
    }

    public float getVitesse() {
        return vitesse.get();
    }

    public void setVitesse(float vitesse) {
        this.vitesse.set(vitesse);
    }

    public String getEtatBlessure() {
        return etatBlessure.get();
    }

    public void setEtatBlessure(String etatBlessure) {
        this.etatBlessure.set(etatBlessure);
    }
}
