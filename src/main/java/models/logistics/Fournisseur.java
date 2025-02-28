package models.logistics;

public class Fournisseur {
    private int id_fournisseur;
    private String nom;
    private String email;
    private String adresse;
    private String categorie_produit;

    public Fournisseur() {}

    public Fournisseur(int id_fournisseur, String nom, String email, String adresse, String categorie_produit) {
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.categorie_produit = categorie_produit;
    }

    public Fournisseur(String nom, String email, String adresse, String categorie_produit) {
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.categorie_produit = categorie_produit;
    }

    public int getId_fournisseur() {
        return id_fournisseur;
    }

    public void setId_fournisseur(int id_fournisseur) {
        this.id_fournisseur = id_fournisseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCategorie_produit() {
        return categorie_produit;
    }

    public void setCategorie_produit(String categorie_produit) {
        this.categorie_produit = categorie_produit;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id_fournisseur=" + id_fournisseur +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", categorie_produit='" + categorie_produit + '\'' +
                '}';
    }
}
