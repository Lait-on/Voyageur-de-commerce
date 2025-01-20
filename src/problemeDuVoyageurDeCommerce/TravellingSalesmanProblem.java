package problemeDuVoyageurDeCommerce;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ParametresHandler.ParametresHandler;
import SQL.Ville;

/**
 * Classe représentant le problème du voyageur de commerce (TSP - Travelling Salesman Problem).
 * Elle gère les villes, les distances entre elles, et l'exécution de différents solveurs pour résoudre le problème.
 * 
 * @see {@link SQL.Ville} pour la classe représentant une ville
 * @see {@link TSPSolveur} pour l'interface des solveurs
 * @see {@link NearestNeighborTSP} pour un solveur de type voisin le plus proche
 * @see {@link Held_Karp} pour un solveur basé sur l'algorithme de Held-Karp
 * @see {@link GeneticAlgorithmTSP} pour un solveur basé sur l'algorithme génétique
 * @see {@link GeneticAlgorithmSmartStartTSP} pour un solveur basé sur l'algorithme génétique avec démarrage intelligent
 */
public class TravellingSalesmanProblem {
	
	/** Liste des villes à visiter */
	private Ville[] villes;
	
	/** Matrice des distances entre les villes */
	private double[][] distances;
	
	/** Liste des solveurs implémentés pour résoudre le problème */
	private ArrayList<TSPSolveur> solveurs;
	
	/** Solveur actuellement utilisé */
	private TSPSolveur solveur;
	
	/** Dernière solution brute calculée */
	private int[] derniereSolBrute;
	
	/** Solveur ayant calculé la dernière solution */
	private TSPSolveur solveurDerniereSolution;
	
	/** Liste des classes de solveurs implémentés */
	static ArrayList<Class<? extends TSPSolveur>> solveursImplemente = new ArrayList<>(Arrays.asList(NearestNeighborTSP.class, Held_Karp.class, GeneticAlgorithmTSP.class, GeneticAlgorithmSmartStartTSP.class));
	
	/**
	 * Constructeur de la classe TravellingSalesmanProblem.
	 * Initialise les villes et les distances, et configure les solveurs.
	 * 
	 * @param villes Liste des villes à visiter
	 * @param distances Matrice des distances entre les villes
	 * @throws Exception Si une erreur survient lors de l'initialisation des solveurs
	 */
	public TravellingSalesmanProblem(Ville[] villes, double[][] distances) throws Exception {
		this.initialiseSolveur();
		this.villes = villes.clone();
		this.distances = distances.clone();
	}
	
	/**
	 * Constructeur pour permettre la creation de prototype.
	 * Privé pour ne pas permettre au contexte 
	 * d'instancier une version non fonctionnel de {@link TravellingSalesmanProblem}.
	 * 
	 * @throws Exception Si une erreur survient lors de l'initialisation des solveurs
	 */
	private TravellingSalesmanProblem() throws Exception {
		this.initialiseSolveur();
		this.villes = null;
		this.distances = null;
	}
	
	/**
	 * Retourne le nombre de villes du problème.
	 * 
	 * @return Le nombre de villes, ou 0 si les distances sont nulles
	 */
	public int getNbVille() {
		if (distances == null)
			return 0;
		return distances.length;
	}
	
	/**
	 * Initialise la liste des solveurs disponibles.
	 * Crée les instances des solveurs en utilisant le principe de "reflection".
	 * 
	 * @throws Exception Si une erreur survient lors de l'initialisation des solveurs
	 */
	private void initialiseSolveur() throws Exception {
		this.derniereSolBrute = null;
		this.solveurs = new ArrayList<>();
		
		// Initialisation de chaque solveur
		for (Class<? extends TSPSolveur> classObj : TravellingSalesmanProblem.solveursImplemente) {
			try {
				this.solveurs.add((TSPSolveur) classObj.getConstructors()[0].newInstance(this));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				throw e;
			}
		}
		
		// Choix du premier solveur par défaut
		this.solveur = this.solveurs.get(0);
	}
	
	/**
	 * Évalue le coût d'une solution donnée.
	 * 
	 * @param solution Un tableau représentant l'ordre des villes dans la solution
	 * @return Le coût total de la solution
	 */
	public double evalCoutSolution(int[] solution) {
		if (solution == null)
			return Double.MAX_VALUE;
		
		if (solution.length <= 1)
			return 0;
		
		float distanceTotale = 0;
		int prevVille = 0;
		for (int i = 1; i < solution.length ; i++) {
			distanceTotale += this.distances[solution[prevVille]][solution[i]];
			prevVille = i;
		}
		
		return distanceTotale + this.distances[solution[0]][solution[prevVille]];
	}
	
	/**
	 * Évalue le coût d'une solution donnée, représentée sous forme de liste.
	 * utile pour des Solveur qui represente leurs solutions sous forme de List
	 * 
	 * @param solution Liste représentant l'ordre des villes dans la solution
	 * @return Le coût total de la solution
	 */
	public double evalCoutSolution(List<Integer> solution) {
		// Conversion de la liste en tableau
		int[] solutionPrimitiveType = solution.stream().mapToInt(Integer::intValue).toArray();
		return evalCoutSolution(solutionPrimitiveType);
	}
	
