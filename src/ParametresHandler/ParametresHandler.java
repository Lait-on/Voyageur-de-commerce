package ParametresHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite permettant de gérer dynamiquement des paramètres via réflexion.
 * Les classes héritant de `ParametersHandler` doivent définir des méthodes "setter"
 * et "getter" pour chaque paramètre. Cela permet d'identifier dynamiquement les noms
 * et les types des paramètres, et d'accéder à ces derniers à partir d'autres parties
 * du code.
 *
 */
public abstract class ParametresHandler {

    /**
     * Liste des paramètres détectées dans la classe héritante.
     * Chaque entrée correspond à un paramètre manipulé par un setter.
     */
    private List<RepresentationParametres> parametresRepr = new ArrayList<>();

    /**
     * Initialise la liste des paramètres en détectant les méthodes "setter"
     * dans la classe héritante.
     */
    public ParametresHandler() {
    	initialiseParametresRepr();
    }

    /**
     * Initialise les entrées de paramètres en détectant dynamiquement les
     * méthodes "setter" dans la classe héritante. Les setters doivent suivre
     * la convention de nommage `setNomParamètre`.
     * 
     * @throws RuntimeException si un setter manipule un type primitif.
     */
    private void initialiseParametresRepr() {
        Method[] methodes = this.getClass().getDeclaredMethods();

        for (Method methode : methodes) {
            if (methode.getName().startsWith("set")) {
                String nomParametre = methode.getName().substring(3); // Récupère le nom du paramètre

                if (nomParametre.length() > 0) {
                    // Récupère le type du paramètre manipulé par le setter
                    Class<?> typeParam = methode.getParameterTypes()[0];
                    if (typeParam.isPrimitive()) {
                        throw new RuntimeException("Les types primitifs ne sont pas autorisés : " + nomParametre);
                    }

                    this.parametresRepr.add(new RepresentationParametres(nomParametre, typeParam));
                }
            }
        }
    }

    /**
     * Retourne la liste des noms des paramètres détectés.
     * 
     * @return Une liste contenant les noms des paramètres.
     */
    public ArrayList<String> getNomsParametres() {
        ArrayList<String> names = new ArrayList<>();
        for (RepresentationParametres param : this.parametresRepr) {
            names.add(param.getName());
        }
        return names;
    }

    /**
     * Retourne la liste des types des paramètres détectés.
     * 
     * @return Une liste contenant les types des paramètres.
     */
    public ArrayList<Class<?>> getTypesParametres() {
        ArrayList<Class<?>> types = new ArrayList<>();
        for (RepresentationParametres entry : this.parametresRepr) {
            types.add(entry.getType());
        }
        return types;
    }

    /**
     * Définit la valeur d'un paramètre à l'aide de son nom.
     * 
     * @param nomParametre  Le nom du paramètre.
     * @param valeur La valeur à attribuer au paramètre.
     * @throws IllegalArgumentException si le paramètre est introuvable ou si le type
     *                                  de la valeur ne correspond pas au type attendu.
     */
    public void setValeurParametre(String nomParametre, Object valeur) throws IllegalArgumentException {
        for (RepresentationParametres param : this.parametresRepr) {
            if (param.getName().equalsIgnoreCase(nomParametre)) {

                if (param.getType().isInstance(valeur) || param.getType() == valeur.getClass()) {
                    try {
                        Method setter = this.getClass().getDeclaredMethod("set" + nomParametre, param.getType());
                        setter.setAccessible(true);
                        setter.invoke(this, valeur);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Échec de la définition de la valeur du paramètre : " + nomParametre + ". " + e.getMessage(), e);
                    }
                } else {
                    throw new IllegalArgumentException("Le type de la valeur ne correspond pas au type attendu pour : " + nomParametre);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Paramètre introuvable : " + nomParametre);
    }

    /**
     * Récupère la valeur d'un paramètre à l'aide de son nom.
     * 
     * @param nom Le nom du paramètre.
     * @return La valeur actuelle du paramètre.
     * @throws IllegalArgumentException si le paramètre est introuvable ou si une erreur
     *                                  survient lors de l'invocation de la méthode "getter".
     */
    public Object getValeurParametre(String nom) throws IllegalArgumentException {
        for (RepresentationParametres param : this.parametresRepr) {
            if (param.getName().equalsIgnoreCase(nom)) {
                try {
                    Method getter = this.getClass().getDeclaredMethod("get" + nom);
                    getter.setAccessible(true);
                    return getter.invoke(this);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Échec de la récupération de la valeur du paramètre : " + nom, e);
                }
            }
        }
        throw new IllegalArgumentException("Paramètre introuvable : " + nom);
    }

    /**
     * Représente l'objet sous forme de chaîne contenant les noms et les valeurs
     * des paramètres détectés.
     * 
     * @return Une chaîne de caractères représentant les paramètres et leurs valeurs.
     */
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (RepresentationParametres entry : parametresRepr) {
            out.append(entry.getName()).append("=").append(this.getValeurParametre(entry.getName())).append("   ");
        }
        return out.toString();
    }

    /**
     * Classe interne représentant une entrée de paramètre avec son nom et son type.
     */
    private static class RepresentationParametres {
        private String nom;
        private Class<?> type;

        /**
         * Constructeur pour une entrée de paramètre.
         * 
         * @param nom Le nom du paramètre.
         * @param type Le type du paramètre.
         */
        public RepresentationParametres(String nom, Class<?> type) {
            this.nom = nom;
            this.type = type;
        }

        /**
         * Retourne le nom du paramètre.
         * 
         * @return Le nom du paramètre.
         */
        public String getName() {
            return nom;
        }

        /**
         * Retourne le type du paramètre.
         * 
         * @return Le type du paramètre.
         */
        public Class<?> getType() {
            return type;
        }
    }
}
