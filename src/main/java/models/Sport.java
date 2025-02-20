package models;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sport {
    private int idSport;
    private final StringProperty nomSport = new SimpleStringProperty();  // Keep this as StringProperty
    private String description;
    private Sport sport;

    // Updated constructor to handle StringProperty for nomSport
    public Sport(int idSport, String nomSport, String description) {
        this.idSport = idSport;
        this.nomSport.set(nomSport);  // Use the set() method to set the value of the StringProperty
        this.description = description;
    }

    // Default constructor
    public Sport() {}

    // Constructor with String and description
    public Sport(String nomSport, String description) {
        this.nomSport.set(nomSport);  // Use the set() method to set the value of the StringProperty
        this.description = description;
    }

    // Getter and Setter for idSport
    public int getIdSport() {
        return idSport;
    }

    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    // Getter for nomSport as StringProperty (so it can be used for binding)
    public StringProperty nomSportProperty() {
        return nomSport;
    }

    // Getter for nomSport as String
    public String getNomSport() {
        return nomSport.get();
    }

    // Setter for nomSport
    public void setNomSport(String nomSport) {
        this.nomSport.set(nomSport);  // Use the set() method of StringProperty
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for Sport object (if needed for associations)
    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
