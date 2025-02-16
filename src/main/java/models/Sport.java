package models;

public class Sport {
    private int idSport;
    private String nom;
    private String description;

    // Constructors
    public Sport() {
    }

    public Sport(int idSport, String nom, String description) {
        this.idSport = idSport;
        this.nom = nom;
        this.description = description;
    }

    public Sport(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getters and Setters
    public int getIdSport() {
        return idSport;
    }

    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString
    @Override
    public String toString() {
        return "Sport{" +
                "idSport=" + idSport +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}