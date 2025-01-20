package ecosysteme;

import SQL.Ville;
import java.io.Serializable;

/**
 * Classe abstraite représentant une personne. 
 * Elle sert de base pour des sous-classes spécifiques, telles que Titulaire, Etudiant, etc.
 */
public abstract class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Le nom de la personne. */
    private String nom;
    
    /** Le prénom de la personne. */
    private String prenom;
    
    /** La ville de résidence de la personne. */
    private Ville ville;
    
    /** L'âge de la personne. */
    private int age;
    
    /** L'identifiant unique de la personne. */
    private int ID;
    
    /** Compteur statique pour générer des IDs uniques pour chaque personne. */
    private static int nbPersonnes = 0;

    /**
     * Constructeur par défaut initialisant les attributs à des valeurs par défaut.
     */
    public Personne() {
        this.nom = "";
        this.prenom = "";
        this.ville = null;
        this.age = 0;
        this.ID = 0;
    }

    /**
     * Constructeur initialisant une Personne avec des valeurs spécifiques.
     * @param nom Le nom de la personne.
     * @param prenom Le prénom de la personne.
     * @param ville La ville de la personne.
     * @param age L'âge de la personne.
     */
    public Personne(String nom, String prenom, Ville ville, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.age = age;
        this.ID = nbPersonnes;
        nbPersonnes++;
    }

    /**
     * Retourne une représentation sous forme de chaîne de la personne.
     * @return La chaîne décrivant la personne.
     */
    @Override
    public String toString() {
        return "Personne [nom=" + nom + ", prenom=" + prenom + ", ville=" + ville + ", age=" + age + ", ID=" + ID + "]";
    }

    /**
     * @return Le nom de la personne.
     */
    public String getNom() {
        return this.nom;
    }
    
    /**
     * @return Le prénom de la personne.
     */
    public String getPrenom() {
        return this.prenom;
    }
    
    /**
     * @return L'âge de la personne sous forme de chaîne.
     */
    public String getAge() {
        return Integer.toString(this.age);
    }
    
    /**
     * @return La ville de la personne sous la forme d'une chaine de caractère: "nom de la ville (département)".
     */
    public String getVilleNom() {
        return this.ville.getName() + " (" + this.ville.getDepartement() + ")";
    }

    /**
     * @return Le nombre total de personnes créées.
     */
    public int getNbPersonnes() {
        return nbPersonnes;
    }
    
    /**
     * @return L'âge de la personne sous forme d'entier.
     */
    public int getAgeInt() {
        return this.age;
    }

    /**
     * @return La ville de la personne.
     */
    public Ville getVilleObject() {
        return this.ville;
    }

    /**
     * Modifie les informations de la personne.
     * @param nom Le nouveau nom de la personne.
     * @param prenom Le nouveau prénom de la personne.
     * @param ville La nouvelle ville de la personne.
     * @param age Le nouvel âge de la personne.
     */
    public void modif(String nom, String prenom, Ville ville, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.age = age;
    }
}
