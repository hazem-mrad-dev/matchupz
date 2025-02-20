package models;
import java.util.Objects;

public class Match {
    int idMatch;
    String C1;
    String C2;
    String SportType;

    public Match(){

    }

    public Match(int idMatch, String C1, String C2, String SportType) {
        this.idMatch = idMatch;
        this.C1 = C1;
        this.C2 = C2;
        this.SportType = SportType;
    }

    public Match(String c2, String c1, String s) {
        C2 = c2;
        C1 = c1;
        SportType = s;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public String getC1() {
        return C1;
    }

    public String getC2() {
        return C2;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public void setC1(String c1) {
        C1 = c1;
    }

    public void setC2(String c2) {
        C2 = c2;
    }

    public String getSportType() {
        return SportType;
    }

    public void setSportType(String sportType) {
        SportType = sportType;
    }

    @Override
    public String toString() {
        return "Match "+ idMatch +"{" +
                "Coté 1 =" + C1 + "//" +
                "Coté 2 =" + C2  +
                " de " + SportType +"}";
    }
}
