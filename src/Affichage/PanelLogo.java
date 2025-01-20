package Affichage;

import javax.swing.*;
import java.awt.*;

/**
 * PanelLogo est une classe qui permet d'afficher une image d'accueil lorsque l'utilisateur ouvre l'application.
 */
public class PanelLogo extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image image; // Stocke l'image chargée pour l'affichage

    /**
     * Constructeur par défaut.
     * Charge l'image à partir d'un chemin prédéfini et vérifie si le chargement a réussi.
     */
    public PanelLogo() {
        // Chargement de l'image à partir d'un fichier
        ImageIcon icon = new ImageIcon("assets/Logo.JPG");
        
        // Vérifie si l'image a été chargée correctement
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            image = icon.getImage();
        } else {
            System.out.println("Erreur: l'image n'a pas pu être chargée.");
        }
    }

    /**
     * Redéfinit la méthode paintComponent pour dessiner l'image sur le Panel.
     * 
     * @param g L'objet Graphics utilisé pour dessiner sur le Panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vérifie si l'image a été chargée avant de tenter de l'afficher
        if (image != null) {
            // Dessine l'image, adaptée à la taille du Panel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.out.println("Erreur d'affichage : l'image est introuvable.");
        }
    }
}
