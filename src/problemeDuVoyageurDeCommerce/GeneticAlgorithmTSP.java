package problemeDuVoyageurDeCommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import algorithmeGenetiquePack.*;

/**
 * Cette classe représente un algorithme génétique appliqué au problème du voyageur de commerce (TSP).
 * Elle hérite de la classe {@link TSPSolveur} et utilise l'algorithme génétique pour résoudre ce problème de manière approchée.
 * 
 * @see TSPSolveur
 * @see GeneticAlgorithm
 * @see GeneticIndividualLinear
 */
public class GeneticAlgorithmTSP extends TSPSolveur {

    /**
     * L'algorithme génétique utilisé pour résoudre le problème du voyageur de commerce.
     * Il est générique et utilise des individus de type {@link IndividuGenetiqueTSP}.
     * 
     * @see GeneticAlgorithm
     */
    private GeneticAlgorithm<IndividuGenetiqueTSP, ArrayList<Integer>> GATSP;

    /**
     * Constructeur de la classe {@link GeneticAlgorithmTSP}.
     * 
     * @param TSP Instance du problème du voyageur de commerce (TSP) à résoudre.
     * Cette méthode initialise l'algorithme génétique en spécifiant le type d'individu à utiliser.
     * 
     * @see GeneticAlgorithm
     * @see IndividuGenetiqueTSP
     */
    public GeneticAlgorithmTSP(TravellingSalesmanProblem TSP) {
        super(TSP);

        // Crée un fournisseur d'individus génétiques (de type IndividuGenetiqueTSP)
        Supplier<IndividuGenetiqueTSP> voyComIndvSupplier = (Supplier<IndividuGenetiqueTSP>) IndividuGenetiqueTSP::new;
        
        // Initialise l'algorithme génétique avec le supplier d'individus
        this.GATSP = new GeneticAlgorithm<IndividuGenetiqueTSP, ArrayList<Integer>>(voyComIndvSupplier);
        
        // Lien des paramètres de l'algorithme génétique avec les paramètres de la classe de base
        this.setParametres(this.GATSP.parameters);
    }

    /**
     * Calcule une solution approchée au problème du voyageur de commerce en utilisant l'algorithme génétique.
     * 
     * @return Un tableau d'entiers représentant l'ordre des villes dans la solution approchée.
     */
    @Override
    public int[] calculSolutionSansVerifications() {
        Integer[] solInteger = new Integer[TSP.getNbVille()];
        
        // Exécute l'algorithme génétique et convertit le résultat en tableau d'entiers
        this.GATSP.run().toArray(solInteger);
        
        return Arrays.stream(solInteger).mapToInt(Integer::intValue).toArray();
    }

    /**
     * Retourne le nom de l'algorithme utilisé.
     * 
     * @return Le nom de l'algorithme : "Algorithme génétique".
     */
    @Override
    public String getNom() {
        return "Algorithme génétique";
    }

    /**
     * Retourne une description de l'algorithme utilisé.
     * 
     * @return Une description de l'algorithme appliqué au problème du voyageur de commerce.
     */
    @Override
    public String getDescription() {
        return "Application du célèbre algorithme génétique au problème du voyageur de commerce. Cet algorithme renvoie une solution approchée.";
    }

    /**
     * Cette classe représente un individu génétique pour l'algorithme génétique du TSP.
     * Elle définit les méthodes nécessaires pour gérer les individus dans l'algorithme.
     * 
     * @see GeneticIndividualLinear
     */
    class IndividuGenetiqueTSP extends GeneticIndividualLinear<IndividuGenetiqueTSP, Integer> {

        /**
         * Constructeur de l'individu génétique pour le problème du voyageur de commerce.
         * Ce constructeur initialise les attributs nécessaires pour l'individu génétique.
         */
        public IndividuGenetiqueTSP() {
            super();
        }

        /**
         * Fonction de fitness qui évalue la qualité d'une solution en fonction du coût de la tournée.
         * 
         * @param val La solution représentée par une liste d'indices des villes.
         * @return Le score de fitness de la solution, qui est l'inverse du coût.
         */
        @Override
        public Double fitnessFunction(ArrayList<Integer> val) {
            return 1. / TSP.evalCoutSolution(val);
        }

        /**
         * Définit la valeur par défaut de l'individu génétique, qui est une permutation des villes.
         * 
         * @return Une liste d'entiers représentant une solution par défaut.
         */
        @Override
        public ArrayList<Integer> setDefaultValue() {
            ArrayList<Integer> defVal = new ArrayList<>();
            for (int i = 0; i < TSP.getNbVille(); i++) {
                defVal.add(i);
            }
            return defVal;
        }

        /**
         * Applique le croisement entre deux parents pour générer deux descendants.
         * Cette méthode utilise le croisement de type "cycle crossover" spécifique aux permutations.
         * 
         * @param p1 Le premier parent.
         * @param p2 Le deuxième parent.
         * @param c1 Le premier enfant généré.
         * @param c2 Le deuxième enfant généré.
         */
        @Override
        public void crossover(IndividuGenetiqueTSP p1, IndividuGenetiqueTSP p2, IndividuGenetiqueTSP c1, IndividuGenetiqueTSP c2) {
            this.cycleCrossover(p1, p2, c1, c2);
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
     * Vérifie si les entrées sont valides pour l'algorithme génétique.
     * 
     * @return true si le nombre de villes est supérieur à 0, false sinon.
     */
    @Override
    public boolean entreesValides() {
        return this.TSP.getNbVille() > 0;
    }
}
