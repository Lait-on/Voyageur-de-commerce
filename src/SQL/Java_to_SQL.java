package SQL;

import java.sql.*;
import java.util.*;

/**
 * Cette classe fournit des méthodes pour interagir avec une base de données SQL.
 * Elle permet notamment de récupérer des informations sur les villes, calculer des distances entre elles
 * et obtenir leurs coordonnées.
 */
public class Java_to_SQL {

    private String url;
    private String user;
    private String password;

    /**
     * Constructeur par défaut. Les paramètres de connexion doivent être définis séparément.
     */
    public Java_to_SQL() {
        this.url = null;
        this.user = null;
        this.password = null;
    }

    /**
     * Définit les paramètres de connexion à la base de données.
     *
     * @param url      l'URL de la base de données
     * @param user     le nom d'utilisateur pour la connexion
     * @param password le mot de passe pour la connexion
     */
    public void Setter(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Récupère une ville en fonction de son nom et du code de département.
     *
     * @param NomVille        le nom de la ville recherchée
     * @param CodeDepartement le code du département où se trouve la ville
     * @return un objet Ville correspondant, ou null si aucun résultat n'est trouvé
     */
    public Ville getVille(String NomVille, String CodeDepartement) {
        String Query = "SELECT v.ville_nom, v.ville_code_postal, v.ville_population_2012, v.ville_surface, v.ville_departement " +
                       "FROM villes_france_free AS v " +
                       "WHERE v.ville_departement = '" + CodeDepartement + "' AND v.ville_nom_reel = '" + NomVille + "';";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(Query)) {

            if (resultSet.next()) {
                return new Ville(
                    resultSet.getString("ville_nom"),
                    resultSet.getString("ville_code_postal"),
                    resultSet.getInt("ville_population_2012"),
                    resultSet.getInt("ville_surface"),
                    resultSet.getString("ville_departement")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête (fonction: getVille)");
        }
        return null;
    }

    /**
     * Calcule une matrice des distances entre les villes d'un trajet donné.
     *
     * @param trajet un tableau des villes
     * @return une matrice des distances (en kilomètres) entre chaque paire de villes
     */
    public double[][] getDistance(Ville[] trajet) {
        double[][] matrice = new double[trajet.length][trajet.length];
        String Query_prepared =
                "SELECT 2 * 6371 * ASIN(SQRT(" +
                "POWER(SIN((RADIANS(v1.ville_latitude_deg) - RADIANS(v2.ville_latitude_deg))/2), 2) +" +
                "COS(RADIANS(v1.ville_latitude_deg)) * COS(RADIANS(v2.ville_latitude_deg)) * " +
                "POWER(SIN((RADIANS(v1.ville_longitude_deg) - RADIANS(v2.ville_longitude_deg))/2), 2))) AS distance_km " +
                "FROM villes_france_free AS v1, villes_france_free AS v2 " +
                "WHERE v1.ville_nom = ? AND v1.ville_departement = ? AND v2.ville_nom = ? AND v2.ville_departement = ?;";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(Query_prepared)) {

            for (int i = 0; i < trajet.length; i++) {
                for (int j = 0; j < trajet.length; j++) {
                    if (i == j) {
                        matrice[i][j] = 0;
                    } else {
                        preparedStatement.setString(1, trajet[i].getName().toUpperCase());
                        preparedStatement.setString(2, trajet[i].getDepartement());
                        preparedStatement.setString(3, trajet[j].getName().toUpperCase());
                        preparedStatement.setString(4, trajet[j].getDepartement());
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                matrice[i][j] = resultSet.getDouble("distance_km");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête préparée (fonction: getDistance)");
        }
        return matrice;
    }

    /**
     * Convertit un identifiant de département en son nom correspondant.
     *
     * @param id l'identifiant du département
     * @return le nom du département, ou null si aucun résultat n'est trouvé
     */
    public String IDDepartement_to_NomDepartement(String id) {
        String Query = "SELECT d.departement_nom_uppercase FROM departement AS d WHERE d.departement_code = '" + id + "';";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(Query)) {

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête (fonction: IDDepartement_to_NomDepartement)");
        }
        return null;
    }

    /**
     * Récupère la liste complète des villes dans la base de données.
     *
     * @return une liste d'objets Ville, triée par ordre alphabétique
     */
    public List<Ville> getVille_from_SQL() {
        String Query = "SELECT ville_nom, ville_code_postal, ville_population_2012, ville_surface, ville_departement " +
                       "FROM villes_france_free ORDER BY ville_nom;";
        List<Ville> res = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(Query)) {

            while (resultSet.next()) {
                res.add(new Ville(
                    resultSet.getString("ville_nom"),
                    resultSet.getString("ville_code_postal"),
                    resultSet.getInt("ville_population_2012"),
                    resultSet.getInt("ville_surface"),
                    resultSet.getString("ville_departement")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête (fonction: getVille_from_SQL)");
        }
        return res;
    }

    /**
     * Récupère les coordonnées (latitude et longitude) d'une ville donnée.
     *
     * @param ville l'objet Ville pour lequel récupérer les coordonnées
     * @return un tableau contenant la latitude et la longitude, ou null si aucun résultat n'est trouvé
     */
    public double[] getCoordonnée(Ville ville) {
        String Query = "SELECT v.ville_latitude_deg, v.ville_longitude_deg " +
                       "FROM villes_france_free AS v " +
                       "WHERE v.ville_code_postal = '" + ville.getCode() + "';";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(Query)) {

            if (resultSet.next()) {
                return new double[] {
                    resultSet.getDouble("ville_latitude_deg"),
                    resultSet.getDouble("ville_longitude_deg")
                };
            } else {
                System.out.println("Aucune coordonnée trouvée pour le code postal : " + ville.getCode());
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête (fonction: getCoordonnée)");
        }
        return null;
    }

    /**
     * Récupère les coordonnées pour une liste de villes.
     *
     * @param villes une liste d'objets Ville
     * @return un tableau contenant les coordonnées (latitude et longitude) de chaque ville
     */
    public double[][] getCoordinatesForVilles(ArrayList<Ville> villes) {
        if (villes.isEmpty()) {
            return null;
        }

        double[][] coordinates = new double[villes.size()][2];

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            for (int i = 0; i < villes.size(); i++) {
                Ville ville = villes.get(i);
                String query = "SELECT v.ville_latitude_deg, v.ville_longitude_deg " +
                               "FROM villes_france_free AS v " +
                               "WHERE v.ville_code_postal = '" + ville.getCode() + "';";

                try (ResultSet resultSet = statement.executeQuery(query)) {
                    if (resultSet.next()) {
                        coordinates[i][0] = resultSet.getDouble("ville_latitude_deg");
                        coordinates[i][1] = resultSet.getDouble("ville_longitude_deg");
                    } else {
                        System.out.println("Aucune coordonnée trouvée pour le code postal : " + ville.getCode());
                        coordinates[i][0] = Double.NaN;
                        coordinates[i][1] = Double.NaN;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la connexion ou l'exécution de la requête (fonction: getCoordinatesForVilles)");
        }

        return coordinates;
    }
}
