package models;

public class EspaceSportif {
    private int idLieu;
    private String nomEspace;
    private String adresse;
    private String categorie;
    private float capacite;

    public EspaceSportif() {
    }

    public EspaceSportif(int idLieu, String nomEspace) {
        this.idLieu = idLieu;
        this.nomEspace = nomEspace;
    }

    public EspaceSportif(int idLieu, String nomEspace, String adresse, String categorie, float capacite) {
        this.idLieu = idLieu;
        this.nomEspace = nomEspace;
        this.adresse = adresse;
        this.categorie = categorie;
        this.capacite = capacite;
    }

    public EspaceSportif(String nomEspace, String adresse, String categorie, float capacite) {
        this.nomEspace = nomEspace;
        this.adresse = adresse;
        this.categorie = categorie;
        this.capacite = capacite;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public String getNomEspace() {
        return nomEspace;
    }

    public void setNomEspace(String nomEspace) {
        this.nomEspace = nomEspace;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public float getCapacite() {
        return capacite;
    }

    public void setCapacite(float capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return "EspaceSportif{" +
                "idLieu=" + idLieu +
                ", nomEspace='" + nomEspace + '\'' +
                ", adresse='" + adresse + '\'' +
                ", categorie='" + categorie + '\'' +
                ", capacite=" + capacite +
                '}';
    }
}
