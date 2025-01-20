package Affichage;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import Data.data;
import map.CarteFrance;

/**
 * PannelAccueil est le Panel principal d'accueil qui affiche la carte de France et l'interface que l'utilisateur va utiliser.
 * Il inclut également une barre de menu en haut.
 */
public class PanelAccueil extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de PannelAccueil. Configure l'interface graphique, y compris la carte et les Panelx de droite.
     * @param mainframe Le JFrame principal de l'application
     */
    public PanelAccueil(JFrame mainframe) {
        setLayout(new BorderLayout());

        // Panel de droite qui contient le logo
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new PanelLogo(), BorderLayout.CENTER);

        // Barre de menu en haut
        JPanel topPanel = new PanelMenu(rightPanel);
        add(topPanel, BorderLayout.NORTH);

        // Création de la carte de France
        data.setCarteFrance(new CarteFrance());

        // JSplitPane pour diviser la fenêtre en deux sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, data.CF, rightPanel);
        splitPane.setDividerLocation(0.4);  // Taille initiale de la partie gauche (40% pour la carte)
        splitPane.setEnabled(false);  // Désactive l'interaction de l'utilisateur avec la division
        splitPane.setDividerSize(0);  // Masque la barre de division

        // Écouteur pour ajuster la taille de la division si la fenêtre est redimensionnée
        splitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                splitPane.setDividerLocation(0.4);  // Maintient le ratio 40% / 60%
            }
        });

        // Ajout du JSplitPane dans la partie centrale du Panel
        add(splitPane, BorderLayout.CENTER);

        // Mise à jour des informations de la ville sur la carte
        data.updateVilleCarteEcosysteme();

        // Demande à l'utilisateur d'initialiser de la base de données
        SwingUtilities.invokeLater(() -> new InitSQL());
    }
}
