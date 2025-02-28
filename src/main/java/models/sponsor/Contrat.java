package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Contrat {
    private int id_contrat;
    private int id_sponsor;
    private String titre;
    private String DateDebut;
    private String DateFin;
    private float montant;

    public Contrat() {

    }

    public Contrat(int idSponsor, String titre, String DateDebut, String DateFin, float montant) {
        this.id_sponsor = idSponsor;
        this.titre = titre;
        this.DateDebut = DateDebut;
        this.DateFin = DateFin;
        this.montant = montant;
    }

    public Contrat(int id_contrat, int idSponsor, String titre, String DateDebut, String DateFin, float montant) {
        this.id_contrat = id_contrat;
        this.id_sponsor = idSponsor;
        this.titre = titre;
        this.DateDebut = DateDebut;
        this.DateFin = DateFin;
        this.montant = montant;
    }

    public int getId_contrat() {
        return id_contrat;
    }

    public String getTitre() {
        return titre;
    }

    public String getDateDebut() {
        return DateDebut;
    }

    public String getDateFin() {
        return DateFin;
    }

    public float getMontant() {
        return montant;
    }

    public void setId_contrat(int id_contrat) {
        this.id_contrat = id_contrat;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDateDebut(String dateDebut) {
        DateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        DateFin = dateFin;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Contrat{" +
                " titre='" + titre + '\'' +
                ", DateDebut='" + DateDebut + '\'' +
                ", DateFin='" + DateFin + '\'' +
                ", montant=" + montant +
                '}';
    }

    public int getId_sponsor() {
        return id_sponsor;
    }

    public void setId_sponsor(int id_sponsor) {
        this.id_sponsor = id_sponsor;
    }


    public static String convertToDateFormat(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Input format (yyyy-MM-dd)
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd"); // Output format (yyyy/MM/dd)

        try {
            // Parse the input date
            java.util.Date date = inputFormat.parse(dateStr);
            // Convert to the new format
            return outputFormat.format(date);
        } catch (ParseException e) {
            // If the date is invalid, return null or handle the error
            System.out.println("Invalid date format: " + dateStr);
            return null;
        }
    }
}
