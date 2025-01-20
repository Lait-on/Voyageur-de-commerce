package ecosysteme;

import java.util.Set;
import SQL.Ville;

/**
 * Classe représentant un Maître de Conférences (MCF).
 * Un MCF est un titulaire qui encadre un seul étudiant pour sa thèse.
 */
public class MCF extends Titulaire {
    private static final long serialVersionUID = 1L;

    /** L'étudiant encadré par ce MCF*/
    private Etudiant etudiant;

    /**
     * Constructeur par défaut de la classe MCF.
     * Initialise l'étudiant à null.
     */
    public MCF() {
        this.etudiant = null;
    }  

    /**
     * Constructeur paramétré de la classe MCF.
     * 
     * @param nom Le nom du MCF.
     * @param prenom Le prénom du MCF.
     * @param age L'âge du MCF.
     * @param ville La ville du MCF.
     * @param disciplines Les disciplines que le MCF enseigne.
     * @param numBureau Le numéro de bureau du MCF.
     * @param etudiant L'étudiant encadré par le MCF.
     */
    public MCF(String nom, String prenom, int age, Ville ville,  
               Set<Discipline> disciplines, int numBureau, Etudiant etudiant) {
        super(nom, prenom, ville, age, disciplines, numBureau);
        this.etudiant = etudiant;
        if (etudiant != null) {
            etudiant.change_titulaire(this);  // L'étudiant est associé à ce MCF
        }
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du MCF,
     * y compris les informations sur l'étudiant si un étudiant est encadré.
     * 
     * @return La représentation sous forme de chaîne de caractères du MCF et de l'étudiant.
     */
    @Override
    public String toString() {
        if (etudiant != null) {
            return super.toString().substring(0, super.toString().length() - 1) + ", [etudiant=" + etudiant.getPrenom() + " " + etudiant.getNom() + "]";
        } else {
            return super.toString();
        }
    }

    /**
     * Retourne le nom et prénom de l'étudiant encadré par ce MCF.
     * 
     * @return Le nom et prénom de l'étudiant, ou une chaîne vide si aucun étudiant n'est encadré.
     */
    public String getEtudiantString() {
        return etudiant != null ? etudiant.getPrenom() + " " + etudiant.getNom() : "";
    }

    /**
     * Retourne l'objet Étudiant encadré par ce MCF.
     * 
     * @return L'étudiant encadré par ce MCF.
     */
    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    /**
     * Associe un étudiant à ce MCF.
     * 
     * @param etudiant L'étudiant à encadrer.
     */
    public void ajouterEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Supprime l'étudiant encadré par ce MCF.
     */
    public void supprEtudiant(Etudiant etudiant) {
        this.etudiant = null;
    }

    /**
     * Modifie les informations du MCF et met à jour l'étudiant encadré.
     * 
     * @param nom Le nouveau nom du MCF.
     * @param prenom Le nouveau prénom du MCF.
     * @param age Le nouvel âge du MCF.
     * @param ville La nouvelle ville du MCF.
     * @param disciplines Les nouvelles disciplines que le MCF enseigne.
     * @param numBureau Le nouveau numéro de bureau du MCF.
     * @param etudiant Le nouvel étudiant à encadrer (ou null si aucun étudiant).
     */
    public void modif(String nom, String prenom, int age, Ville ville, Set<Discipline> disciplines, int numBureau, Etudiant etudiant) {
        super.modif(nom, prenom, ville, age, disciplines, numBureau);
        if (this.etudiant != null) {
            this.etudiant.change_titulaire(null);  // Déassocie l'étudiant du précédent titulaire
        }
        this.etudiant = etudiant;
        if (etudiant != null) {
            etudiant.change_titulaire(this);  // Associe l'étudiant à ce MCF
        }
    }
}
