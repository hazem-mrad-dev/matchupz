package tests;

import models.Match;
import models.Personne;
import models.Schedule;
import services.MatchService;
import services.PersonneService;
import services.ScheduleService;
import utils.MyDatabase;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainProg {


    public static void main(String[] args) {


        //PersonneService ps = new PersonneService();
        //MatchService ms = new MatchService();
        ScheduleService ss = new ScheduleService();


        // ps.ajouter(new Personne("Louay","Tlili",21));
       // ps.modifier(new Personne(1,"Kawther","Ben Salem",30));
       // ps.supprimer(new Personne(1,"","",0));
       // ps.rechercher();

        //==========================================

        //ms.ajouter(new Match("ATM","BYN","Football"));
        //ms.modifier(new Match(4,"EST","CA","Handball"));
        //ms.supprimer(new Match(1,"","",""));
        //ms.rechercher();

        //==========================================
        LocalDate dateMatch = LocalDate.parse("2025-03-12");
        LocalTime startTime = LocalTime.parse("18:00");
        LocalTime endTime = LocalTime.parse("19:00");
        //ss.ajouter(new Schedule( dateMatch, startTime, endTime,6,1));
        //ss.modifier(new Schedule(2,dateMatch,startTime,endTime,4));
        //ss.supprimer(new Schedule(2,dateMatch,startTime,endTime,0));
        //ss.rechercher();
        ss.getAllEspacesSportifs();



    }
}