	/**
	 * Retourne le noms des solveurs implémenté.
	 * 
	 * @return Liste des noms des solveurs implémentés
	 * @throws Exception Si une erreur survient lors de l'initialisation des solveurs
	 */
	public static ArrayList<String> getImplementedSolveurNames() throws Exception{
		TravellingSalesmanProblem prototype = new TravellingSalesmanProblem();
		ArrayList<String> noms = new ArrayList<>();
		for (TSPSolveur solveur : prototype.solveurs) {
			noms.add(solveur.getNom());
		}
		return noms;
	}
	
	/**
	 * Retourne les paramètres d'un solveur spécifié par son nom.
	 * 
	 * @param solveurName Nom du solveur
	 * @return Les paramètres du solveur
	 * @throws Exception si une erreur survient lors de l'initialisation du solveur
	 */
	public static ParametresHandler getPrametersFromSolveurName(String solveurName) throws Exception {
		TravellingSalesmanProblem prototype = new TravellingSalesmanProblem();
		prototype.setSolveur(solveurName);
		return prototype.getParametresSolveur();
	}
	
	/**
	 * Retourne la description d'un solveur spécifié par son nom.
	 * 
	 * @param solveurName Nom du solveur
	 * @return La description du solveur
	 * @throws Exception si une erreur survient lors de l'initialisation du solveur
	 */
	public static String getDescriptionFromSolveurName(String solveurName) throws Exception {
		TravellingSalesmanProblem prototype = new TravellingSalesmanProblem();
		prototype.setSolveur(solveurName);
		return prototype.getDescriptionSolveur();
	}
	
	/**
	 * Retourne le nom du solveur actuellement utilisé.
	 * 
	 * @return Le nom du solveur
	 */
	public String getNomSolveur() {
		return this.solveur.getNom();
	}

	/**
	 * Retourne la description du solveur actuellement utilisé.
	 * 
	 * @return La description du solveur
	 */
	public String getDescriptionSolveur() {
		return this.solveur.getDescription();
	}
	
	/**
	 * Retourne les paramètres du solveur si celui-ci est paramétrable.
	 * 
	 * @return Les paramètres du solveur ou null si non paramétrable
	 */
	public ParametresHandler getParametresSolveur() {
		if (!this.solveur.estParametrable()) {
			return null;
		}
		return this.solveur.parametres;
	}
	
	public boolean solveurEstParametrable() {
		return this.solveur.estParametrable();
	}
	
	/**
	 * Exécute le solveur et retourne la solution calculée.
	 * 
	 * @return Les villes dans l'ordre de la solution trouvée
	 */
	public Ville[] executeSolveur() {
		this.derniereSolBrute = this.solveur.calculSolutionSafe();
		this.solveurDerniereSolution = this.solveur;
		return this.getDerniereSolution();
	}
	
	/**
	 * Vérifie si l'exécution du solveur a réussi.
	 * 
	 * @return true si l'exécution a réussi, false sinon
	 */
	public boolean estExecutionReussit() {
		return this.solveurDerniereSolution.estExecutionReussit();
	}
	
	/**
	 * @return La solution sous forme d'un tableau de villes, ou null si l'exécution a échoué
	 */
	public Ville[] getDerniereSolution() {
		if (!this.solveurDerniereSolution.estExecutionReussit()) {
			return null;
		}
		
		Ville[] villeArray = new Ville[this.getNbVille()];
		for (int i = 0; i < this.getNbVille(); i++) {
			villeArray[i] = this.villes[this.derniereSolBrute[i]];		
		}
		return villeArray;
	}
	
	/**
	 * Modifie le solveur utilisé par le problème en spécifiant son nom.
	 * 
	 * @param solveurName Nom du solveur
	 * @throws IllegalArgumentException Si le nom du solveur n'est pas valide
	 */
	public void setSolveur(String solveurName) throws IllegalArgumentException {
		for (TSPSolveur solveur: this.solveurs) {
			if (solveur.getNom().equals(solveurName)) {
				this.solveur = solveur;
				return;
			}
		}
		throw new IllegalArgumentException("le solveur spécifié ne fait pas partie de la liste " + solveurName);
	}
	
	/**
	 * Exécute le solveur et retourne les résultats sous forme de chaîne de caractères.
	 * 
	 * @return Les résultats sous forme de chaîne de caractères
	 */
	public String executeSolveurVerbose() {
	    StringBuilder result = new StringBuilder();

	    result.append("---------------------------------TSP-------------------------------\n");
	    result.append("Problème du voyageur de commerce pour les villes :\n");

	    result.append(formatVilleArray(this.villes)).append("\n");

	    result.append("Algorithme utilisé : ").append(this.getNomSolveur()).append("\n");
	    if (this.solveur.estParametrable()) {
	        result.append("Paramètres utilisés : ").append(this.solveur.parametres).append("\n");
	    }
	    result.append("Début du calcul...\n");

	    this.executeSolveur();
	    
	    if (this.solveurDerniereSolution.estExecutionReussit()) {
		    result.append("Solution trouvée :\n").append(formatVilleArray(this.getDerniereSolution())).append("\n");
		    result.append("Solution trouvée en ").append(this.solveur.getTempsExecutionPrecedent() / 1000).append(" s\n\n");
	    } else {
	    	result.append("Échec de l'exécution");
	    }

	    return result.toString();
	}
	
