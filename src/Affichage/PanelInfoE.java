package Affichage;

import java.awt.*;
import javax.swing.*;
import Data.data;
import ecosysteme.*;
import SQL.Ville;

/**
 * PanelInfoE est une classe permettant d'afficher plusieurs tableaux
 * organisés par type d'information : chercheurs, MCFs, étudiants, et villes.
 * Ces informations consistuent l'écosystème et sont présentes dans data.
 */
public class PanelInfoE extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de la classe PanelInfoE. 
     * Initialise l'interface utilisateur avec les différents tableaux d'information.
     */
    public PanelInfoE() {
        // Mise en place du layout principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges autour du Panel

        // Ajout des sections pour chaque type de tableau
        add(createSection("Tableaux Titulaires", createTablePanel("Chercheurs", getContentC(),
                new String[]{"Nom", "Prénom", "Age", "Ville", "Num Bureau", "Disciplines", "Étudiants"})));

        add(createTablePanel("MCFs", getContentM(),
                new String[]{"Nom", "Prénom", "Age", "Ville", "Num Bureau", "Disciplines", "Étudiant"}));

        add(createSection("Tableau Étudiants", createTablePanel("Étudiants", getContentE(),
                new String[]{"Nom", "Prénom", "Age", "Ville", "Sujet de Thèse", "Année de Thèse", "Discipline", "Encadrant"})));

        add(createSection("Tableau Villes", createTablePanel("Villes", getContentV(),
                new String[]{"Nom", "Code Postal", "Population", "Surface", "Département"})));
    }

    /**
     * Crée un Panel contenant un tableau avec un titre.
     *
     * @param title  Titre du Panel.
     * @param data   Données à afficher dans le tableau.
     * @param columns Noms des colonnes du tableau.
     * @return Un Panel contenant un tableau.
     */
    private JPanel createTablePanel(String title, String[][] data, String[] columns) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crée une section comprenant un titre et un Panel de contenu.
     *
     * @param title   Titre de la section.
     * @param content Contenu de la section.
     * @return Un Panel représentant une section.
     */
    private JPanel createSection(String title, JPanel content) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        section.add(label);
        section.add(Box.createVerticalStrut(10));
        section.add(content);

        return section;
    }

    /**
     * Récupère les données pour le tableau des chercheurs.
     *
     * @return Un tableau 2D contenant les données des chercheurs.
     */
    private String[][] getContentC() {
        if (data.chercheurs != null && !data.chercheurs.isEmpty()) {
            String[][] content = new String[data.chercheurs.size()][7];
            for (int i = 0; i < data.chercheurs.size(); i++) {
                Chercheur chercheur = data.chercheurs.get(i);
                content[i] = chercheur != null
                        ? new String[]{chercheur.getNom(), chercheur.getPrenom(), chercheur.getAge(), chercheur.getVilleNom(),
                                chercheur.getNumBureau(), chercheur.getDisciplines(), chercheur.getEtudiantsString()}
                        : new String[]{"", "", "", "", "", "", ""};
            }
            return content;
        }
        return new String[][]{{"", "", "", "", "", "", ""}};
    }

    /**
     * Récupère les données pour le tableau des MCFs.
     *
     * @return Un tableau 2D contenant les données des MCFs.
     */
    private String[][] getContentM() {
        if (data.MCFs != null && !data.MCFs.isEmpty()) {
            String[][] content = new String[data.MCFs.size()][7];
            for (int i = 0; i < data.MCFs.size(); i++) {
                MCF mcf = data.MCFs.get(i);
                content[i] = mcf != null
                        ? new String[]{mcf.getNom(), mcf.getPrenom(), mcf.getAge(), mcf.getVilleNom(),
                                mcf.getNumBureau(), mcf.getDisciplines(), mcf.getEtudiantString()}
                        : new String[]{"", "", "", "", "", "", ""};
            }
            return content;
        }
        return new String[][]{{"", "", "", "", "", "", ""}};
    }

    /**
     * Récupère les données pour le tableau des étudiants.
     *
     * @return Un tableau 2D contenant les données des étudiants.
     */
    private String[][] getContentE() {
        if (data.etudiants != null && !data.etudiants.isEmpty()) {
            String[][] content = new String[data.etudiants.size()][8];
            for (int i = 0; i < data.etudiants.size(); i++) {
                Etudiant etudiant = data.etudiants.get(i);
                content[i] = etudiant != null
                        ? new String[]{etudiant.getNom(), etudiant.getPrenom(), etudiant.getAge(), etudiant.getVilleNom(),
                                etudiant.getSujetDeThese(),  etudiant.getAnneeDeThese(), etudiant.getDiscipline(), etudiant.getEncadrantString()}
                        : new String[]{"", "", "", "", "", "", "", ""};
            }
            return content;
        }
        return new String[][]{{"", "", "", "", "", "", "", ""}};
    }

    /**
     * Récupère les données pour le tableau des villes.
     *
     * @return Un tableau 2D contenant les données des villes.
     */
    private String[][] getContentV() {
        if (data.villes != null && !data.villes.isEmpty()) {
            String[][] content = new String[data.villes.size()][5];
            for (int i = 0; i < data.villes.size(); i++) {
                Ville ville = data.villes.get(i);
                content[i] = ville != null
                        ? new String[]{ville.getName(), ville.getDepartement(), ville.getCode(), ville.getPopulation(), ville.getSuperficie()}
                        : new String[]{"", "", "", "", ""};
            }
            return content;
        }
        return new String[][]{{"", "", "", "", ""}};
    }
}
