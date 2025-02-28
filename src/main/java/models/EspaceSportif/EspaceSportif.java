package models.EspaceSportif;

public class EspaceSportif {
    private int idLieu;         // Identifiant unique de l'espace sportif
    private String nomEspace;   // Nom de l'espace sportif
    private String adresse;     // Adresse de l'espace sportif
    private String categorie;   // Catégorie (ex. Stade, Gymnase, etc.)
    private float capacite;     // Capacité maximale de l'espace

    // Constructeur par défaut
    public EspaceSportif() {
    }

    // Constructeur avec tous les attributs, incluant l'ID
    public EspaceSportif(int idLieu, String nomEspace, String adresse, String categorie, float capacite) {
        this.idLieu = idLieu;
        this.nomEspace = nomEspace;
        this.adresse = adresse;
        this.categorie = categorie;
        this.capacite = capacite;
    }

    // Constructeur sans ID (utile pour l'ajout avant attribution d'un ID par la base)
    public EspaceSportif(String nomEspace, String adresse, String categorie, float capacite) {
        this.nomEspace = nomEspace;
        this.adresse = adresse;
        this.categorie = categorie;
        this.capacite = capacite;
    }

    public EspaceSportif(int id, String nom) {
        this.idLieu = id;
        this.nomEspace = nom;
    }

    // Getters et Setters
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
        if (nomEspace == null || nomEspace.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'espace ne peut pas être vide");
        }
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

    // Méthode toString pour une représentation textuelle claire
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

    // Ajout d'une méthode equals pour comparer deux objets (optionnel mais utile)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspaceSportif that = (EspaceSportif) o;
        return idLieu == that.idLieu &&
                Float.compare(that.capacite, capacite) == 0 &&
                nomEspace != null && nomEspace.equals(that.nomEspace) &&
                adresse != null && adresse.equals(that.adresse) &&
                categorie != null && categorie.equals(that.categorie);
    }

    // Ajout d'une méthode hashCode cohérente avec equals (optionnel)
    @Override
    public int hashCode() {
        int result = idLieu;
        result = 31 * result + (nomEspace != null ? nomEspace.hashCode() : 0);
        result = 31 * result + (adresse != null ? adresse.hashCode() : 0);
        result = 31 * result + (categorie != null ? categorie.hashCode() : 0);
        result = 31 * result + Float.floatToIntBits(capacite);
        return result;
    }
}
