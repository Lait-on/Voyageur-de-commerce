package SQL;

import java.io.*;

/**
 * Classe représentant une ville avec ses informations principales.
 * Implémente Serializable pour permettre la sérialisation des objets Ville.
 */
public class Ville implements Serializable {

    private static final long serialVersionUID = 1L; // Identifiant de version pour la sérialisation
    /** Nom de la ville */
    private String nom; 
    
    /** Code postal de la ville */
    private String code; 
    
    /** Population de la ville */
    private int population; 
     
    /** Superficie de la ville en kilomètres carrés */
    private int superficie; 
    
    /** Département auquel appartient la ville */
    private String departement; 

    /**
     * Constructeur de la classe Ville.
     * 
     * @param nom         Le nom de la ville
     * @param code        Le code postal ou INSEE de la ville
     * @param population  La population de la ville
     * @param superficie  La superficie de la ville en kilomètres carrés
     * @param departement Le département auquel appartient la ville
     */
    public Ville(String nom, String code, int population, int superficie, String departement) {
        this.nom = nom;
        this.code = code;
        this.population = population;
        this.superficie = superficie;
        this.departement = departement;
    }

    /**
     * Retourne une représentation textuelle de la ville.
     * 
     * @return Une chaîne de caractères décrivant la ville
     */
    @Override
    public String toString() {
        return "Ville [nom=" + nom + ", code=" + code + ", population=" + population + ", superficie=" + superficie
                + ", departement=" + departement + "]";
    }

    /**
     * Retourne le département de la ville.
     * 
     * @return Le département de la ville
     */
    public String getDepartement() {
        return departement;
    }

    /**
     * Retourne le nom de la ville.
     * 
     * @return Le nom de la ville
     */
    public String getName() {
        return nom;
    }

    /**
     * Retourne le code postal ou INSEE de la ville.
     * 
     * @return Le code de la ville
     */
    public String getCode() {
        return code;
    }

    /**
     * Retourne la population de la ville sous forme de chaîne de caractères.
     * 
     * @return La population de la ville
     */
    public String getPopulation() {
        return Integer.toString(population);
    }

    /**
     * Retourne la superficie de la ville sous forme de chaîne de caractères.
     * 
     * @return La superficie de la ville
     */
    public String getSuperficie() {
        return Integer.toString(superficie);
    }

    /**
     * Retourne une chaîne combinant le nom de la ville et son département.
     * 
     * @return Une chaîne au format "Nom (Département)"
     */
    public String getNomDepartement() {
        return nom + " (" + departement + ")";
    }
    
}
