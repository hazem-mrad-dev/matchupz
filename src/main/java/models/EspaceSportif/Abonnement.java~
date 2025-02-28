package models.EspaceSportif;

import java.util.Date;

public class Abonnement {
    private int idAbonnement;
    private int idSport;
    private String nomSport;  // Jointure avec la table Sport
    private String typeAbonnement; // Mensuel, Trimestriel, Annuel
    private Date dateDebut;
    private Date dateFin;
    private double tarif;
    private String etat; // Actif, Expiré, Suspendu

    public Abonnement() {}

    // Constructeur avec nomSport (via jointure)
    public Abonnement(int idAbonnement, int idSport, String nomSport, String typeAbonnement, Date dateDebut, Date dateFin, double tarif, String etat) {
        this.idAbonnement = idAbonnement;
        this.idSport = idSport;
        this.nomSport = nomSport;
        this.typeAbonnement = typeAbonnement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tarif = tarif;
        this.etat = etat;
    }

    // Getters & Setters
    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public int getIdSport() { return idSport; }
    public void setIdSport(int idSport) { this.idSport = idSport; }

    public String getNomSport() { return nomSport; }
    public void setNomSport(String nomSport) { this.nomSport = nomSport; }

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
                ", idSport=" + idSport +
                ", nomSport='" + nomSport + '\'' +
                ", typeAbonnement='" + typeAbonnement + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", tarif=" + tarif +
                ", etat='" + etat + '\'' +
                '}';
    }
}
