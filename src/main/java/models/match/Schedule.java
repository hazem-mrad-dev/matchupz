package models.match;
import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
        int idSchedule;
        LocalDate dateMatch;
        LocalTime startTime;
        LocalTime endTime;
        int idMatchFK;
        int idLieu;

        public Schedule(){

        }

    public Schedule(int idSchedule, LocalDate dateMatch, LocalTime startTime, LocalTime endTime, int idMatchFK, int idLieu) {
        this.idSchedule = idSchedule;
        this.dateMatch = dateMatch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.idMatchFK = idMatchFK;
        this.idLieu = idLieu;
    }

    public Schedule(LocalDate dateMatch, LocalTime startTime, LocalTime endTime, int idMatchFK, int idLieu) {
        this.dateMatch = dateMatch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.idMatchFK = idMatchFK;
        this.idLieu = idLieu;
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public LocalDate getDateMatch() {
        return dateMatch;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIdMatchFK() {
        return idMatchFK;
    }

    public int getIdLieu() {return idLieu; }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public void setDateMatch(LocalDate dateMatch) {
        this.dateMatch = dateMatch;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setIdMatchFK(int idMatchFK) {
        this.idMatchFK = idMatchFK;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "idSchedule=" + idSchedule +
                ", dateMatch=" + dateMatch +
                ", tDepart=" + startTime +
                ", tFin=" + endTime +
                ", idMatch=" + idMatchFK +
                '}';
    }

}
