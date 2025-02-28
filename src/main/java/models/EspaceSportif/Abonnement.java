package models.EspaceSportif;

import java.util.Date;

public class Abonnement {
    private int idAbonnement;
    private int idClub;
    private String nomClub;  // Jointure avec la table Club
    private String typeAbonnement; // Mensuel, Trimestriel, Annuel
    private Date dateDebut;
    private Date dateFin;
    private double tarif;
    private String etat; // Actif, Expiré, Suspendu

    public Abonnement() {}

    // Constructeur avec nomClub (via jointure)
    public Abonnement(int idAbonnement, int idClub, String nomClub, String typeAbonnement, Date dateDebut, Date dateFin, double tarif, String etat) {
        this.idAbonnement = idAbonnement;
        this.idClub = idClub;
        this.nomClub = nomClub;
        this.typeAbonnement = typeAbonnement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tarif = tarif;
        this.etat = etat;
    }

    // Getters & Setters
    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public int getIdClub() { return idClub; }
    public void setIdClub(int idClub) { this.idClub = idClub; }

    public String getNomClub() { return nomClub; }
    public void setNomClub(String nomClub) { this.nomClub = nomClub; }

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }

    public java.sql.Date getDateDebut() { return (java.sql.Date) dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public java.sql.Date getDateFin() { return (java.sql.Date) dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    @Override
    public String toString() {
        return "Abonnement{" +
                "idAbonnement=" + idAbonnement +
                ", idClub=" + idClub +
                ", nomClub='" + nomClub + '\'' +
                ", typeAbonnement='" + typeAbonnement + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", tarif=" + tarif +
                ", etat='" + etat + '\'' +
                '}';
    }
}