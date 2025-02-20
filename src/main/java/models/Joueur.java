package models;

import javafx.beans.property.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Joueur {
    private IntegerProperty idJoueur = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty prenom = new SimpleStringProperty();
    private ObjectProperty<Date> dateNaissance = new SimpleObjectProperty<>();
    private StringProperty poste = new SimpleStringProperty();
    private FloatProperty taille = new SimpleFloatProperty();
    private FloatProperty poids = new SimpleFloatProperty();
    private StringProperty statut = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty telephone = new SimpleStringProperty();
    private IntegerProperty idSport = new SimpleIntegerProperty(); // Foreign key to the Sport table
    private Sport sport;

    // Constructor with properties initialized
    public Joueur(String nom, String prenom, java.sql.Date dateNaissance, String poste, float taille, float poids, String statut, String email, String telephone, int idSport) {
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.dateNaissance.set(dateNaissance);
        this.poste.set(poste);
        this.taille.set(taille);
        this.poids.set(poids);
        this.statut.set(statut);
        this.email.set(email);
        this.telephone.set(telephone);
        this.idSport.set(idSport);
    }

    public Joueur(int idJoueur, int idSport, String nom, String prenom, java.sql.Date dateNaissance, String poste, float taille, float poids, String statut, String email, String telephone) {
        this.idJoueur.set(idJoueur);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.dateNaissance.set(dateNaissance);
        this.poste.set(poste);
        this.taille.set(taille);
        this.poids.set(poids);
        this.statut.set(statut);
        this.email.set(email);
        this.telephone.set(telephone);
        this.idSport.set(idSport);
    }

    public Joueur(int idJoueur, Sport sport, String nom, String prenom, java.sql.Date dateNaissance, String poste, float taille, float poids, String statut, String email, String telephone) {
        this.idJoueur.set(idJoueur);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.dateNaissance.set(dateNaissance);
        this.poste.set(poste);
        this.taille.set(taille);
        this.poids.set(poids);
        this.statut.set(statut);
        this.email.set(email);
        this.telephone.set(telephone);
        this.idSport.set(idSport.get()); // This will set the value correctly from one IntegerProperty to another
    }

    // Property methods for the fields
    public IntegerProperty idJoueurProperty() {
        return idJoueur;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public ObjectProperty<Date> dateNaissanceProperty() {
        return dateNaissance;
    }

    public StringProperty posteProperty() {
        return poste;
    }

    public FloatProperty tailleProperty() {
        return taille;
    }

    public FloatProperty poidsProperty() {
        return poids;
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public IntegerProperty idSportProperty() {
        return idSport;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    // Getter and Setter for other fields
    public int getIdJoueur() {
        return idJoueur.get();
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur.set(idJoueur);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public Date getDateNaissance() {
        return dateNaissance.get();
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance.set(dateNaissance);
    }

    public String getPoste() {
        return poste.get();
    }

    public void setPoste(String poste) {
        this.poste.set(poste);
    }

    public float getTaille() {
        return taille.get();
    }

    public void setTaille(float taille) {
        this.taille.set(taille);
    }

    public float getPoids() {
        return poids.get();
    }

    public void setPoids(float poids) {
        this.poids.set(poids);
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public int getIdSport() {
        return idSport.get();
    }

    public void setIdSport(int idSport) {
        this.idSport.set(idSport);
    }
}
