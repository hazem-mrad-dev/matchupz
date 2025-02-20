package models;

public class Materiel {
    private int id_materiel;
    private int id_fournisseur;
    private String nom;
    private TypeMateriel type;
    private int quantite;
    private EtatMateriel etat;
    private float prix_unitaire;

    public Materiel() {}

    public Materiel(int id_materiel, int id_fournisseur, String nom, TypeMateriel type, int quantite, EtatMateriel etat, float prix_unitaire) {
        this.id_materiel = id_materiel;
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.type = type;
        this.quantite = quantite;
        this.etat = etat;
        this.prix_unitaire = prix_unitaire;
    }

    public Materiel(int id_fournisseur, String nom, TypeMateriel type, int quantite, EtatMateriel etat, float prix_unitaire) {
        this.id_fournisseur = id_fournisseur;
        this.nom = nom;
        this.type = type;
        this.quantite = quantite;
        this.etat = etat;
        this.prix_unitaire = prix_unitaire;
    }

    public int getId_materiel() {
        return id_materiel;
    }

    public void setId_materiel(int id_materiel) {
        this.id_materiel = id_materiel;
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

    public TypeMateriel getType() {
        return type;
    }

    public void setType(TypeMateriel type) {
        this.type = type;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public EtatMateriel getEtat() {
        return etat;
    }

    public void setEtat(EtatMateriel etat) {
        this.etat = etat;
    }

    public float getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(float prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    @Override
    public String toString() {
        return "Materiel{" +
                "id_materiel=" + id_materiel +
                ", id_fournisseur=" + id_fournisseur +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", quantite=" + quantite +
                ", etat=" + etat +
                ", prix_unitaire=" + prix_unitaire +
                '}';
    }
}
