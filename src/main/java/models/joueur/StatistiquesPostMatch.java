package models.joueur;

public class StatistiquesPostMatch {
    private int idStatPostMatch;
    private int idJoueur; // Foreign key reference
    private int idMatch; // Foreign key reference
    private int butsMarques;
    private int passesDecisives;
    private float distanceParcourue;
    private int tirsCadres;
    private float noteMatch;

    public StatistiquesPostMatch(int idStatPostMatch, int idJoueur, int idMatch, int butsMarques, int passesDecisives, float distanceParcourue, int tirsCadres, float noteMatch) {
        this.idStatPostMatch = idStatPostMatch;
        this.idJoueur = idJoueur;
        this.idMatch = idMatch;
        this.butsMarques = butsMarques;
        this.passesDecisives = passesDecisives;
        this.distanceParcourue = distanceParcourue;
        this.tirsCadres = tirsCadres;
        this.noteMatch = noteMatch;
    }

    public StatistiquesPostMatch() {

    }

    public int getIdStatPostMatch() {
        return idStatPostMatch;
    }

    public void setIdStatPostMatch(int idStatPostMatch) {
        this.idStatPostMatch = idStatPostMatch;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getButsMarques() {
        return butsMarques;
    }

    public void setButsMarques(int butsMarques) {
        this.butsMarques = butsMarques;
    }

    public int getPassesDecisives() {
        return passesDecisives;
    }

    public void setPassesDecisives(int passesDecisives) {
        this.passesDecisives = passesDecisives;
    }

    public float getDistanceParcourue() {
        return distanceParcourue;
    }

    public void setDistanceParcourue(float distanceParcourue) {
        this.distanceParcourue = distanceParcourue;
    }

    public int getTirsCadres() {
        return tirsCadres;
    }

    public void setTirsCadres(int tirsCadres) {
        this.tirsCadres = tirsCadres;
    }

    public float getNoteMatch() {
        return noteMatch;
    }

    public void setNoteMatch(float noteMatch) {
        this.noteMatch = noteMatch;
    }
}