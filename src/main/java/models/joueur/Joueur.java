package models.joueur;

import javafx.beans.property.*;

import java.util.Date;

public class Joueur {
    private final IntegerProperty idJoueur = new SimpleIntegerProperty();
    private final IntegerProperty idSport = new SimpleIntegerProperty(); // Keep id_sport
    private final StringProperty nomSport = new SimpleStringProperty(); // Add nomSport
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final ObjectProperty<Date> dateNaissance = new SimpleObjectProperty<>();
    private final StringProperty poste = new SimpleStringProperty();
    private final FloatProperty taille = new SimpleFloatProperty();
    private final FloatProperty poids = new SimpleFloatProperty();
    private final StringProperty statut = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty profilePictureUrl = new SimpleStringProperty();

    // Default constructor
    public Joueur(String nom, String prenom, java.sql.Date dateNaissance, String poste, float taille, float poids, String statut, String email, String telephone, int idSport) {}

    // Constructor with all fields except idJoueur (auto-generated)
    public Joueur(int idSport, String nomSport, String nom, String prenom, Date dateNaissance, String poste, float taille,
                  float poids, String statut, String email, String telephone, String profilePictureUrl) {
        this.idSport.set(idSport);
        this.nomSport.set(nomSport);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.dateNaissance.set(dateNaissance);
        this.poste.set(poste);
        this.taille.set(taille);
        this.poids.set(poids);
        this.statut.set(statut);
        this.email.set(email);
        this.telephone.set(telephone);
        this.profilePictureUrl.set(profilePictureUrl);
    }

    public Joueur() {

    }

    // Getters and Setters
    public int getIdJoueur() { return idJoueur.get(); }
    public void setIdJoueur(int idJoueur) { this.idJoueur.set(idJoueur); }
    public IntegerProperty idJoueurProperty() { return idJoueur; }

    public int getIdSport() { return idSport.get(); }
    public void setIdSport(int idSport) { this.idSport.set(idSport); }
    public IntegerProperty idSportProperty() { return idSport; }

    public String getNomSport() { return nomSport.get(); }
    public void setNomSport(String nomSport) { this.nomSport.set(nomSport); }
    public StringProperty nomSportProperty() { return nomSport; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public StringProperty prenomProperty() { return prenom; }

    public Date getDateNaissance() { return dateNaissance.get(); }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance.set(dateNaissance); }
    public ObjectProperty<Date> dateNaissanceProperty() { return dateNaissance; }

    public String getPoste() { return poste.get(); }

    public void setPoste(String poste) { this.poste.set(poste); }
    public StringProperty posteProperty() { return poste; }

    public float getTaille() { return taille.get(); }
    public void setTaille(float taille) { this.taille.set(taille); }
    public FloatProperty tailleProperty() { return taille; }

    public float getPoids() { return poids.get(); }
    public void setPoids(float poids) { this.poids.set(poids); }
    public FloatProperty poidsProperty() { return poids; }

    public String getStatut() { return statut.get(); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public StringProperty statutProperty() { return statut; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String telephone) { this.telephone.set(telephone); }
    public StringProperty telephoneProperty() { return telephone; }

    public String getProfilePictureUrl() { return profilePictureUrl.get(); }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl.set(profilePictureUrl); }
    public StringProperty profilePictureUrlProperty() { return profilePictureUrl; }
}