package algorithmeGenetiquePack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Classe abstraite représentant un individu génétique avec un chromosome linéaire (liste ordonnée).
 * Cette classe générique permet de manipuler des chromosomes composés de tout type d'éléments.
 * 
 * @param <Indv> Le type de l'individu qui hérite de cette classe.
 * @param <E> Le type des gènes constituant le chromosome.
 */
public abstract class GeneticIndividualLinear<Indv extends GeneticIndividualLinear<Indv, E>, E> extends GeneticIndividual<Indv, ArrayList<E>> {

    /**
     * Constructeur par défaut pour initialiser l'individu.
     */
    public GeneticIndividualLinear() {
        super();
    }

    /**
     * Récupère le gène à une position donnée dans le chromosome.
     * 
     * @param i L'indice du gène à récupérer.
     * @return Le gène à l'indice spécifié.
     */
    public E getGene(int i) {
        return this.chromosome.get(i);
    }

    /**
     * Modifie un gène à une position donnée dans le chromosome.
     * 
     * @param i L'indice du gène à modifier.
     * @param gene Le nouveau gène à placer à cet indice.
     * @return L'ancien gène qui était à cet indice.
     */
    public E setGene(int i, E gene) {
        return this.chromosome.set(i, gene);
    }

    /**
     * Copie le contenu du chromosome dans un tableau donné.
     * 
     * @param arrayToPopulate Le tableau à remplir avec les gènes du chromosome.
     */
    public void toArray(E[] arrayToPopulate) {
        this.chromosome.toArray(arrayToPopulate);
    }

    /**
     * Mélange aléatoirement les gènes du chromosome pour générer une nouvelle configuration.
     */
    @Override
    public void randomize() {
        Collections.shuffle(this.chromosome);
    }

    /**
     * Applique une mutation sur le chromosome en échangeant deux gènes choisis aléatoirement.
     */
    public void switchMutate() {
        int indexToSwap1 = (int) (Math.random() * this.chromosome.size());
        int indexToSwap2 = (int) (Math.random() * this.chromosome.size());

        // Échange des gènes sélectionnés.
        E tmp = this.chromosome.get(indexToSwap1);
        this.chromosome.set(indexToSwap1, this.chromosome.get(indexToSwap2));
        this.chromosome.set(indexToSwap2, tmp);
    }

    /**
     * Applique un croisement en cycle (Cycle Crossover) entre deux parents pour générer deux enfants.
     * 
     * @param p1 Le premier parent.
     * @param p2 Le second parent.
     * @param c1 Le premier enfant (à remplir).
     * @param c2 Le second enfant (à remplir).
     */
    public void cycleCrossover(GeneticIndividualLinear<Indv, E> p1, GeneticIndividualLinear<Indv, E> p2,
                                GeneticIndividualLinear<Indv, E> c1, GeneticIndividualLinear<Indv, E> c2) {

        Random rand = new Random();
        boolean pileOuFace = rand.nextBoolean();

        // Tableau pour suivre quels indices ont été fixés
        boolean[] isFixed = new boolean[this.chromosome.size()];

        for (int i = 0; i < this.chromosome.size(); i++) {
            if (!isFixed[i]) {
                isFixed[i] = true;

                // Détermine quel enfant hérite du gène du premier parent
                pileOuFace = rand.nextBoolean();
                GeneticIndividualLinear<Indv, E> child1 =  pileOuFace ? c1 : c2;
                GeneticIndividualLinear<Indv, E> child2 = !pileOuFace ? c1 : c2;

                // Fixe les gènes initiaux des deux enfants
                child1.setGene(i, p1.getGene(i));
                child2.setGene(i, p2.getGene(i));

                // Complète le cycle en suivant les correspondances entre les deux parents
                E toFix = p2.getGene(i);
                while (!toFix.equals(p1.getGene(i))) {
                    for (int k = 0; k < this.chromosome.size(); k++) {
                        if (p1.getGene(k).equals(toFix)) {
                            child1.setGene(k, p1.getGene(k));
                            child2.setGene(k, p2.getGene(k));
                            toFix = p2.getGene(k);
                            isFixed[k] = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Renvoie une copie indépendante du chromosome de cet individu.
     * 
     * @return Une copie du chromosome sous forme de {@link ArrayList}.
     */
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<E> getChromosomeCopy() {
        return (ArrayList<E>) this.chromosome.clone();
    }
}
