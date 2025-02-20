package tests;

import models.Fournisseur;
import models.Materiel;
import models.TypeMateriel;
import models.EtatMateriel;
import services.FournisseurService;
import services.MaterielService;

import java.util.Scanner;

public class MainProg {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FournisseurService fournisseurService = new FournisseurService();
        MaterielService materielService = new MaterielService();

        // Saisie des informations du fournisseur
        System.out.println("📝 Ajout d'un nouveau fournisseur");
        System.out.print("Nom du fournisseur : ");
        String nomFournisseur = scanner.nextLine();

        System.out.print("Email du fournisseur : ");
        String emailFournisseur = scanner.nextLine();

        System.out.print("Adresse du fournisseur : ");
        String adresseFournisseur = scanner.nextLine();

        System.out.print("Catégorie du produit (ex: EQUIPEMENT_SPORTIF) : ");
        String categorieProduit = scanner.nextLine();

        // Création et ajout du fournisseur
        Fournisseur fournisseur = new Fournisseur(nomFournisseur, emailFournisseur, adresseFournisseur, categorieProduit);
        fournisseurService.ajouter(fournisseur);

        // Récupération du dernier ID inséré
        int idFournisseur = fournisseurService.getLastInsertedId();

        if (idFournisseur == -1) {
            System.out.println("❌ Erreur lors de l'ajout du fournisseur.");
            return;
        }

        System.out.println("✅ Fournisseur ajouté avec succès ! ID: " + idFournisseur);

        // Saisie des informations du matériel
        System.out.println("\n📝 Ajout d'un nouveau matériel");
        System.out.print("Nom du matériel : ");
        String nomMateriel = scanner.nextLine();

        System.out.print("Type du matériel (ex: EQUIPEMENT_SPORTIF) : ");
        TypeMateriel typeMateriel = TypeMateriel.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Quantité : ");
        int quantite = scanner.nextInt();

        System.out.print("État du matériel (NEUF, OCCASION, ENDOMMAGE) : ");
        EtatMateriel etatMateriel = EtatMateriel.valueOf(scanner.next().toUpperCase());

        System.out.print("Prix unitaire : ");
        String prixString = scanner.next();
        prixString = prixString.replace(",", ".");  // Remplace les virgules par des points
        float prixUnitaire = Float.parseFloat(prixString); // Convertit en float


        // Création et ajout du matériel
        Materiel materiel = new Materiel(idFournisseur, nomMateriel, typeMateriel, quantite, etatMateriel, prixUnitaire);
        materielService.ajouter(materiel);

        System.out.println("✅ Matériel ajouté avec succès !");

        // Afficher les fournisseurs et matériels ajoutés
        System.out.println("\n🔎 Liste des fournisseurs : " + fournisseurService.rechercher());
        System.out.println("\n🔎 Liste des matériels : " + materielService.rechercher());
// 🔹 Modification du matériel
        System.out.println("\n✏️ Modification du matériel");
        System.out.print("Entrez l'ID du matériel à modifier : ");
        int idModif = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nouveau nom du matériel : ");
        String nouveauNom = scanner.nextLine();

        System.out.print("Nouvelle quantité : ");
        int nouvelleQuantite = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nouveau prix unitaire : ");
        float nouveauPrix = scanner.nextFloat();
        scanner.nextLine(); 

        Materiel materielModifie = new Materiel(idModif, nouveauNom, typeMateriel, nouvelleQuantite, etatMateriel, nouveauPrix);
        materielService.modifier(materielModifie);

        System.out.println("✅ Matériel modifié avec succès !");


        // 🔹 Suppression du matériel
        System.out.println("\n🗑️ Suppression du matériel");
        System.out.print("Entrez l'ID du matériel à supprimer : ");
        int idSuppMat = scanner.nextInt();
        materielService.supprimer(new Materiel(idSuppMat, "", null, 0, null, 0));
        System.out.println("✅ Matériel supprimé !");

        // 🔹 Suppression du fournisseur
        System.out.println("\n🗑️ Suppression du fournisseur");
        System.out.print("Entrez l'ID du fournisseur à supprimer : ");
        int idSuppFourn = scanner.nextInt();
        fournisseurService.supprimer(new Fournisseur(idSuppFourn, "", "", "", ""));
        System.out.println("✅ Fournisseur supprimé !");

        // 🔹 Affichage final
        System.out.println("\n🔎 Liste des fournisseurs : " + fournisseurService.rechercher());
        System.out.println("🔎 Liste des matériels : " + materielService.rechercher());

        scanner.close();
    }
}