	/**
	 * Formate un tableau de villes sous forme de chaîne de caractères.
	 * 
	 * @param villes Tableau de villes à formater
	 * @return La chaîne formatée représentant l'ordre des villes
	 */
	private String formatVilleArray(Ville[] villes) {
	    StringBuilder formatted = new StringBuilder();
	    for (int i = 0; i < villes.length; i++) {
	        formatted.append(villes[i].getName());
	        if (i < villes.length - 1) {
	            formatted.append(" -> ");
	        }
	    }
	    return formatted.toString();
	}

	/**
	 * Retourne la distance entre deux villes.
	 * 
	 * @param i index de la première ville
	 * @param j index de la deuxième ville
	 * @return La distance entre les deux villes
	 */
	public double distanceEntre(int i, int j) {
		return this.distances[i][j];
	}
	
	/**
	 * Retourne le coût de la dernière solution trouvée.
	 * 
	 * @return Le coût de la dernière solution ou -1 si l'exécution a échoué
	 */
	public double getCoutDerniereSolution() {
	    if (!this.solveurDerniereSolution.estExecutionReussit()) return -1;
	    return evalCoutSolution(this.derniereSolBrute);
	}

	/**
	 * Retourne le temps d'exécution du dernier calcul de solution en secondes.
	 * 
	 * @return Le temps d'exécution du dernier calcul ou -1 si l'exécution a échoué
	 */
	public double getTempsExecutionPrecedent() {
		if (!this.solveurDerniereSolution.estExecutionReussit()) return -1;
	    return this.solveur.getTempsExecutionPrecedent() / 1000.0;
	}

    /**
     * Présente les résultats sous forme de code HTML.
     * 
     * @return String.
     */
public String presentationResultatsHTML() {
    	
        StringBuilder presentation = new StringBuilder();
        presentation.append("<html><body style='font-family: Arial, sans-serif; line-height: 1.6;'>");

        // Titre
        presentation.append("<h2 style='color: #003366; font-weight: bold;'>Problème du voyageur de commerce - Resultats</h2>");

        // Info Algo
        presentation.append("<table style='width: 100%; border-collapse: collapse;'>")
                	.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Algorithme utilisé</th>")
                	.append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;'>")
                	.append(this.getNomSolveur())
                	.append("</td></tr>");

        // Nombre villes
        presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Nombre de villes</th>")
                	.append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;'>")
                	.append(this.getNbVille())
                	.append("</td></tr>");
        
        if (this.getNbVille() > 0) {
	        // Solution proposée
	        if (this.solveurDerniereSolution.estExecutionReussit()) {
		        presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Solution</th>")
		                	.append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;font-size:6;'>")
		                	.append(formatVilleArray(this.getDerniereSolution()))
		                	.append("</td></tr>");
		        // Cout
		        presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Cout solution (km)</th>")
		                	.append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;'>")
		                	.append(String.format("%.2f", this.getCoutDerniereSolution()))
		                	.append("</td></tr>");
	
		        // temps calcule
		        presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Temps d'execution</th>")
		                .append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;'>")
		                .append(String.format("%.2f", this.getTempsExecutionPrecedent())).append(" s")
		                .append("</td></tr>");
		        
		        // Affichage parametres
		        ParametresHandler parameters = this.getParametresSolveur();
		        if (parameters != null) {
		            presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #f2f2f2;'>Parametre utilisé</th>")
		                    .append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ffffff;'>");
	
		            ArrayList<String> paramNames = parameters.getNomsParametres();
	
		            for (int i = 0; i < paramNames.size(); i++) {
		                String   paramName  = paramNames.get(i);
		                Object   paramValue = parameters.getValeurParametre(paramName);
	
		                presentation.append("<strong>").append(paramName).append(":</strong> ")
		                        .append("<span style='color: #2d3e50;'>")
		                        .append(paramValue).append("</span><br>");
		            }
	
		            presentation.append("</td></tr>");
		        }
		        
	        } else {
		        presentation.append("<tr><th style='text-align: left; padding: 8px; background-color: #ff0000;'>Solution</th>")
	        	.append("<td style='padding: 8px; border: 1px solid #ddd; background-color: #ff0000;'>")
	        	.append("ECHEC LORS DE L'EXECUTION DE L'ALGORITHME")
	        	.append("</td></tr>");
	        }
        }



        presentation.append("</table>");
        presentation.append("</body></html>");

        return presentation.toString();
    }

}
