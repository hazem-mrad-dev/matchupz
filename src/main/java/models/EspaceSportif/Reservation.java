package models.EspaceSportif;

import java.sql.Timestamp;
import models.EspaceSportif.EspaceSportif;

public class Reservation {
    private int idReservation;
    private int idLieu;
    private Timestamp dateReservee;
    private String motif;

    private String status;

    private EspaceSportif espace;

    public EspaceSportif getEspaceSportif() {
        return espace;
    }

    public void setEspaceSportif (EspaceSportif espace) {
        this.espace = espace;
    }


    private String nomLieu;

    public String getNomLieu() {
        return nomLieu;
    }

    public void setNomLieu(String nomLieu) {
        this.nomLieu = nomLieu;
    }


    public Reservation() {}

    public Reservation(int idLieu, Timestamp dateReservee, String motif, String status) {
        this.idLieu = idLieu;
        this.dateReservee = dateReservee;
        this.motif = motif;
        this.status = status;
    }

    public Reservation(int idReservation, int idLieu, Timestamp dateReservee, String motif, String status) {
        this.idReservation = idReservation;
        this.idLieu = idLieu;
        this.dateReservee = dateReservee;
        this.motif = motif;
        this.status = status;
    }

    // Getters et Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getIdLieu() { return idLieu; }
    public void setIdLieu(int idLieu) { this.idLieu = idLieu; }

    public Timestamp getDateReservee() { return dateReservee; }
    public void setDateReservee(Timestamp dateReservee) { this.dateReservee = dateReservee; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", idLieu=" + idLieu +
                ", dateReservee=" + dateReservee +
                ", motif='" + motif + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
