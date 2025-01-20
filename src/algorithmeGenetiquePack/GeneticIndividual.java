package algorithmeGenetiquePack;

/**
 * Classe abstraite représentant un individu dans un algorithme génétique.
 * Cette classe sert de base pour créer des individus avec des chromosomes
 * spécifiques et une fonction d'évaluation adaptée au problème à résoudre.
 *
 * @param <Indv> Le type spécifique de l'individu (auto-référentiel pour les sous-classes).
 * @param <V> Le type du chromosome de l'individu.
 */
public abstract class GeneticIndividual<Indv extends GeneticIndividual<Indv, V>, V> implements Comparable<Indv> {

    /**
     * Chromosome représentant l'individu dans l'algorithme génétique.
     */
    protected V chromosome;

    /**
     * Constructeur par défaut qui initialise l'individu avec une valeur par défaut.
     */
    public GeneticIndividual() {	
        this.chromosome = this.setDefaultValue();
    }

    /**
     * Définit la valeur par défaut du chromosome.
     * Cette méthode est appelée lors de l'initialisation d'un individu.
     *
     * @return La valeur par défaut du chromosome.
     */
    public abstract V setDefaultValue();

    /**
     * Fonction de fitness qui évalue la qualité d'un chromosome donné.
     *
     * @param val Le chromosome à évaluer.
     * @return La valeur de fitness associée au chromosome.
     */
    public abstract Double fitnessFunction(V val);

    /**
     * Retourne la valeur de fitness de cet individu.
     * La valeur est calculée uniquement si elle n'a pas déjà été évaluée.
     *
     * @return La valeur de fitness de cet individu.
     */
    public Double getFitnessValue() {
        return this.fitnessFunction(this.chromosome);
    }

    /**
     * Randomise le chromosome de l'individu de manière aléatoire.
     * Cette méthode est utilisée pour générer une diversité initiale dans la population.
     */
    public abstract void randomize();

    /**
     * Effectue une mutation sur le chromosome de l'individu.
     * Cette méthode introduit des variations pour éviter la stagnation dans l'algorithme.
     */
    public abstract void mutate();

    /**
     * Effectue un croisement (crossover) entre deux individus parents pour produire deux enfants.
     *
     * @param p1 Le premier parent.
     * @param p2 Le second parent.
     * @param c1 Le premier enfant (résultat du croisement).
     * @param c2 Le second enfant (résultat du croisement).
     */
    public abstract void crossover(Indv p1, Indv p2, Indv c1, Indv c2);

    /**
     * Retourne une copie du chromosome de l'individu.
     *
     * @return Une copie indépendante du chromosome.
     */
    public abstract V getChromosomeCopy();

    /**
     * Compare cet individu à un autre en fonction de leur valeur de fitness.
     *
     * @param otherIndv L'autre individu à comparer.
     * @return Un entier négatif, nul ou positif si cet individu est respectivement moins adapté,
     *         aussi adapté ou plus adapté que l'autre individu.
     */
    @Override
    public int compareTo(Indv otherIndv) {
        return Double.compare(this.getFitnessValue(), otherIndv.getFitnessValue());
    }
}
