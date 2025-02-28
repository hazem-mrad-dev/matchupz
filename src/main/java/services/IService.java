package services;

import models.utilisateur.User;

import java.util.List;

public interface IService <T>{
    void ajouter(T t);
    void supprimer(T t);
    void modifier(T t);
    List<T> recherche();
   // public User verifierUtilisateur(String email, String password);



}
