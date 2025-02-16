package models;

import java.util.Date;

public class Joueur {
    private int idJoueur;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String poste;
    private float taille; // in cm
    private float poids;  // in kg
    private String statut;
    private String email;
    private String telephone;

    public Joueur(int idJoueur, String nom, String prenom, Date dateNaissance, String poste,
                  float taille, float poids, String statut, String email, String telephone) {
        this.idJoueur = idJoueur;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.poste = poste;
        this.taille = taille;
        this.poids = poids;
        this.statut = statut;
        this.email = email;
        this.telephone = telephone;
    }

    public Joueur(String nom, String prenom, Date dateNaissance, String poste,
                  float taille, float poids, String statut, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.poste = poste;
        this.taille = taille;
        this.poids = poids;
        this.statut = statut;
        this.email = email;
        this.telephone = telephone;
    }

    public Joueur() {

    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public float getTaille() {
        return taille;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // toString method
    @Override
    public String toString() {
        return "Joueur{" +
                "idJoueur=" + idJoueur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", poste='" + poste + '\'' +
                ", taille=" + taille +
                ", poids=" + poids +
                ", statut='" + statut + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}