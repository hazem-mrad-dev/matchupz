package models.joueur;

import javafx.beans.property.*;

public class HistoriqueClub {
    private final IntegerProperty idHistorique = new SimpleIntegerProperty();
    private final IntegerProperty idJoueur = new SimpleIntegerProperty();
    private final IntegerProperty idClub = new SimpleIntegerProperty();
    private final ObjectProperty<java.sql.Date> saisonDebut = new SimpleObjectProperty<>();
    private final ObjectProperty<java.sql.Date> saisonFin = new SimpleObjectProperty<>();

    public HistoriqueClub() {}

    public HistoriqueClub(int idJoueur, int idClub, java.sql.Date saisonDebut, java.sql.Date saisonFin) {
        this.idJoueur.set(idJoueur);
        this.idClub.set(idClub);
        this.saisonDebut.set(saisonDebut);
        this.saisonFin.set(saisonFin);
    }

    public HistoriqueClub(int idHistorique, int idJoueur, int idClub, java.sql.Date saisonDebut, java.sql.Date saisonFin) {
        this.idHistorique.set(idHistorique);
        this.idJoueur.set(idJoueur);
        this.idClub.set(idClub);
        this.saisonDebut.set(saisonDebut);
        this.saisonFin.set(saisonFin);
    }

    public int getIdHistorique() { return idHistorique.get(); }
    public void setIdHistorique(int idHistorique) { this.idHistorique.set(idHistorique); }
    public IntegerProperty idHistoriqueProperty() { return idHistorique; }

    public int getIdJoueur() { return idJoueur.get(); }
    public void setIdJoueur(int idJoueur) { this.idJoueur.set(idJoueur); }
    public IntegerProperty idJoueurProperty() { return idJoueur; }

    public int getIdClub() { return idClub.get(); }
    public void setIdClub(int idClub) { this.idClub.set(idClub); }
    public IntegerProperty idClubProperty() { return idClub; }

    public java.sql.Date getSaisonDebut() { return saisonDebut.get(); }
    public void setSaisonDebut(java.sql.Date saisonDebut) { this.saisonDebut.set(saisonDebut); }
    public ObjectProperty<java.sql.Date> saisonDebutProperty() { return saisonDebut; }

    public java.sql.Date getSaisonFin() { return saisonFin.get(); }
    public void setSaisonFin(java.sql.Date saisonFin) { this.saisonFin.set(saisonFin); }
    public ObjectProperty<java.sql.Date> saisonFinProperty() { return saisonFin; }
}