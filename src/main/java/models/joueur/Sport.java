package models.joueur;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Sport {
    private final IntegerProperty idSport;
    private final StringProperty nomSport;
    private final StringProperty description;

    // Constructor with no arguments
    public Sport() {
        this.idSport = new SimpleIntegerProperty();
        this.nomSport = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    // Constructor with parameters for nomSport and description
    public Sport(String nomSport, String description) {
        this.idSport = new SimpleIntegerProperty();
        this.nomSport = new SimpleStringProperty(nomSport);
        this.description = new SimpleStringProperty(description);
    }

    // Getters and setters for properties
    public int getIdSport() {
        return idSport.get();
    }

    public void setIdSport(int idSport) {
        this.idSport.set(idSport);
    }

    public IntegerProperty idSportProperty() {
        return idSport;
    }

    public String getNomSport() {
        return nomSport.get();
    }

    public void setNomSport(String nomSport) {
        this.nomSport.set(nomSport);
    }

    public StringProperty nomSportProperty() {
        return nomSport;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
