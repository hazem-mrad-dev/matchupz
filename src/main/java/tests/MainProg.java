package tests;

import models.*;
import services.*;

import java.util.Date;

public class MainProg {
    public static void main(String[] args) throws Exception {
        /*
        JoueurService joueurService = new JoueurService();
        Joueur joueur1 = new Joueur("BRUH", "Mrad",new Date(90,0,1), "CF", 180, 75, "Actif", "hazem@example.com", "123456789");
        joueurService.ajouter(joueur1);
        joueurService.modifier(joueur1);
        joueurService.supprimer(new Joueur(14, "", "",new Date(90,0,1), "", 180, 75, "Actif", "hazem@example.com", "123456789"));
        joueurService.rechercher().forEach(System.out::println);
        //===========================================================/
        EvaluationPhysiqueService service = new EvaluationPhysiqueService();
        EvaluationPhysique evaluation = new EvaluationPhysique(4, 11, new Date(25,6,3), 7.5f, 8.0f, 9.2f, "No injuries");
        service.ajouter(evaluation);
        evaluation.setNiveauEndurance(8.5f);
        service.modifier(evaluation);
        service.rechercher().forEach(e -> System.out.println(e.getIdEvaluation() + " - " + e.getNiveauEndurance()));
        service.supprimer(new EvaluationPhysique(3, 11, new Date(25,6,3), 7.5f, 8.0f, 9.2f, "No injuries"));
        //===========================================================/

        HistoriqueClubService historiqueService = new HistoriqueClubService();
        HistoriqueClub historique = new HistoriqueClub(1, 10, "Club B", new Date(), new Date());
        //historiqueService.ajouter(historique);
        historiqueService.modifier(new HistoriqueClub(3, 10, "Club B", new Date(), new Date()));
        historiqueService.rechercher().forEach(h -> System.out.println(h.getNomClub()));
        //historiqueService.supprimer(historique);
        //===========================================================/
        PerformanceJoueurService service = new PerformanceJoueurService();
        PerformanceJoueur performance = new PerformanceJoueur(11, "2022-2023", 30, 2700, 10, 5, 3, 1);
        //service.ajouter(performance);
        //performance.setButsMarques(12);
        //service.modifier(new PerformanceJoueur(6,11, "2045-2046", 30, 2700, 10, 5, 3, 1));
        service.supprimer(new PerformanceJoueur(6,11, "2045-2046", 30, 2700, 10, 5, 3, 1));
        //===========================================================/

<
         */
        StatistiquesPostMatchService service = new StatistiquesPostMatchService();
        StatistiquesPostMatch stat = new StatistiquesPostMatch(0, 11, 1, 2, 1, 8.5f, 3, 7.5f);
        //service.ajouter(stat);
        service.modifier(stat);
        service.supprimer(new StatistiquesPostMatch(5, 11, 1, 10, 10, 8.5f, 3, 7.5f));


    }
}
