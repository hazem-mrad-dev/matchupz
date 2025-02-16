package models;

public class Sport {
    private int idSport;
    private String nom;
    private String description;
    private String type;

    // Constructors
    public Sport() {
    }

    public Sport(int idSport, String nom, String description, String type) {
        this.idSport = idSport;
        this.nom = nom;
        this.description = description;
        this.type = type;
    }

    public Sport(String nom, String description, String type) {
        this.nom = nom;
        this.description = description;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // toString
    @Override
    public String toString() {
        return "Sport{" +
                "idSport=" + idSport +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
