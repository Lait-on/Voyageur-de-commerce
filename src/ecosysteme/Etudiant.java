package ecosysteme;

import SQL.Ville;

/**
 * Classe représentant un étudiant, qui est une personne avec un sujet de thèse,
 * une discipline, une année de thèse et un encadrant (Titulaire).
 */
public class Etudiant extends Personne {
    private static final long serialVersionUID = 1L;

    /** Sujet de la thèse de l'étudiant. */
    private String sujetDeThese;
    
    /** Discipline de l'étudiant. */
    private Discipline discipline;
    
    /** Année de la thèse de l'étudiant. */
    private int anneeDeThese;
    
    /** L'encadrant (Titulaire) de l'étudiant. */
    private Titulaire encadrant;

    /**
     * Constructeur par défaut, initialisant les attributs à des valeurs par défaut.
     */
    public Etudiant() {
        this.sujetDeThese = "";
        this.discipline = null;
        this.anneeDeThese = 0;
        this.encadrant = null;
    }

    /**
     * Constructeur initialisant un étudiant avec des valeurs spécifiques.
     * @param nom Le nom de l'étudiant.
     * @param prenom Le prénom de l'étudiant.
     * @param age L'âge de l'étudiant.
     * @param ville La ville de résidence de l'étudiant.
     * @param sujetDeThese Le sujet de la thèse de l'étudiant.
     * @param discipline La discipline de l'étudiant.
     * @param anneeDeThese L'année de la thèse de l'étudiant.
     * @param encadrant L'encadrant (Titulaire) de l'étudiant.
     */
    public Etudiant(String nom, String prenom, int age, Ville ville, 
                    String sujetDeThese, Discipline discipline, int anneeDeThese, Titulaire encadrant) {
        super(nom, prenom, ville, age);
        this.sujetDeThese = sujetDeThese;
        this.discipline = discipline;
        this.anneeDeThese = anneeDeThese;
        this.encadrant = encadrant;
        if (encadrant != null) {
            encadrant.ajouterEtudiant(this);
        }
    }

    /**
     * Retourne une chaîne représentant l'étudiant, incluant son sujet de thèse, sa discipline, 
     * son année de thèse et son encadrant.
     * @return La chaîne décrivant l'étudiant.
     */
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1) + ", Etudiant [sujetDeThese=" + sujetDeThese + ", discipline=" + discipline +
               ", anneeDeThese=" + anneeDeThese + ", encadrant=" + encadrant + "]";
    }

    /**
     * @return Le sujet de la thèse de l'étudiant.
     */
    public String getSujetDeThese() {
        return sujetDeThese;
    }

    /**
     * @return La discipline de l'étudiant sous forme de chaîne.
     */
    public String getDiscipline() {
        return discipline != null ? discipline.toString() : "";  // Retourne une chaîne vide si discipline est nulle
    }

    /**
     * @return L'année de la thèse de l'étudiant sous forme de chaîne.
     */
    public String getAnneeDeThese() {
        return String.valueOf(anneeDeThese);
    }

    /**
     * @return Le nom et prénom de l'encadrant sous forme de chaîne, ou une chaîne vide si l'encadrant est nul.
     */
    public String getEncadrantString() {
        return encadrant != null ? encadrant.getPrenom() + " " + encadrant.getNom() : "";  
    }
    
    /**
     * @return L'encadrant (Titulaire) de l'étudiant.
     */
    public Titulaire getTitulaire() {
        return this.encadrant;
    }
    
    /**
     * Modifie l'encadrant de l'étudiant.
     * @param titulaire Le nouveau titulaire (encadrant).
     */
    public void change_titulaire(Titulaire titulaire) {
        this.encadrant = titulaire;
    }
    
    /**
     * Supprime l'encadrant de l'étudiant (met à null).
     */
    public void suppr_titulaire() {
        this.encadrant = null;
    }

    /**
     * Modifie les informations de l'étudiant, y compris son sujet de thèse, discipline, 
     * année de thèse et encadrant.
     * @param nom Le nouveau nom de l'étudiant.
     * @param prenom Le nouveau prénom de l'étudiant.
     * @param age Le nouvel âge de l'étudiant.
     * @param ville La nouvelle ville de résidence de l'étudiant.
     * @param sujetDeThese Le nouveau sujet de thèse de l'étudiant.
     * @param discipline La nouvelle discipline de l'étudiant.
     * @param anneeDeThese La nouvelle année de thèse de l'étudiant.
     * @param encadrant Le nouvel encadrant de l'étudiant.
     */
    public void modif(String nom, String prenom, int age, Ville ville, String sujetDeThese, Discipline discipline, int anneeDeThese, Titulaire encadrant) {
        super.modif(nom, prenom, ville, age);
        this.sujetDeThese = sujetDeThese;
        this.discipline = discipline;
        this.anneeDeThese = anneeDeThese;
        this.encadrant = encadrant;
        if (encadrant != null) {
            encadrant.ajouterEtudiant(this);
        }
    }
}
