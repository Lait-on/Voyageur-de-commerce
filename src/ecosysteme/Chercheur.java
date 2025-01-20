package ecosysteme;

import SQL.Ville;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe représentant un Chercheur, qui est un titulaire supervisant un ensemble d'étudiants.
 */
public class Chercheur extends Titulaire {
    private static final long serialVersionUID = 1L;

    /** L'ensemble des étudiants encadrés par ce chercheur*/
    private Set<Etudiant> etudiants;

    /**
     * Constructeur par défaut de la classe Chercheur.
     * Initialise l'ensemble des étudiants à un ensemble vide.
     */
    public Chercheur() {
        this.etudiants = Set.of(); // Création d'un ensemble vide d'étudiants
    }

    /**
     * Constructeur paramétré de la classe Chercheur.
     * 
     * @param nom Le nom du chercheur.
     * @param prenom Le prénom du chercheur.
     * @param age L'âge du chercheur.
     * @param ville La ville du chercheur.
     * @param disciplines Les disciplines que le chercheur enseigne.
     * @param numBureau Le numéro de bureau du chercheur.
     * @param etudiants L'ensemble des étudiants encadrés par le chercheur.
     */
    public Chercheur(String nom, String prenom, int age, Ville ville, 
                     Set<Discipline> disciplines, int numBureau, Set<Etudiant> etudiants) {
        super(nom, prenom, ville, age, disciplines, numBureau);
        this.etudiants = etudiants;
        // Associer chaque étudiant au chercheur
        if (etudiants != null && !etudiants.isEmpty()) {
            for (Etudiant e : etudiants) {
                e.change_titulaire(this);  // Le chercheur devient titulaire des étudiants
            }
        }
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du Chercheur,
     * y compris les informations sur les étudiants qu'il encadre.
     * 
     * @return La chaîne représentant le Chercheur et ses étudiants (si présents).
     */
    @Override
    public String toString() {
        // Si aucun étudiant n'est encadré, on retourne uniquement les informations du chercheur
        if (etudiants == null || etudiants.isEmpty()) {
            return super.toString();
        } else {
            String res = super.toString().substring(0, super.toString().length() - 1) + ", [etudiants=";
            for (Etudiant e : etudiants) {
                res += "_" + e.getPrenom() + " " + e.getNom() + "_";
            }
            return res + "]";
        }
    }

    /**
     * Retourne une chaîne de caractères représentant tous les étudiants encadrés par ce chercheur.
     * 
     * @return La chaîne contenant les prénoms et noms des étudiants, séparés par des virgules.
     */
    public String getEtudiantsString() {
        StringBuilder nomsComplet = new StringBuilder();
        
        // Si aucun étudiant n'est encadré, retourner une chaîne vide
        if (etudiants == null || etudiants.isEmpty()) {
            return "";
        }
        
        // Ajouter le nom et prénom de chaque étudiant
        for (Etudiant etudiant : etudiants) {
            if (nomsComplet.length() > 0) {
                nomsComplet.append(", ");  // Ajouter une virgule pour séparer les noms
            }
            nomsComplet.append(etudiant.getPrenom()).append(" ").append(etudiant.getNom());
        }

        return nomsComplet.toString();
    }

    /**
     * Retourne l'ensemble des étudiants encadrés par ce chercheur.
     * 
     * @return L'ensemble des étudiants encadrés par ce chercheur.
     */
    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    /**
     * Ajoute un étudiant à l'ensemble des étudiants encadrés par ce chercheur.
     * 
     * @param etudiant L'étudiant à ajouter.
     */
    public void ajouterEtudiant(Etudiant etudiant) {
        if (this.etudiants == null) {
            this.etudiants = new HashSet<>();  // Créer un nouvel ensemble si nécessaire
        }
        if(etudiant != null) {
        	this.etudiants.add(etudiant);
        }
    }

    /**
     * Supprime un étudiant de l'ensemble des étudiants encadrés par ce chercheur.
     * 
     * @param etudiant L'étudiant à supprimer.
     */
    public void supprEtudiant(Etudiant etudiant) {
        if(etudiant!=null) {
        	this.etudiants.remove(etudiant);
        }
    }

    /**
     * Modifie les informations du Chercheur et met à jour l'ensemble des étudiants encadrés.
     * 
     * @param nom Le nouveau nom du chercheur.
     * @param prenom Le nouveau prénom du chercheur.
     * @param age Le nouvel âge du chercheur.
     * @param ville La nouvelle ville du chercheur.
     * @param disciplines Les nouvelles disciplines que le chercheur enseigne.
     * @param numBureau Le nouveau numéro de bureau du chercheur.
     * @param etudiants L'ensemble des nouveaux étudiants à encadrer.
     */
    public void modif(String nom, String prenom, int age, Ville ville, Set<Discipline> disciplines, int numBureau, Set<Etudiant> etudiants) {
        super.modif(nom, prenom, ville, age, disciplines, numBureau);
        
        // Déassocier tous les étudiants de ce chercheur
        for (Etudiant e : this.etudiants) {
            e.change_titulaire(null);
        }

        this.etudiants = etudiants;
        // Associer chaque étudiant au nouveau chercheur
        if (etudiants != null && !etudiants.isEmpty()) {
            for (Etudiant e : etudiants) {
                e.change_titulaire(this);
            }
        }
    }
}
