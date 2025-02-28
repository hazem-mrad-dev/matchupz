package services.EspaceSportif;

import java.util.List;

public interface EspaceService<T> {

    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    List<T> rechercher();
}
