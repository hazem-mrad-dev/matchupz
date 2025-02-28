package models.joueur;

import javafx.beans.property.*;

public class Club {
    private final IntegerProperty idClub = new SimpleIntegerProperty();
    private final StringProperty nomClub = new SimpleStringProperty();
    private final StringProperty photoUrl = new SimpleStringProperty();

    // Default constructor
    public Club() {}

    // Constructor with all fields except idClub (auto-generated)
    public Club(String nomClub, String photoUrl) {
        this.nomClub.set(nomClub);
        this.photoUrl.set(photoUrl);
    }

    // Full constructor including idClub
    public Club(int idClub, String nomClub, String photoUrl) {
        this.idClub.set(idClub);
        this.nomClub.set(nomClub);
        this.photoUrl.set(photoUrl);
    }

    // Getters and Setters
    public int getIdClub() { return idClub.get(); }
    public void setIdClub(int idClub) { this.idClub.set(idClub); }
    public IntegerProperty idClubProperty() { return idClub; }

    public String getNomClub() { return nomClub.get(); }
    public void setNomClub(String nomClub) { this.nomClub.set(nomClub); }
    public StringProperty nomClubProperty() { return nomClub; }

    public String getPhotoUrl() { return photoUrl.get(); }
    public void setPhotoUrl(String photoUrl) { this.photoUrl.set(photoUrl); }
    public StringProperty photoUrlProperty() { return photoUrl; }
}