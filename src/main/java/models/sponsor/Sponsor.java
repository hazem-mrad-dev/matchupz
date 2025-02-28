package models;

public class Sponsor {
    private int id_sponsor;
    private String nom;
    private String Contact;
    private String Pack;

    public Sponsor() {}
    public Sponsor(int id_sponsor, String nom, String Contact, String Pack) {
        this.id_sponsor = id_sponsor;
        this.nom = nom;
        this.Contact = Contact;
        this.Pack = Pack;
    }

    public Sponsor(String nom, String Contact, String Pack) {
        this.nom = nom;
        this.Contact = Contact;
        this.Pack = Pack;
    }

    public int getId_sponsor() {
        return id_sponsor;
    }

    public String getContact() {
        return Contact;
    }

    public String getNom() {
        return nom;
    }

    public String getPack() {
        return Pack;
    }

    public void setId_sponsor(int id_sponsor) {
        this.id_sponsor = id_sponsor;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setPack(String pack) {
        Pack = pack;
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                " nom='" + nom + '\'' +
                ", Contact='" + Contact + '\'' +
                ", Pack='" + Pack + '\'' +
                '}';
    }
}
