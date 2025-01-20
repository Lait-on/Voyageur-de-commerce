package problemeDuVoyageurDeCommerce;

/**
 * Classe implémentant l'algorithme de Held-Karp pour résoudre le problème du voyageur de commerce (TSP).
 * Utilise la programmation dynamique pour générer la solution optimale avec une complexité en temps de O(n² 2^n)
 * et une complexité en mémoire de O(n 2^n).
 * 
 * @see TravellingSalesmanProblem
 * @see TSPSolveur
 */
public class Held_Karp extends TSPSolveur {

    /**
     * Constructeur de l'algorithme de Held-Karp.
     * 
     * @param TSP Le problème du voyageur de commerce (TSP) à résoudre.
     * @see TravellingSalesmanProblem
     */
    public Held_Karp(TravellingSalesmanProblem TSP) {
        super(TSP);
    }

    /**
     * Calcule la solution du problème TSP en utilisant l'algorithme de Held-Karp sans vérifications.
     * Cette méthode utilise la programmation dynamique pour trouver la solution optimale.
     * 
     * @return un tableau représentant l'ordre des villes dans la solution optimale.
     */
    
    //coutMemoire(calculSolutionSansVerifications) =
    // = coutMemoire(G) + coutMemoire(precedent) + coutMemoire(idSousEnsemblesParTaille)
    // = ((4+1) * n * (2**n) + 2**(n-1)*4) octets
    public int[] calculSolutionSansVerifications() {

        int n = this.TSP.getNbVille();
        // Si n <= 1, il n'y a pas de solution possible, retourne un tableau vide ou de taille 1
        if (n <= 1) {
            return new int[n];
        }

        // Nombre de sous-ensembles possibles (2^n)
        int deuxExpN = (1 << n);

        // Tableau G pour stocker les distances minimales des sous-ensembles 
        //coutMemoire(G) = 4 * n * 2^n octet
        float[] G = new float[n * deuxExpN];

        // Tableau precedent pour garder une trace de la dernière ville visitée dans chaque sous-ensemble
        //coutMemoire(precedent) = 1 * n * 2^n octet
        byte[] precedent = new byte[n * deuxExpN];

        // Initialisation des distances pour les sous-ensembles de taille 1
        for (int k = 1; k < n; k++) {
            int singletonK_k_id = (1 << k);
            G[singletonK_k_id * n + k] = (float) this.TSP.distanceEntre(0, k);
        }

        // Calcul des sous-ensembles pour chaque taille (2 à n-1)
        
        //coutMemoire(idSousEnsemblesParTaille) = 4 * 2^(n-1) octets
        int[][] idSousEnsemblesParTaille = Held_Karp.calculeIdSousEnsemble(n - 1);
        
        for (int tailleSousEnsemble = 2; tailleSousEnsemble < n; tailleSousEnsemble++) {

            for (int idSousEnsembleCourant : idSousEnsemblesParTaille[tailleSousEnsemble - 1]) {
                idSousEnsembleCourant <<= 1;

                if (idSousEnsembleCourant == 0) {
                    break;
                }

                // Calcul des distances minimales pour chaque ville k dans le sous-ensemble courant
                for (int k = 1; k < n; k++) {
                    if (((1 << k) & idSousEnsembleCourant) != 0) {
                        float min = Float.MAX_VALUE;
                        int minVille = -1;

                        // Recherche de la ville m qui permet d'atteindre la ville k avec la distance minimale
                        for (int m = 1; m < n; m++) {
                            if (m != k && (((1 << m) & idSousEnsembleCourant) != 0)) {
                                float distToM = G[(idSousEnsembleCourant - (1 << k)) * n + m] + (float) this.TSP.distanceEntre(m, k);
                                if (distToM < min) {
                                    min = distToM;
                                    minVille = m;
                                }
                            }
                        }

                        G[idSousEnsembleCourant * n + k] = min;
                        precedent[idSousEnsembleCourant * n + k] = (byte) minVille;
                    }
                }
            }
        }

        // Dernière étape : déterminer le chemin optimal en revenant de la dernière ville vers la première
        int isEnsembleSansVilleDepart = (deuxExpN - 1) - 1; 
        float min = Float.MAX_VALUE;
        int dernierVille = -1;
        for (int k = 1; k < n; k++) {
            float distToM = G[isEnsembleSansVilleDepart * n + k] + (float) this.TSP.distanceEntre(k, 0);
            if (distToM < min) {
                min = distToM;
                dernierVille = k;
            }
        }

        // Construction de la solution à partir des tableaux G et precedent
        int[] solution = new int[n];
        int subsetCourant = (deuxExpN - 1) - 1;

        solution[0] = 0;              
        solution[n - 1] = dernierVille;

        for (int i = n - 2; i > 0; i--) {
            int villeCourante = solution[i + 1];
            solution[i] = precedent[subsetCourant * n + villeCourante];
            subsetCourant -= (1 << villeCourante);
        }

        return solution;
    }

