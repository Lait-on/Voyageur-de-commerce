package SQL;

import java.util.List;

/**
 * Enum représentant les régions de France métropolitaine.
 * Chaque région est définie par son nom, son chef-lieu, sa liste de départements et sa superficie.
 */
public enum Region {
    AUVERGNE_RHONE_ALPES("Auvergne-Rhône-Alpes", "Lyon", 
        List.of("AIN", "ALLIER", "ARDÈCHE", "CANTAL", "DRÔME", "ISÈRE", "LOIRE", "HAUTE-LOIRE", "PUY-DE-DÔME", "RHÔNE", "SAVOIE", "HAUTE-SAVOIE"), 
        69711),
    
    BOURGOGNE_FRANCHE_COMTE("Bourgogne-Franche-Comté", "Dijon", 
        List.of("CÔTE-D'OR", "DOUBS", "JURA", "NIÈVRE", "HAUTE-SAÔNE", "SAÔNE-ET-LOIRE", "YONNE", "TERRITOIRE DE BELFORT"), 
        47784),
    
    BRETAGNE("Bretagne", "Rennes", 
        List.of("CÔTES-D'ARMOR", "FINISTÈRE", "ILLE-ET-VILAINE", "MORBIHAN"), 
        27208),
    
    CENTRE_VAL_DE_LOIRE("Centre-Val de Loire", "Orléans", 
        List.of("CHER", "EURE-ET-LOIR", "INDRE", "INDRE-ET-LOIRE", "LOIR-ET-CHER", "LOIRET"), 
        39151),
    
    CORSE("Corse", "Ajaccio", 
        List.of("CORSE-DU-SUD", "HAUTE-CORSE"), 
        8680),
    
    GRAND_EST("Grand Est", "Strasbourg", 
        List.of("ARDENNES", "AUBE", "MARNE", "HAUTE-MARNE", "MEURTHE-ET-MOSELLE", "MEUSE", "MOSELLE", "BAS-RHIN", "HAUT-RHIN", "VOSGES"), 
        57441),
    
    HAUTS_DE_FRANCE("Hauts-de-France", "Lille", 
        List.of("AISNE", "NORD", "OISE", "PAS-DE-CALAIS", "SOMME"), 
        31806),
    
    ILE_DE_FRANCE("Île-de-France", "Paris", 
        List.of("PARIS", "SEINE-ET-MARNE", "YVELINES", "ESSONNE", "HAUTS-DE-SEINE", "SEINE-SAINT-DENIS", "VAL-DE-MARNE", "VAL-D'OISE"), 
        12012),
    
    NORMANDIE("Normandie", "Rouen", 
        List.of("CALVADOS", "EURE", "MANCHE", "ORNE", "SEINE-MARITIME"), 
        29907),
    
    NOUVELLE_AQUITAINE("Nouvelle-Aquitaine", "Bordeaux", 
        List.of("CHARENTE", "CHARENTE-MARITIME", "CORRÈZE", "CREUSE", "DORDOGNE", "GIRONDE", "LANDES", "LOT-ET-GARONNE", "PYRÉNÉES-ATLANTIQUES", "DEUX-SÈVRES", "VIENNE", "HAUTE-VIENNE"), 
        84036),
    
    OCCITANIE("Occitanie", "Toulouse", 
        List.of("ARIÈGE", "AUDE", "AVEYRON", "GARD", "HAUTE-GARONNE", "GERS", "HÉRAULT", "LOT", "LOZÈRE", "HAUTES-PYRÉNÉES", "PYRÉNÉES-ORIENTALES", "TARN", "TARN-ET-GARONNE"), 
        72724),
    
    PAYS_DE_LA_LOIRE("Pays de la Loire", "Nantes", 
        List.of("LOIRE-ATLANTIQUE", "MAINE-ET-LOIRE", "MAYENNE", "SARTHE", "VENDÉE"), 
        32082),
    
    PROVENCE_ALPES_COTE_D_AZUR("Provence-Alpes-Côte d'Azur", "Marseille", 
        List.of("ALPES-DE-HAUTE-PROVENCE", "HAUTES-ALPES", "ALPES-MARITIMES", "BOUCHES-DU-RHÔNE", "VAR", "VAUCLUSE"), 
        31400);

    private final String nom;
    private final String chefLieu;
    private final List<String> departements;
    private final int superficie;

    /**
     * Constructeur de l'enum Region.
     * 
     * @param nom          Le nom de la région
     * @param chefLieu     Le chef-lieu de la région
     * @param departements Liste des départements de la région
     * @param superficie   Superficie de la région en kilomètres carrés
     */
    Region(String nom, String chefLieu, List<String> departements, int superficie) {
        this.nom = nom;
        this.chefLieu = chefLieu;
        this.departements = departements;
        this.superficie = superficie;
    }

    /**
     * Retourne une représentation textuelle de la région.
     * 
     * @return Une chaîne de caractères décrivant la région
     */
    @Override
    public String toString() {
        return "Region [nom=" + nom + ", chefLieu=" + chefLieu + ", departements=" + departements + ", superficie=" + superficie + "]";
    }

    /**
     * Trouve la région correspondant à une ville donnée.
     * 
     * @param v   L'objet Ville pour lequel déterminer la région
     * @param SQL Une instance de la classe Java_to_SQL pour interroger la base de données
     * @return Le nom de la région contenant la ville, ou null si non trouvée
     */
    public static String ville_to_region(Ville v, Java_to_SQL SQL) {
        String departement = SQL.IDDepartement_to_NomDepartement(v.getDepartement());
        for (Region region : Region.values()) {
            if (region.departements.contains(departement)) {
                return region.nom;
            }
        }
        return null;
    }
}
