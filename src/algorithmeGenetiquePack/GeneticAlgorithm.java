package algorithmeGenetiquePack;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

import ParametresHandler.ParametresHandler;

/**
 * Classe principale de l'algorithme génétique. Cette classe permet de gérer
 * l'ensemble du processus génétique, incluant la génération initiale, la
 * sélection des parents et la gestion des
 * paramètres associés.
 *
 * @param <Indv> Le type d'individu géré par l'algorithme, qui doit étendre
 *              {@link GeneticIndividual}.
 * @param <V>    Le type des valeurs manipulées par les individus.
 */
public class GeneticAlgorithm<Indv extends GeneticIndividual<Indv, V>, V> {

    /** Supplier pour créer de nouveaux individus. */
    private final Supplier<Indv> supplier;

    /** Paramètres de l'algorithme génétique. */
    public Parameters parameters;

    /**
     * Constructeur principal pour initialiser l'algorithme génétique avec un
     * supplier d'individus.
     *
     * @param supplier permettant de générer des individus.
     */
    public GeneticAlgorithm(Supplier<Indv> supplier) {
        this.supplier = supplier;
        this.parameters = new Parameters(100, 1d, 0.5d, 100);
    }

    /**
     * Lance l'algorithme génétique et renvoie le meilleur individu trouvé.
     *
     * @return Le meilleur individu après l'exécution des itérations.
     */
    public Indv run() {
        Random rand = new Random();

        Indv[] population = this.initializeRandomPopulationArray();
        int it = 0;

        while (it < this.parameters.getNbIteration()) {
            Indv[] newPop = this.getIndvArray(this.parameters.getSizePop());

            for (int i = 0; i < this.parameters.getSizePop() / 2; i++) {
            	
            	Indv parent1 = this.supplier.get();
                Indv parent2 = this.supplier.get();
                this.selectParent(parent1, parent2, population);

                Indv child1 = this.supplier.get();
                Indv child2 = this.supplier.get();
                this.crossover(parent1, parent2, child1, child2);

                if (rand.nextFloat() < this.parameters.getProbMut()) {
                    child1.mutate();
                }

                if (rand.nextFloat() < this.parameters.getProbMut()) {
                    child2.mutate();
                }

                newPop[2 * i] = child1;
                newPop[2 * i + 1] = child2;
            }

            this.populationReplace(population, newPop);
            it++;
        }

        Arrays.sort(population);
        return population[this.parameters.getSizePop() - 1];
    }

    /**
     * Remplace la population actuelle par la nouvelle, en respectant le taux
     * d'élitisme.
     *
     * @param population Population actuelle.
     * @param newPop     Nouvelle population générée.
     */
    private void populationReplace(Indv[] population, Indv[] newPop) {
        Arrays.sort(population);
        Arrays.sort(newPop);

        for (int k = 0; k < this.parameters.getSizePop() * (1 - this.parameters.getRateElite()); k++) {
            population[k] = newPop[this.parameters.getSizePop() - k - 1];
        }
    }

    /**
     * Utilise la reflexion pour allouer un tableau d'individus
     *
     * @param size Taille du tableau souhaitée.
     * @return Un tableau vide d'individus.
     */
    @SuppressWarnings("unchecked")
    private Indv[] getIndvArray(int size) {
        Indv prototype = this.supplier.get();
        return (Indv[]) Array.newInstance(prototype.getClass(), size);
    }

    /**
     * Initialise une population aléatoire.
     *
     * @return Un tableau contenant la population initiale randomisée.
     */
    private Indv[] initializeRandomPopulationArray() {
        Indv[] population = this.getIndvArray(this.parameters.getSizePop());
        for (int i = 0; i < this.parameters.getSizePop(); i++) {
            population[i] = this.supplier.get();
            population[i].randomize();
        }
        return population;
    }

    /**
     * Sélectionne deux parents à partir de la population existante, en utilisant
     * une méthode de sélection basée sur la fitness cumulée.
     *
     * @param p1         Premier parent sélectionné.
     * @param p2         Deuxième parent sélectionné.
     * @param population Population actuelle.
     */
    private void selectParent(Indv p1, Indv p2, Indv[] population) {
        int sizePop = this.parameters.getSizePop();
        double[] indivFitnessSegments = new double[sizePop + 1];
        indivFitnessSegments[0] = 0;

        for (int i = 1; i < sizePop + 1; i++) {
            indivFitnessSegments[i] = indivFitnessSegments[i - 1] + population[i - 1].getFitnessValue();
        }

        double RandomPosParent1 = Math.random() * indivFitnessSegments[sizePop];
        double RandomPosParent2 = Math.random() * indivFitnessSegments[sizePop];

        for (int i = 0; i < sizePop; i++) {
            if (indivFitnessSegments[i] < RandomPosParent1 && RandomPosParent1 < indivFitnessSegments[i + 1]) {
                p1.chromosome = population[i].getChromosomeCopy();
            }
            if (indivFitnessSegments[i] < RandomPosParent2 && RandomPosParent2 < indivFitnessSegments[i + 1]) {
                p2.chromosome = population[i].getChromosomeCopy();
            }
        }
    }

    /**
     * Réalise le croisement entre deux parents pour générer deux enfants.
     *
     * @param p1 Premier parent.
     * @param p2 Deuxième parent.
     * @param c1 Premier enfant généré.
     * @param c2 Deuxième enfant généré.
     */
    private void crossover(Indv p1, Indv p2, Indv c1, Indv c2) {
        Indv prototype = this.supplier.get();
        prototype.crossover(p1, p2, c1, c2);
    }

    /**
     * Classe interne pour gérer les paramètres de l'algorithme génétique.
     */
    public class Parameters extends ParametresHandler {
        private Integer sizePop;
        private Double probMut;
        private Double rateElite;
        private Integer nbIteration;

        /**
         *
         * @param sizePop     Taille de la population.
         * @param probMut     Probabilité de mutation.
         * @param rateElite   Taux d'élitisme.
         * @param nbIteration Nombre maximal d'itérations.
         */
        public Parameters(Integer sizePop, Double probMut, Double rateElite, Integer nbIteration) {
            this.sizePop = sizePop;
            this.probMut = probMut;
            this.rateElite = rateElite;
            this.nbIteration = nbIteration;
        }

        public Integer getSizePop() {
            return sizePop;
        }

        public void setSizePop(Integer sizePop) {
            this.sizePop = sizePop;
        }

        public double getProbMut() {
            return probMut;
        }

        public void setProbMut(Double probMut) {
            this.probMut = probMut;
        }

        public double getRateElite() {
            return rateElite;
        }

        public void setRateElite(Double rateElite) {
            this.rateElite = rateElite;
        }

        public Integer getNbIteration() {
            return nbIteration;
        }

        public void setNbIteration(Integer nbIteration) {
            this.nbIteration = nbIteration;
        }

        public void set(Integer sizePop, Double d, Double e, Integer nbIteration) {
            this.sizePop = sizePop;
            this.probMut = d;
            this.rateElite = e;
            this.nbIteration = nbIteration;
        }
    }
}
