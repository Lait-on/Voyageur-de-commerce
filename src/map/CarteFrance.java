package map;

import javax.swing.*;
import Data.data;
import SQL.Ville;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

/**
 * Représente une carte de la France avec des villes et des chemins affichés graphiquement.
 * Cette classe permet de gérer l'affichage et la manipulation de points (villes) et de chemins sur une image de carte.
 */
public class CarteFrance extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Image représentant la carte de la France.
     */
    private BufferedImage imageCarteFrance;


    /**
     * Liste des points représentant les villes sur la carte.
     */
    private ArrayList<VillePoint> villesPoints;

    /**
     * Liste des chemins reliant les villes sur la carte.
     */
    private ArrayList<Chemin> chemins;

    /**
     * Constructeur de la classe CarteFrance.
     * Initialise la carte, les villes et les chemins, et configure l'affichage dynamique.
     */
    public CarteFrance() {
        setBackground(Color.WHITE);
        try {
            this.imageCarteFrance = ImageIO.read(new File("assets/France-Country-Outline.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.villesPoints = new ArrayList<>();
        this.chemins = new ArrayList<>();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension parentSize = getParent().getSize();
                setPreferredSize(parentSize);
                setSize(parentSize);
                revalidate();
                repaint();
            }
        });
    }

    /**
     * dessine la carte et ses éléments graphiques (villes et chemins).
     *
     * @param g Contexte graphique pour le dessin.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double maxWidthHeight = Math.min(getHeight(), getWidth());
        double imageMinimumScale = maxWidthHeight / imageCarteFrance.getHeight();

        int scaledWidth = (int) (imageCarteFrance.getWidth() * imageMinimumScale);
        int scaledHeight = (int) (imageCarteFrance.getHeight() * imageMinimumScale);

        int centerOffsetX = (getWidth() - scaledWidth) / 2;
        int centerOffsetY = (getHeight() - scaledHeight) / 2;

        int imagePosX = centerOffsetX;
        int imagePosY = centerOffsetY;

        // Dessine la carte de France redimensionnée
        g2d.drawImage(imageCarteFrance, imagePosX, imagePosY, scaledWidth, scaledHeight, this);

        // Dessine les chemins entre les villes
        for (Chemin chemin : chemins) {
            if (chemin.estVisible() && !chemin.getVillePoints().isEmpty()) {
                ArrayList<VillePoint> villesDuChemin = chemin.getVillePoints();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setStroke(new BasicStroke(4f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

                int premierePositionX = (int) (villesDuChemin.get(0).getPositionSurCarteX() * imageMinimumScale + imagePosX);
                int premierePositionY = (int) (villesDuChemin.get(0).getPositionSurCarteY() * imageMinimumScale + imagePosY);
                int precedentePosX = premierePositionX;
                int precedentePosY = premierePositionY;

                for (VillePoint vp : villesDuChemin.subList(1, villesDuChemin.size())) {
                    int vpPosX = (int) (vp.getPositionSurCarteX() * imageMinimumScale + imagePosX);
                    int vpPosY = (int) (vp.getPositionSurCarteY() * imageMinimumScale + imagePosY);
                    g2d.drawLine(precedentePosX, precedentePosY, vpPosX, vpPosY);
                    precedentePosX = vpPosX;
                    precedentePosY = vpPosY;
                }

                g2d.drawLine(premierePositionX, premierePositionY, precedentePosX, precedentePosY);
            }
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Dessine les villes sur la carte
        for (VillePoint vp : villesPoints) {
            if (vp.estVisible()) {
                int cityPosX = (int) (vp.getPositionSurCarteX() * imageMinimumScale + imagePosX);
                int cityPosY = (int) (vp.getPositionSurCarteY() * imageMinimumScale + imagePosY);
                g2d.fillOval(cityPosX - 5, cityPosY - 5, 10, 10);
                g2d.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 13));
                g2d.drawString(vp.getNom(), cityPosX - 10, cityPosY - 10);
            }
        }
    }

    /**
     * Ajoute une ville à la carte.
     *
     * @param ville La ville à ajouter.
     */
    public void ajoutVille(Ville ville) {
        VillePoint vp = this.getPointFromVille(ville);
        if (vp == null) {
            this.villesPoints.add(new VillePoint(ville));
        } else {
            vp.rendVisible();
        }
    }

    /**
     * Ajoute une ville avec sa latitude et longitude explicites à la carte.
     *
     * @param ville     La ville à ajouter.
     * @param latitude  Latitude de la ville.
     * @param longitude Longitude de la ville.
     */
    public void ajoutVille(Ville ville, double latitude, double longitude) {
        VillePoint vp = this.getPointFromVille(ville);
        if (vp == null) {
            this.villesPoints.add(new VillePoint(ville, latitude, longitude));
        } else {
            vp.rendVisible();
        }
    }

    /**
     * Ajoute une liste de villes à la carte.
     *
     * @param villes Liste des villes à ajouter.
     */
    public void ajouterListeVille(ArrayList<Ville> villes) {
        ArrayList<Ville> villesNonCalculee = new ArrayList<>();
        for (Ville ville : villes) {
            VillePoint vp = this.getPointFromVille(ville);
            if (vp == null) {
                villesNonCalculee.add(ville);
            } else {
                vp.rendVisible();
            }
        }

        double[][] latlong = data.SQL.getCoordinatesForVilles(villesNonCalculee);
        for (int i = 0; i < villesNonCalculee.size(); i++) {
            this.villesPoints.add(new VillePoint(villesNonCalculee.get(i), latlong[i][0], latlong[i][1]));
        }
    }

    /**
     * Retourne le point correspondant à une ville donnée.
     *
     * @param ville La ville pour laquelle chercher le point.
     * @return Le point correspondant, ou null si aucun point n'existe.
     */
    private VillePoint getPointFromVille(Ville ville) {
        for (VillePoint vp : this.villesPoints) {
            if (vp.getVilleObject().equals(ville)) {
                return vp;
            }
        }
        return null;
    }

    /**
     * Affiche une ville sur la carte.
     *
     * @param ville La ville à afficher.
     */
    public void afficheVille(Ville ville) {
        for (VillePoint vp : villesPoints) {
            if (vp.getVilleObject().equals(ville)) {
                vp.rendVisible();
            }
        }
    }

    /**
     * Cache une ville sur la carte.
     *
     * @param ville La ville à cacher.
     */
    public void cacheVille(Ville ville) {
        for (VillePoint vp : villesPoints) {
            if (vp.getVilleObject().equals(ville)) {
                vp.rendInvisible();
            }
        }
    }

    /**
     * Réinitialise la carte en cachant toutes les villes et en supprimant les chemins.
     */
    public void clearCarte() {
        villesPoints.forEach(VillePoint::rendInvisible);
        chemins.clear();
    }

    /**
     * Supprime tous les chemins de la carte.
     */
    public void clearChemins() {
        chemins.clear();
    }

    /**
     * Ajoute un chemin reliant plusieurs villes.
     *
     * @param villes Liste des villes à connecter par un chemin.
     */
    public void addChemin(ArrayList<Ville> villes) {
        ArrayList<VillePoint> villesDuChemin = new ArrayList<>();
        for (Ville ville : villes) {
            for (VillePoint vp : villesPoints) {
                if (vp.getVilleObject().equals(ville)) {
                    villesDuChemin.add(vp);
                }
            }
        }
        chemins.add(new Chemin(villesDuChemin));
    }

    /**
     * Ajoute un chemin reliant plusieurs villes (version tableau).
     *
     * @param villes Tableau des villes à connecter par un chemin.
     */
    public void addChemin(Ville[] villes) {
        addChemin(new ArrayList<>(Arrays.asList(villes)));
    }

    /**
     * Capture l'état actuel de la carte sous forme d'image (BufferedImage).
     * Cette méthode crée une image de la taille de la zone d'affichage du Panel, 
     * puis utilise le rendu du Panel pour dessiner la carte avec ses villes et chemins.
     *
     * @return L'image capturée sous forme de BufferedImage.
     */
    public BufferedImage captureImageCarte() {
        // Créer une BufferedImage avec les mêmes dimensions que le JPanel
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Appeler la méthode paintComponent pour dessiner la carte actuelle dans l'image
        paint(g2d);
        g2d.dispose();

        // Retourner l'image capturée
        return image;
    }
    
    /**
     * Classe interne représentant un point (ville) sur la carte.
     */
    class VillePoint {

        /**
         * Objet Ville associé à ce point.
         */
        private Ville villeObject;

        /**
         * Latitude de la ville.
         */
        private double latitude;

        /**
         * Longitude de la ville.
         */
        private double longitude;

        /**
         * Position X sur la carte.
         */
        private int positionSurCarteX = 0;

        /**
         * Position Y sur la carte.
         */
        private int positionSurCarteY = 0;

        /**
         * Indique si le point est visible sur la carte.
         */
        private boolean estVisible;

        /**
         * Constructeur avec latitude et longitude explicites.
         *
         * @param ville     Objet Ville associé.
         * @param latitude  Latitude de la ville.
         * @param longitude Longitude de la ville.
         */
        public VillePoint(Ville ville, double latitude, double longitude) {
            this.villeObject = ville;
            this.latitude = latitude;
            this.longitude = longitude;
            this.estVisible = true;
            calculePositionSurCarte();
        }
        
        
       

        /**
         * Constructeur basé sur les coordonnées de la base de données.
         *
         * @param ville Objet Ville associé.
         */
        public VillePoint(Ville ville) {
            double[] latlong = data.SQL.getCoordonnée(ville);
            this.villeObject = ville;
            this.latitude = latlong[0];
            this.longitude = latlong[1];
            this.estVisible = true;
            calculePositionSurCarte();
        }

        /**
         * Calcule les positions X et Y de la ville sur l'image de la carte.
         */
        private void calculePositionSurCarte() {
            final double mapLonLeft = -5.0;
            final double mapLonRight = 9.5;
            final double mapLatBottom = 41.0;
            final double mapLatTop = 51.0;

            double x = (this.longitude - mapLonLeft) / (mapLonRight - mapLonLeft) * (imageCarteFrance.getWidth());
            double latRad = Math.toRadians(this.latitude);
            double mercN = Math.log(Math.tan(Math.PI / 4 + latRad / 2));
            double y = (1 - (mercN - Math.log(Math.tan(Math.PI / 4 + Math.toRadians(mapLatBottom) / 2))) /
                    (Math.log(Math.tan(Math.PI / 4 + Math.toRadians(mapLatTop) / 2)) -
                            Math.log(Math.tan(Math.PI / 4 + Math.toRadians(mapLatBottom) / 2))))
                    * (imageCarteFrance.getHeight());

            this.positionSurCarteX = (int) x;
            this.positionSurCarteY = (int) y;
        }

        /**
         * Retourne le nom de la ville.
         *
         * @return Nom de la ville.
         */
        public String getNom() {
            return villeObject.getName();
        }

        /**
         * Indique si le point est visible.
         *
         * @return True si le point est visible, false sinon.
         */
        public boolean estVisible() {
            return estVisible;
        }

        /**
         * Rend le point visible.
         */
        public void rendVisible() {
            this.estVisible = true;
        }

        /**
         * Rend le point invisible.
         */
        public void rendInvisible() {
            this.estVisible = false;
        }

        /**
         * Retourne la position X du point sur la carte.
         *
         * @return Position X.
         */
        public int getPositionSurCarteX() {
            return positionSurCarteX;
        }

        /**
         * Retourne la position Y du point sur la carte.
         *
         * @return Position Y.
         */
        public int getPositionSurCarteY() {
            return positionSurCarteY;
        }

        /**
         * Retourne l'objet Ville associé à ce point.
         *
         * @return Objet Ville.
         */
        public Ville getVilleObject() {
            return villeObject;
        }
    }

    /**
     * Classe interne représentant un chemin reliant plusieurs points (villes) sur la carte.
     */
    class Chemin {

        /**
         * Liste des points (villes) constituant le chemin.
         */
        ArrayList<VillePoint> villePoints;

        /**
         * Indique si le chemin est visible sur la carte.
         */
        boolean estVisible;

        /**
         * Constructeur pour créer un chemin avec une liste de points (villes).
         *
         * @param villePoints Liste des points constituant le chemin.
         */
        public Chemin(ArrayList<VillePoint> villePoints) {
            this.villePoints = villePoints;
            this.estVisible = true;
        }

        /**
         * Indique si le chemin est visible.
         *
         * @return True si le chemin est visible, false sinon.
         */
        public boolean estVisible() {
            return estVisible;
        }

        /**
         * Rend le chemin visible.
         */
        public void rendVisible() {
            this.estVisible = true;
        }

        /**
         * Rend le chemin invisible.
         */
        public void rendInvisible() {
            this.estVisible = false;
        }

        /**
         * Retourne la liste des points (villes) constituant le chemin.
         *
         * @return Liste des points.
         */
        public ArrayList<VillePoint> getVillePoints() {
            return villePoints;
        }
    }
}
