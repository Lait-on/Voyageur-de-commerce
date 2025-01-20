package Affichage;

import javax.swing.*;

/**
 * Classe représentant la fenêtre principale de l'application "Voyageur du commerce".
 * Elle initialise une fenêtre JFrame avec une taille par défaut et un Panel d'accueil.
 */
public class Frame {

    /**
     * Constructeur de la classe Frame.
     * Configure la fenêtre principale de l'application avec les paramètres d'affichage requis
     * et un Panel d'accueil.
     */
    public Frame() {
        // Création de la fenêtre principale
        JFrame frame = new JFrame("Voyageur du commerce");

        // Définit l'action par défaut à effectuer lors de la fermeture de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Définit la taille initiale de la fenêtre
        frame.setSize(1000, 600);

        // Étend la fenêtre à la taille maximale de l'écran
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Active ou désactive la barre de titre et les bordures de la fenêtre
        frame.setUndecorated(false);

        // Définit le contenu de la fenêtre en affichant le Panel d'accueil
        frame.setContentPane(new PanelAccueil(frame));

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }
}
