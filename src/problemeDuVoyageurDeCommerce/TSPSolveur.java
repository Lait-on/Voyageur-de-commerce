package problemeDuVoyageurDeCommerce;

import java.util.Arrays;

import ParametresHandler.ParametresHandler;

/**
 * Classe abstraite représentant un solveur pour le problème du voyageur de commerce (TSP).
 * Cette classe fournit des méthodes génériques pour résoudre le problème en implémentant des algorithmes spécifiques.
 * Elle permet d'évaluer la validité des entrées, de calculer une solution, et de mesurer le temps d'exécution.
 * 
 * @see TravellingSalesmanProblem
 * @see ParametresHandler
 */
public abstract class TSPSolveur {
    
    /** Le problème du voyageur de commerce (TSP) à résoudre. */
    protected TravellingSalesmanProblem TSP;
    
    /** Le gestionnaire des paramètres de l'algorithme. */
    protected ParametresHandler parametres;
    
    /** Temps d'exécution du dernier calcul effectué (en millisecondes). */
    private long tempsExecutionPrecedent = -1;
    
    /** Indique si l'algorithme est paramétrable. */
    private boolean estParametrable;
    
    /** Indique si l'exécution de l'algorithme a réussi. */
    private boolean executionReussit;
    
    /**
     * Constructeur de la classe TSPSolveur.
     * Initialise les attributs de l'objet avec les paramètres fournis.
     *
     * @param TSP Le problème du voyageur de commerce à résoudre.
     * @see TravellingSalesmanProblem
     */
    public TSPSolveur(TravellingSalesmanProblem TSP) {
        this.TSP = TSP;
        this.estParametrable = false;
        this.parametres = null;
        this.executionReussit = true;
    }
    
    /**
     * Méthode abstraite pour valider les entrées de l'algorithme.
     * Chaque algorithme spécifique doit définir cette méthode pour vérifier si les entrées sont valides.
     *
     * @return true si les entrées sont valides, false sinon.
     */
    public abstract boolean entreesValides();
    
    /**
     * Méthode abstraite pour calculer la solution sans vérification des entrées.
     * Chaque algorithme spécifique doit définir cette méthode pour résoudre le problème du voyageur de commerce.
     *
     * @return Un tableau d'entiers représentant la solution calculée (ordre des villes).
     */
    public abstract int[] calculSolutionSansVerifications();
    
    /**
     * Méthode pour calculer une solution avec vérification des entrées.
     * Si les entrées sont invalides, l'exécution échoue et retourne null.
     * Sinon, la solution est calculée et le temps d'exécution est mesuré.
     *
     * @return Un tableau d'entiers représentant la solution calculée (ordre des villes) ou null si l'exécution a échoué.
     */
    public int[] calculSolutionSafe() {
        
        // Vérification des entrées
        if (!this.entreesValides()) {
            this.executionReussit = false;
            return null;
        }
        
        // Mesure du temps d'exécution
        long startTime = System.nanoTime();
        int[] solution = this.calculSolutionSansVerifications();
        long endTime = System.nanoTime();
        
        // Calcul du temps d'exécution en millisecondes
        this.tempsExecutionPrecedent = (endTime - startTime) / 1000000;
        
        // Vérification de la réussite de l'exécution
        this.executionReussit = solution != null;
        
        return solution;
    }
    
    /**
     * Méthode pour savoir si l'exécution de l'algorithme a réussi.
     * 
     * @return true si l'exécution a réussi, false sinon.
     */
    public boolean estExecutionReussit() {
        return this.executionReussit;
    }
    
    /**
     * Méthode pour obtenir le temps d'exécution du dernier calcul effectué.
     * Si l'exécution a échoué, -1 est retourné.
     *
     * @return Le temps d'exécution en secondes, ou -1 si l'exécution a échoué.
     */
    public float getTempsExecutionPrecedent() {
        if (!estExecutionReussit()) {
            return -1f;
        }
        return this.tempsExecutionPrecedent;
    }
    
    /**
     * Méthode pour savoir si l'algorithme est paramétrable.
     * 
     * @return true si l'algorithme peut être paramétré, false sinon.
     */
    public boolean estParametrable() {
        return this.estParametrable;
    }
    
    /**
     * Méthode pour définir les paramètres de l'algorithme.
     * Si les paramètres sont non nuls, l'algorithme est considéré comme paramétrable.
     *
     * @param parametres Les paramètres à utiliser pour l'algorithme.
     * @see ParametresHandler
     */
    protected void setParametres(ParametresHandler parametres) {
        this.parametres = parametres;
        
        // Si les paramètres sont définis, l'algorithme devient paramétrable
        if (parametres != null)
            this.estParametrable = true;    
    }
    
    /**
     * Méthode abstraite pour obtenir le nom de l'algorithme.
     * Chaque algorithme spécifique doit fournir un nom pour être affiché lors des tests.
     *
     * @return Le nom de l'algorithme.
     */
    public abstract String getNom();
    
    /**
     * Méthode abstraite pour obtenir la description de l'algorithme.
     * Chaque algorithme spécifique doit fournir une description de son fonctionnement.
     *
     * @return Une chaîne de caractères décrivant l'algorithme.
     */
    public abstract String getDescription();
    
    /**
     * Méthode de test pour l'algorithme.
     * Elle calcule une solution, affiche le résultat, et indique si l'exécution a réussi ou échoué.
     */
    public void testAlgo() {
        int[] solution = this.calculSolutionSafe();
        
        // Vérification du succès de l'exécution
        if (estExecutionReussit()) {
            System.out.println("Algorithm : \"" + this.getNom() + "\",  solution trouvée:");
            System.out.println("Solution : " + Arrays.toString(solution) +
                               "\nCoût de la solution trouvée : " + this.TSP.evalCoutSolution(solution) + 
                               "\nTemps calculé : " + this.getTempsExecutionPrecedent() / 1000 + " s\n");
        } else {
            System.out.println("Échec de l'exécution.");
        }
    }
}
