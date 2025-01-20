package ecosysteme;

import java.util.Set;
import SQL.Ville;

/**
 * Classe abstraite représentant un titulaire. Un titulaire est une personne
 * qui enseigne et possède des disciplines associées, ainsi qu'un numéro de bureau.
 */
public abstract class Titulaire extends Personne {
    private static final long serialVersionUID = 1L;

    /** Ensemble des disciplines enseignées par le titulaire. */
    private Set<Discipline> disciplines;
    
    /** Numéro du bureau du titulaire. */
    private int numBureau;

    /**
     * Constructeur par défaut, initialisant les disciplines à un ensemble vide
     * et le numéro de bureau à 0.
     */
    public Titulaire() {
        this.disciplines = Set.of();
        this.numBureau = 0;
    }

    /**
     * Constructeur initialisant un Titulaire avec des valeurs spécifiques.
     * @param nom Le nom du titulaire.
     * @param prenom Le prénom du titulaire.
     * @param ville La ville de résidence du titulaire.
     * @param age L'âge du titulaire.
     * @param disciplines L'ensemble des disciplines enseignées par le titulaire.
     * @param numBureau Le numéro de bureau du titulaire.
     */
    public Titulaire(String nom, String prenom, Ville ville, int age, 
                     Set<Discipline> disciplines, int numBureau) {
        super(nom, prenom, ville, age);
        this.disciplines = disciplines;
        this.numBureau = numBureau;
    }

    /**
     * Retourne une chaîne de caractères représentant un Titulaire,
     * incluant les informations de la personne et les détails spécifiques au titulaire.
     * @return La chaîne décrivant le titulaire.
     */
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1) + ", Titulaire [numBureau=" + numBureau + ", disciplines=" + disciplines + "]";
    }

    /**
     * Retourne une chaîne de caractères représentant les disciplines du titulaire.
     * Chaque discipline est séparée par une virgule.
     * @return La chaîne des disciplines du titulaire.
     */
    public String getDisciplines() {
        if (this.disciplines == null || this.disciplines.isEmpty()) {
            return "";  // Retourne une chaîne vide si aucune discipline n'est présente
        }
        StringBuilder disciplinesStr = new StringBuilder();
        for (Discipline discipline : disciplines) {
            if (disciplinesStr.length() > 0) {
                disciplinesStr.append(", ");  // Séparateur entre les disciplines
            }
            disciplinesStr.append(discipline.toString());  // Utilise toString() pour chaque discipline
        }
        return disciplinesStr.toString();
    }

    /**
     * Retourne le numéro de bureau sous forme de chaîne de caractères.
     * @return Le numéro de bureau du titulaire.
     */
    public String getNumBureau() {
        return String.valueOf(numBureau);  // Convertit le numéro de bureau en chaîne
    }

    /**
     * Retourne l'ensemble des disciplines enseignées par le titulaire.
     * @return L'ensemble des disciplines du titulaire.
     */
    public Set<Discipline> getDisciplineSet() {
        return this.disciplines;
    }

    /**
     * Ajoute un étudiant au titulaire. Cette méthode est abstraite et doit être implémentée
     * dans les sous-classes.
     * @param etudiant L'étudiant à ajouter.
     */
    public abstract void ajouterEtudiant(Etudiant etudiant);

    /**
     * Supprime un étudiant du titulaire. Cette méthode est abstraite et doit être implémentée
     * dans les sous-classes.
     * @param etudiant L'étudiant à supprimer.
     */
    public abstract void supprEtudiant(Etudiant etudiant);

    /**
     * Modifie les informations d'un titulaire, y compris son nom, prénom, ville, âge,
     * disciplines et numéro de bureau.
     * @param nom Le nouveau nom du titulaire.
     * @param prenom Le nouveau prénom du titulaire.
     * @param ville La nouvelle ville de résidence du titulaire.
     * @param age Le nouvel âge du titulaire.
     * @param disciplines Les nouvelles disciplines du titulaire.
     * @param numBureau Le nouveau numéro de bureau du titulaire.
     */
    public void modif(String nom, String prenom, Ville ville, int age, Set<Discipline> disciplines, int numBureau) {
        super.modif(nom, prenom, ville, age);
        this.disciplines = disciplines;
        this.numBureau = numBureau;
    }
}
