package Data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import ecosysteme.*;
import map.CarteFrance;
import problemeDuVoyageurDeCommerce.TravellingSalesmanProblem;
import SQL.*;

/**
 * Classe de gestion des données globales et des interactions avec la base de données, la carte, et d'autres composants.
 * Elle permet de stocker et de manipuler les informations liées aux villes, étudiants, MCFs, chercheurs,
 * ainsi que d'interagir avec la base de données et la carte de France.
 */
public abstract class data {
    
    /**
     * Liste statique contenant toutes les villes enregistrées dans l'écosystème.
     * Ces villes sont récupérées depuis la base de données ou définies manuellement.
     */
    public static List<Ville> villes = new ArrayList<>();
    
    /**
     * Liste statique contenant tous les étudiants dans l'écosystème.
     */
    public static List<Etudiant> etudiants = new ArrayList<>();
    
    /**
     * Liste statique contenant tous les MCF (Maîtres de Conférences) dans l'écosystème.
     */
    public static List<MCF> MCFs = new ArrayList<>();
    
    /**
     * Liste statique contenant tous les chercheurs dans l'écosystème.
     */
    public static List<Chercheur> chercheurs = new ArrayList<>();
    
    /**
     * Instance de la classe Java_to_SQL, utilisée pour gérer les connexions et les requêtes à la base de données.
     * Cette classe facilite l'interaction avec la base de données pour récupérer ou insérer des données.
     */
    public static Java_to_SQL SQL = new Java_to_SQL();
    
    /**
     * Instance du problème de voyageur de commerce (TSP).
     * Utilisé pour résoudre un problème d'optimisation de parcours.
     */
    public static TravellingSalesmanProblem tsp = null;
    
    /**
     * Instance de la carte de France (CarteFrance), utilisée pour afficher les villes et leurs informations géographiques.
     * Cette carte peut être mise à jour dynamiquement pour afficher les données de l'écosystème.
     */
    public static CarteFrance CF = null;
    
    /**
     * Liste statique contenant tous les étudiants dans l'écosystème.
     */
    public static BufferedImage imageCarte = null;
    
    /**
     * Méthode permettant de configurer les paramètres de la base de données (URL, utilisateur, mot de passe).
     * 
     * @param url L'URL de la base de données.
     * @param user Le nom d'utilisateur pour se connecter à la base de données.
     * @param password Le mot de passe pour l'utilisateur.
     */
    public static void SQL_Setter(String url, String user, String password) {
        SQL.Setter(url, user, password);
    }

    /**
     * Méthode permettant d'initialiser la liste des villes en récupérant les données depuis la base de données.
     * Les villes récupérées sont ajoutées à la liste statique 'villes'.
     */
    public static void Init_Ville() {
        villes.clear();
    	List<Ville> copie = SQL.getVille_from_SQL();
        if (copie != null) {
            villes.addAll(copie); // Ajout des villes récupérées à la liste
        }
    }

    /**
     * Méthode permettant de configurer l'instance de la carte de France (CarteFrance).
     * 
     * @param CF L'instance de la carte de France.
     */
    public static void setCarteFrance(CarteFrance CF) {
        data.CF = CF;
    }

    /**
     * Méthode permettant de mettre à jour la carte de France avec les villes des étudiants, MCFs et chercheurs.
     * Cette méthode ajoute toutes les villes des entités à la carte et la met à jour visuellement.
     */
    public static void updateVilleCarteEcosysteme() {
        ArrayList<Ville> touteLesVilles = new ArrayList<>();
        
        // Ajouter les villes des étudiants
        for (Etudiant etd : data.etudiants) {
            Ville ajout = etd.getVilleObject();
            if (ajout != null) touteLesVilles.add(ajout);
        }
        
        // Ajouter les villes des MCFs
        for (MCF mcf : data.MCFs) {
            touteLesVilles.add(mcf.getVilleObject());
        }
        
        // Ajouter les villes des chercheurs
        for (Chercheur chrch : data.chercheurs) {
            touteLesVilles.add(chrch.getVilleObject());
        }
        
        // Mettre à jour la carte en vidant les anciennes données et en ajoutant les nouvelles
        data.CF.clearCarte();
        data.CF.ajouterListeVille(touteLesVilles);
        data.CF.repaint();  // Rafraîchir l'affichage de la carte
    }

    /**
     * Méthode permettant de mettre à jour la carte de France avec une liste personnalisée de villes.
     * 
     * @param touteLesVilles La liste des villes à ajouter à la carte.
     */
    public static void updateVilleCarteSandBox(ArrayList<Ville> touteLesVilles) {
        data.CF.clearCarte(); // Vider la carte existante
        data.CF.ajouterListeVille(touteLesVilles); // Ajouter les nouvelles villes
        data.CF.repaint();  // Rafraîchir l'affichage
    }

    /**
     * Méthode permettant de récupérer une ville en fonction de son nom et département.
     * 
     * @param input Le nom et le département de la ville sous la forme "Nom (Département)".
     * @return La ville correspondante, ou null si la ville n'existe pas.
     */
    public static Ville getVille(String input) {
        for (Ville ville : data.villes) {
            if (ville.getNomDepartement().equals(input)) {
                return ville;
            }
        }
        return null;  // Retourner null si aucune ville ne correspond
    }
}
