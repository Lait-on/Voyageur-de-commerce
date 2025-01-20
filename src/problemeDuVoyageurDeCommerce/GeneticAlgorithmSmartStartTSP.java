	package problemeDuVoyageurDeCommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import algorithmeGenetiquePack.GeneticAlgorithm;
import algorithmeGenetiquePack.GeneticIndividualLinear;

/**
 * Classe permettant de résoudre le problème du voyageur de commerce (TSP) 
 * en utilisant un algorithme génétique avec un démarrage intelligent.
 * L'individu génétique commence par une approximation de la solution à la place
 * d'une permutation aléatoire pour améliorer les chances de convergence rapide.
 * 
 * @see GeneticAlgorithm
 * @see GeneticIndividualLinear
 * @see NearestNeighborTSP
 */
public class GeneticAlgorithmSmartStartTSP extends TSPSolveur {

    /**
     * Instance de l'algorithme génétique pour le problème du TSP.
     * 
     * @see GeneticAlgorithm
     */
    private GeneticAlgorithm<SmartIndividuGenetiqueTSP, ArrayList<Integer>> GATSP;

    /**
     * Instance de l'algorithme voisin le plus proche pour une solution heuristique de départ.
     * 
     * @see NearestNeighborTSP
     */
    private NearestNeighborTSP NN;

    /**
     * Constructeur de la classe qui initialise l'algorithme génétique et l'heuristique.
     * 
     * @param TSP L'instance du problème du voyageur de commerce.
     * @see GeneticAlgorithm
     * @see NearestNeighborTSP
     */
    public GeneticAlgorithmSmartStartTSP(TravellingSalesmanProblem TSP) {
        super(TSP);
        
        // Instancie un algorithme génétique général avec pour paramètre un IndividuGenetiqueTSP.
        Supplier<SmartIndividuGenetiqueTSP> voyComIndvSupplier = (Supplier<SmartIndividuGenetiqueTSP>) SmartIndividuGenetiqueTSP::new;
        this.GATSP = new GeneticAlgorithm<SmartIndividuGenetiqueTSP, ArrayList<Integer>>(voyComIndvSupplier);
        
        // Initialise l'algorithme heuristique du voisin le plus proche.
        this.NN = new NearestNeighborTSP(TSP);
        
        // Lien des paramètres de l'algorithme génétique avec les paramètres du TSP.
        this.setParametres(this.GATSP.parameters);
    }

    /**
     * Méthode qui calcule la solution du problème du TSP en utilisant l'algorithme génétique.
     * 
     * @return La solution sous forme d'un tableau d'entiers représentant l'ordre des villes à visiter.
     */
    @Override
    public int[] calculSolutionSansVerifications() {
        Integer[] solInteger = new Integer[TSP.getNbVille()];
        
        // Exécute l'algorithme génétique et transforme la solution en tableau d'entiers.
        this.GATSP.run().toArray(solInteger);
        
        return Arrays.stream(solInteger).mapToInt(Integer::intValue).toArray();
    }

    /**
     * Retourne le nom de l'algorithme utilisé.
     * 
     * @return Le nom de l'algorithme.
     */
    @Override
    public String getNom() {
        return "Algorithme génétique Smart Start";
    }

    /**
     * Retourne une description détaillée de l'algorithme utilisé.
     * 
     * @return La description de l'algorithme.
     */
    @Override
    public String getDescription() {
        return "Application du célèbre algorithme génétique au problème du voyageur de commerce, " +
               "où chaque individu commence déjà par être une approximation de la solution, " +
               "dans l'espoir d'approcher les individus de la solution idéale. Cet algorithme renvoie une solution approximative.";
    }

    /**
     * Classe interne représentant un individu génétique pour le problème du TSP.
     * Chaque individu est une permutation des villes représentant une solution au problème.
     * 
     * @see GeneticIndividualLinear
     */
    class SmartIndividuGenetiqueTSP extends GeneticIndividualLinear<SmartIndividuGenetiqueTSP, Integer> {

        /**
         * Constructeur de l'individu génétique.
         */
        public SmartIndividuGenetiqueTSP() {
            super();
        }

        /**
         * Fonction de fitness utilisée pour évaluer la qualité de la solution représentée par l'individu.
         * 
         * @param val La solution (une permutation des villes).
         * @return Le fitness de l'individu (plus la valeur est élevée, meilleure est la solution).
         */
        @Override
        public Double fitnessFunction(ArrayList<Integer> val) {
            return 1. / TSP.evalCoutSolution(val); // Le fitness est l'inverse du coût de la solution.
        }

        /**
         * Retourne la valeur par défaut de l'individu, qui est une permutation des villes.
         * 
         * @return Une permutation des villes.
         */
        @Override
        public ArrayList<Integer> setDefaultValue() {
            ArrayList<Integer> defVal = new ArrayList<>(); 
            for (int i = 0; i < TSP.getNbVille(); i++) {
                defVal.add(i); // Les villes sont initialisées dans l'ordre.
            }
            return defVal;
        }

        /**
         * Fonction de croisement entre deux individus génétiques pour générer deux descendants.
         * 
         * @param p1 Le premier parent.
         * @param p2 Le deuxième parent.
         * @param c1 Le premier descendant.
         * @param c2 Le deuxième descendant.
         */
        @Override
        public void crossover(SmartIndividuGenetiqueTSP p1, SmartIndividuGenetiqueTSP p2, 
                               SmartIndividuGenetiqueTSP c1, SmartIndividuGenetiqueTSP c2) {
            this.cycleCrossover(p1, p2, c1, c2); // Utilisation d'un croisement de type cycle.
        }

        /**
         * Fonction qui initialise un individu avec une solution heuristique.
         * L'individu commence toujours par une approximation heuristique (voisin le plus proche).
         */
        @Override
        public void randomize() {
            this.chromosome = new ArrayList<>(Arrays.stream(NN.calculSolutionSansVerifications()).boxed().toList());
        }

        /**
         * Applique une mutation sur le chromosome.
         * Ici switchMutate est selectionné.
         */
		@Override
		public void mutate() {
			this.switchMutate();
		}
    }

    /**
     * Vérifie que les entrées nécessaires à l'algorithme sont valides.
     * 
     * @return true si les entrées sont valides, false sinon.
     */
    @Override
    public boolean entreesValides() {
        return this.TSP.getNbVille() > 0; // Vérifie qu'il y a au moins une ville.
    }
}
