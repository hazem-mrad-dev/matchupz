package models;

public class PerformanceJoueur {
    private int idPerformance;
    private int idJoueur; // Foreign key reference
    private String saison;
    private int nombreMatches;
    private int minutesJouees;
    private int butsMarques;
    private int passesDecisives;
    private int cartonsJaunes;
    private int cartonsRouges;

    public PerformanceJoueur(int idPerformance, int idJoueur, String saison, int nombreMatches, int minutesJouees,
                             int butsMarques, int passesDecisives, int cartonsJaunes, int cartonsRouges) {
        this.idPerformance = idPerformance;
        this.idJoueur = idJoueur;
        this.saison = saison;
        this.nombreMatches = nombreMatches;
        this.minutesJouees = minutesJouees;
        this.butsMarques = butsMarques;
        this.passesDecisives = passesDecisives;
        this.cartonsJaunes = cartonsJaunes;
        this.cartonsRouges = cartonsRouges;
    }

    public PerformanceJoueur(int idJoueur, String saison, int nombreMatches, int minutesJouees, int butsMarques, int passesDecisives, int cartonsJaunes, int cartonsRouges) {
        this.idJoueur = idJoueur;
        this.saison = saison;
        this.nombreMatches = nombreMatches;
        this.minutesJouees = minutesJouees;
        this.butsMarques = butsMarques;
        this.passesDecisives = passesDecisives;
        this.cartonsJaunes = cartonsJaunes;
        this.cartonsRouges = cartonsRouges;
    }

    public PerformanceJoueur() {

    }

    @Override
    public String toString() {
        return "PerformanceJoueur{" +
                "idPerformance=" + idPerformance +
                ", idJoueur=" + idJoueur +
                ", saison='" + saison + '\'' +
                ", nombreMatches=" + nombreMatches +
                ", minutesJouees=" + minutesJouees +
                ", butsMarques=" + butsMarques +
                ", passesDecisives=" + passesDecisives +
                ", cartonsJaunes=" + cartonsJaunes +
                ", cartonsRouges=" + cartonsRouges +
                '}';
    }

    public int getIdPerformance()    {
        return idPerformance;
    }

    public void setIdPerformance(int idPerformance) {
        this.idPerformance = idPerformance;
    }

    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getSaison() {
        return saison;
    }

    public void setSaison(String saison) {
        this.saison = saison;
    }

    public int getNombreMatches() {
        return nombreMatches;
    }

    public void setNombreMatches(int nombreMatches) {
        this.nombreMatches = nombreMatches;
    }

    public int getMinutesJouees() {
        return minutesJouees;
    }

    public void setMinutesJouees(int minutesJouees) {
        this.minutesJouees = minutesJouees;
    }

    public int getButsMarques() {
        return butsMarques;
    }

    public void setButsMarques(int butsMarques) {
        this.butsMarques = butsMarques;
    }

    public int getPassesDecisives() {
        return passesDecisives;
    }

    public void setPassesDecisives(int passesDecisives) {
        this.passesDecisives = passesDecisives;
    }

    public int getCartonsJaunes() {
        return cartonsJaunes;
    }

    public void setCartonsJaunes(int cartonsJaunes) {
        this.cartonsJaunes = cartonsJaunes;
    }

    public int getCartonsRouges() {
        return cartonsRouges;
    }

    public void setCartonsRouges(int cartonsRouges) {
        this.cartonsRouges = cartonsRouges;
    }
}