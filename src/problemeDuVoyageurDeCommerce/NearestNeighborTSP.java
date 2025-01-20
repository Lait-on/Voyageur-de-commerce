package problemeDuVoyageurDeCommerce;

import java.util.Random;

/**
 * Classe implémentant l'algorithme du plus proche voisin (Nearest Neighbor) pour résoudre le problème du voyageur de commerce (TSP).
 * L'algorithme consiste à construire une solution approchée en sélectionnant à chaque étape la ville la plus proche de la précédente.
 * Cette approche ne garantit pas la solution optimale mais fournit une approximation.
 * 
 * @see TSPSolveur
 * @see TravellingSalesmanProblem
 */
public class NearestNeighborTSP extends TSPSolveur {

    /**
     * Constructeur de la classe NearestNeighborTSP.
     * 
     * @param TSP L'instance du problème du voyageur de commerce à résoudre, généralement une instance de {@link TravellingSalesmanProblem}.
     */
    public NearestNeighborTSP(TravellingSalesmanProblem TSP) {
        super(TSP);
    }

    /**
     * Calcule une solution approximative au problème du voyageur de commerce en utilisant l'algorithme du plus proche voisin.
     * 
     * @return Un tableau d'entiers représentant l'ordre des villes à visiter pour une solution approximée.
     *         La première ville est l'indice de départ choisi au hasard, et les villes suivantes sont sélectionnées
     *         en fonction de la proximité avec la ville précédente.
     *         Si le nombre de villes est 1, retourne un tableau contenant uniquement l'indice 0.
     * 
     * @see TravellingSalesmanProblem#distanceEntre(int, int)
     */
    public int[] calculSolutionSansVerifications() {
        // Si le problème ne contient qu'une seule ville, retourner directement la solution triviale
        if (this.TSP.getNbVille() == 1) {
            return new int[] { 0 };
        }

        Random rand = new Random();

        // Tableau pour suivre les villes explorées
        boolean[] villesExplore = new boolean[this.TSP.getNbVille()];
        // Tableau pour stocker l'ordre des villes dans la solution
        int[] solution = new int[this.TSP.getNbVille()];

        // Sélection aléatoire de la ville de départ
        int villeDepart = rand.nextInt(this.TSP.getNbVille());
        solution[0] = villeDepart;
        villesExplore[villeDepart] = true;

        int nbVilleVisitee = 1;
        // Boucle pour visiter toutes les villes
        while (nbVilleVisitee < this.TSP.getNbVille()) {

            // Recherche de la ville la plus proche de la ville précédente
            double distanceMinimal = Double.MAX_VALUE;
            int minVille = -1;
            for (int i = 0; i < this.TSP.getNbVille(); i++) {
                if (!villesExplore[i]) {
                    // Calcul de la distance entre la ville actuelle et la ville précédente dans la solution
                    double distanceAvecVillePrecedente = this.TSP.distanceEntre(solution[nbVilleVisitee - 1], i);
                    if (distanceMinimal > distanceAvecVillePrecedente) {
                        distanceMinimal = distanceAvecVillePrecedente;
                        minVille = i;
                    }
                }
            }

            // Marquer la ville comme visitée et l'ajouter à la solution
            villesExplore[minVille] = true;
            solution[nbVilleVisitee++] = minVille;
        }
        return solution;
    }

    /**
     * Retourne le nom de l'algorithme utilisé pour résoudre le problème du voyageur de commerce.
     * 
     * @return Le nom de l'algorithme, ici "Nearest neighbor".
     */
    @Override
    public String getNom() {
        return "Nearest neighbor";
    }

    /**
     * Fournit une description de l'algorithme utilisé pour résoudre le problème du voyageur de commerce.
     * 
     * @return Une chaîne de caractères contenant une description détaillée de l'algorithme du plus proche voisin.
     * 
     * @see #calculSolutionSansVerifications()
     */
    @Override
    public String getDescription() {
        return "Construction d'un chemin approché de la solution optimale en sélectionnant à chaque étape la ville la plus proche de la précédente. "
                + "Plus d'informations : https://en.wikipedia.org/wiki/Nearest_neighbour_algorithm";
    }

    /**
     * Vérifie la validité des entrées nécessaires pour l'algorithme.
     * 
     * @return true si le nombre de villes est supérieur à 0, sinon false.
     * 
     * @see TravellingSalesmanProblem#getNbVille()
     */
    @Override
    public boolean entreesValides() {
        return this.TSP.getNbVille() > 0;
    }
}
