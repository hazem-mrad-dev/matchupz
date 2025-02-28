package models.utilisateur;


import models.utilisateur.Role;

import java.time.LocalDate;

public class User {

    private int id_user ;
    private String nom ;
    private String prenom ;
    private String email ;
    private String password ;
    private int num_telephone ;
    private LocalDate date_de_naissance ;
    private String genre ;
    private models.utilisateur.Role role ;
    private String image ;
    private int reset_code ;

    public void setReset_code(int reset_code) {
        this.reset_code = reset_code;
    }

    public int getReset_code() {
        return reset_code;
    }

    public User() {
    }

    public User(int id_user) {
        this.id_user = id_user;
    }

    public User(String nom, String prenom, String email, String password, int num_telephone, LocalDate date_de_naissance, String genre, models.utilisateur.Role role, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.num_telephone = num_telephone;
        this.date_de_naissance = date_de_naissance;
        this.genre = genre;
        this.role = role;
        this.image = image;
    }

    public User(int id_user, String nom, String prenom, String email, String password, int num_telephone, LocalDate date_de_naissance, String genre, models.utilisateur.Role role, String image) {
        this.id_user = id_user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.num_telephone = num_telephone;
        this.date_de_naissance = date_de_naissance;
        this.genre = genre;
        this.role = role;
        this.image = image;
    }



    public int getId_user() {
        return id_user;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getNum_telephone() {
        return num_telephone;
    }

    public LocalDate getDate_de_naissance() {
        return date_de_naissance;
    }

    public String getGenre() {
        return genre;
    }

    public models.utilisateur.Role getRole() {
        return role;
    }

    public String getImage() {
        return image;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNum_telephone(int num_telephone) {
        this.num_telephone = num_telephone;
    }

    public void setDate_de_naissance(LocalDate date_de_naissance) {
        this.date_de_naissance = date_de_naissance;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", num_telephone=" + num_telephone +
                ", date_de_naissance=" + date_de_naissance +
                ", genre='" + genre + '\'' +
                ", role='" + role + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
