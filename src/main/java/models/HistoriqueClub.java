package models;

import java.util.Date;

public class HistoriqueClub {
    private int idHistorique;
    private int idJoueur;
    private String nomClub;
    private Date saisonDebut;
    private Date saisonFin;

    public HistoriqueClub(int idHistorique, int idJoueur, String nomClub, Date saisonDebut, Date saisonFin) {
        this.idHistorique = idHistorique;
        this.idJoueur = idJoueur;
        this.nomClub = nomClub;
        this.saisonDebut = saisonDebut;
        this.saisonFin = saisonFin;
    }

    public HistoriqueClub() {

    }

    public int getIdHistorique() {
        return idHistorique;
    }

    public void setIdHistorique(int idHistorique) {
        this.idHistorique = idHistorique;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getNomClub() {
        return nomClub;
    }

    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    public Date getSaisonDebut() {
        return saisonDebut;
    }

    public void setSaisonDebut(Date saisonDebut) {
        this.saisonDebut = saisonDebut;
    }

    public Date getSaisonFin() {
        return saisonFin;
    }

    public void setSaisonFin(Date saisonFin) {
        this.saisonFin = saisonFin;
    }
}