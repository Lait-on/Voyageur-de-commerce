package Affichage;

import java.awt.BorderLayout;
import javax.swing.*;

import Data.data;

/**
 * PanelMenu est une classe qui représente la barre de menus.
 * Cette classe organise différents menus (Ecosystème, Algorithmes, Bac à Sable, et Fichier)
 * et gère leurs interactions pour afficher des panels spécifiques dans le panel de droite.
 */
public class PanelMenu extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de la classe PanelMenu.
     * 
     * @param right Le Panel de droite (PanelRight) où les différents panels seront affichés en fonction du menu sélectionné.
     */
    public PanelMenu(JPanel right) {
        // Création de la barre de menus
        JMenuBar menuBar = new JMenuBar();

        // Menu "Ecosystème"
        JMenu menuEcosysteme = new JMenu("Ecosystème");

        JMenuItem modification = new JMenuItem("Modifier écosystème");
        JMenuItem infoE = new JMenuItem("Voir informations écosystème");

        menuEcosysteme.add(modification);
        menuEcosysteme.add(infoE);

        // Action : Modifier l'écosystème
        modification.addActionListener(e -> {
            right.removeAll(); // Supprime les composants existants du panel de droite
            right.add(new PanelModifEcosysteme()); // Ajoute le panel de modification de l'écosystème
            data.updateVilleCarteEcosysteme(); // Met à jour les données liées aux villes
            right.revalidate(); // Revalide le Panel pour refléter les changements
            right.repaint(); // Redessine le Panel
        });

        // Action : Voir les informations sur l'écosystème
        infoE.addActionListener(e -> {
            right.removeAll();
            right.add(new PanelInfoE()); // Ajoute le panel des informations
            data.updateVilleCarteEcosysteme();
            right.revalidate();
            right.repaint();
        });

        menuBar.add(menuEcosysteme); // Ajout du menu "Ecosystème" à la barre de menus

        // Menu "Algorithmes"
        JMenu menuAlgorithmes = new JMenu("Algorithmes");
        JMenuItem testAlgo = new JMenuItem("Tester les algorithmes");
        menuAlgorithmes.add(testAlgo);

        // Action : Tester les algorithmes
        testAlgo.addActionListener(e -> {
            right.removeAll();
            try {
                right.add(new PanelTestAlgo()); // Ajoute le panel de test des algorithmes
                data.updateVilleCarteEcosysteme();
            } catch (Exception e1) {
                e1.printStackTrace(); // Gère les exceptions potentielles
            }
            right.revalidate();
            right.repaint();
        });

        menuBar.add(menuAlgorithmes);

        // Menu "Bac à Sable"
        JMenu menuBAS = new JMenu("Bac à Sable");
        JMenuItem basItem = new JMenuItem("Bac à Sable");
        menuBAS.add(basItem);

        // Action : Accéder au Bac à Sable
        basItem.addActionListener(e -> {
            right.removeAll();
            try {
                right.add(new PanelBAS()); // Ajoute le panel du Bac à Sable
                System.out.println("Bac à Sable activé.");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            right.revalidate();
            right.repaint();
        });

        menuBar.add(menuBAS);

        // Menu "Fichier"
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem nouveauFichier = new JMenuItem("Nouveau Fichier");
        JMenuItem sauvegarde = new JMenuItem("Sauvegarde l'écosystème");

        menuFichier.add(nouveauFichier);
        menuFichier.add(sauvegarde);

        // Action : Créer un nouveau fichier
        nouveauFichier.addActionListener(e -> {
            right.removeAll();
            right.add(new PanelFichier()); // Ajoute le panel de création de fichier
            data.updateVilleCarteEcosysteme();
            right.revalidate();
            right.repaint();
        });

        // Action : Sauvegarder l'écosystème
        sauvegarde.addActionListener(e -> {
            right.removeAll();
            right.add(new PanelSauvegarde()); // Ajoute le panel de sauvegarde
            data.updateVilleCarteEcosysteme();
            right.revalidate();
            right.repaint();
        });

        menuBar.add(menuFichier);

        // Ajout de la barre de menus au JPanel (PanelMenu)
        setLayout(new BorderLayout());
        add(menuBar, BorderLayout.NORTH);
    }
}