    /**
     * Calcule les identifiants des sous-ensembles de villes.
     * Organise les sous-ensembles par poids de Hamming.
     * 
     * @param n Le nombre de villes.
     * @return Un tableau 2D contenant les identifiants des sous-ensembles ordonnés par poids de Hamming.
     */
    private static int[][] calculeIdSousEnsemble(int n) {
        int[][] nombreOrdonneParHammingWeight = new int[n][];
        int[] positions = new int[n];
        
        //coutMemoire(nombreOrdonneParHammingWeight) 
        // = sum(k = 0, k = n,coutMemoire(nombreOrdonneParHammingWeight[k])) octets
        // = sum(k = 0, k = n, 4 * C(n , k + 1) ) octets
        // = 4 * 2^n octets
        for (int k = 0; k < n; k++) {        	
        	//coutMemoire(nombreOrdonneParHammingWeight[k]) = 4 * C(n , k + 1) octets
            nombreOrdonneParHammingWeight[k] = new int[coefficientBinomiale(n, k + 1)];
        }

        // Remplissage des sous-ensembles ordonnés par poids de Hamming
        for (int k = 1; k < (1 << n); k++) {
            int weight = Integer.bitCount(k);
            nombreOrdonneParHammingWeight[weight - 1][positions[weight - 1]++] = k;
        }

        return nombreOrdonneParHammingWeight;
    }

    /**
     * Calcule le coefficient binomial C(n, k), qui donne le nombre de façons de choisir k éléments parmi n.
     * 
     * @param n Le nombre total d'éléments.
     * @param k Le nombre d'éléments à choisir.
     * @return Le coefficient binomial C(n, k).
     */
    private static int coefficientBinomiale(int n, int k) {
        if (k > n) return 0;
        if (k == 0 || k == n) return 1;

        int result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - i + 1);
            result /= i;
        }
        return result;
    }

    /**
     * Retourne le nom de l'algorithme.
     * 
     * @return Le nom de l'algorithme ("Held-Karp").
     */
    @Override
    public String getNom() {
        return "Held-Karp";
    }

    /**
     * Retourne une description détaillée de l'algorithme de Held-Karp.
     * 
     * @return Une description textuelle de l'algorithme.
     */
    @Override
    public String getDescription() {
        return "L'algorithme de Held-Karp utilise la programmation dynamique pour "
                + "la résolution du problème du voyageur de commerce. Cet algorithme renvoie une solution exacte "
                + "et a une complexité de O(n² 2^n), ce qui est considérable mais peu par rapport à O(n!) de "
                + "l'algorithme naïf. Là où cet algorithme est plus coûteux, c'est en mémoire. "
                + "En effet, sa complexité spatiale de O(n 2^n) nécessite une grande quantité de RAM "
                + "pour des problèmes avec plus de 24 villes. Pour notre implémentation, le coût mémoire est "
                + "donné par la formule suivante : ((4 + 1) * n * (2^n) + 2^n * 4) / 1000000 Mo, "
                + "donc pour 26 villes, environ 9 Go de RAM.\nhttps://en.wikipedia.org/wiki/Held%E2%80%93Karp_algorithm";
    }

    /**
     * Vérifie que les entrées sont valides (nombre de villes entre 1 et 26 inclus).
     * 
     * @return true si les entrées sont valides, false sinon.
     */
    @Override
    public boolean entreesValides() {
        return this.TSP.getNbVille() > 0 && this.TSP.getNbVille() < 27;
    }
}
