package TriCycleHamiltonien;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Data.data;
import ecosysteme.Personne;
import SQL.Ville;

/**
 * Classe abstraite représentant un tri sur les données d'une liste de personnes.
 * Cette classe récupère les données des chercheurs, maîtres de conférences (MCF),
 * et étudiants présents dans la classe {@code data}.
 */
public abstract class Tri {

    /** Liste des personnes à trier, comprenant chercheurs, MCF et étudiants. */
    protected List<Personne> personnes;

    /**
     * Constructeur de la classe Tri.
     * Initialise la liste des personnes en combinant chercheurs, MCF et étudiants
     * provenant de la classe {@code data}.
     */
    public Tri() {
        this.personnes = new ArrayList<>();
        // Ajout des chercheurs à la liste
        for (Personne p : data.chercheurs) {
            this.personnes.add(p);
        }
        // Ajout des maîtres de conférences à la liste
        for (Personne p : data.MCFs) {
            this.personnes.add(p);
        }
        // Ajout des étudiants à la liste
        for (Personne p : data.etudiants) {
            this.personnes.add(p);
        }
    }

    /**
     * Méthode abstraite qui retourne une liste de villes après application du tri.
     * Les classes qui héritent de {@code Tri} doivent implémenter cette méthode.
     * 
     * @return Une liste d'objets {@code Ville} triés selon un critère spécifique.
     */
    public abstract List<Ville> Getter();
    
    /**
     * Vérifie si une ville est présente dans la base de données de l'écosystème.
     * Affiche un message d'erreur si la ville est absente.
     * 
     * @param v La ville à vérifier.
     * @return {@code true} si la ville est présente dans la liste globale des villes,
     *         {@code false} sinon.
     */
    public Boolean VerifVille(Ville v) {
        if (v == null) {
            JOptionPane.showMessageDialog(
                null, 
                "La ville spécifiée est nulle.", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        for(Ville test: data.villes) {
        	if(v.getNomDepartement().equals(test.getNomDepartement())){
        		return true;
        	}
        }
        
        JOptionPane.showMessageDialog(
            null, 
            "La ville \"" + v.getNomDepartement() + "\" n'est pas présente dans la base de données.", 
            "Erreur", 
            JOptionPane.ERROR_MESSAGE
        );
        return false;
        

     
    }
}
