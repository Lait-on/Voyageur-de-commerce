package Affichage;

import Data.data;
import ecosysteme.*;
import SQL.*;
import java.util.*;
import java.util.List;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * PanelFichier est une classe qui gère l'interface pour la génération d'un fichier modelisant la solution au problème.
 * Cette interface permet à l'utilisateur de saisir un nom de fichier et de générer
 * un fichier contenant des informations sur les personnes de l'écosystème et la solution du problème.
 */
public class PanelFichier extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut qui initialise l'interface utilisateur du Panel.
     */
    public PanelFichier() {
        setLayout(new BorderLayout(10, 10)); // Espacement entre les composants

        // Titre en haut
        JLabel titleLabel = new JLabel("Création de fichier", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espacement autour
        add(titleLabel, BorderLayout.NORTH);

        // Panel central avec formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Disposition verticale
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espacement interne

        // Ajout du champ texte avec label
        JLabel label = new JLabel("Nom du fichier :");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        formPanel.add(label);

        formPanel.add(Box.createVerticalStrut(10)); // Espacement entre l'étiquette et le champ texte

        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(300, 30)); // Taille maximale
        textField.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        formPanel.add(textField);

        formPanel.add(Box.createVerticalStrut(10)); // Espacement entre le champ texte et le bouton

        // Bouton Enregistrer
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Police plus petite
        saveButton.setFocusPainted(false); // Retire le contour bleu
        saveButton.setMaximumSize(new Dimension(120, 30)); // Taille plus petite
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        saveButton.addActionListener(e -> {
            String nomFichier = textField.getText().trim();
            if (!nomFichier.isEmpty()) {
                CreationFichier(nomFichier);
            } else {
                JOptionPane.showMessageDialog(this, "Le nom du fichier ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Crée un fichier HTML dans le répertoire "GenerateFiles" avec les informations nécessaires.
     * 
     * @param NomFichier Le nom du fichier à créer (sans extension).
     */
    private void CreationFichier(String NomFichier) {
        try {
            // Création du répertoire si nécessaire
            File dossier = new File("GenerateFiles");
            dossier.mkdir();

            // Création du fichier HTML
            FileWriter fichier = new FileWriter(new File(new File("GenerateFiles"), NomFichier + ".html"));
            fichier.write("<!DOCTYPE html>\n<html lang=\"fr\">\n<head>\n<meta charset=\"UTF-8\">\n<meta name=\"viewport\" "
                    + "content=\"width=device-width, initial-scale=1.0\">\n<title>Voyageur de Commerce</title>\n<style>\nbody "
                    + " {\nbackground-color: #fdf6e3;\nfont-family: Arial, sans-serif;\nmargin: 0;\npadding: 0;\ncolor:"
                    + " #333;\n}\nheader, footer {\nbackground-color: #333;\ncolor: white;\ntext-align: center;\npadding: 1rem 0;\n}\nheader h1,"
                    + " footer h1 {\nmargin: 0;\nfont-size: 1.5rem;\n}\n.container {\nmax-width: 800px;\nmargin: 20px auto;\npadding: 20px;\nbackground-color: "
                    + " #fff;\nborder-radius: 8px;\nbox-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\n}\nh2 {\nborder-bottom: 2px solid #ffa726;\npadding-bottom:"
                    + " 5px;\nmargin-top: 30px;\ncolor: #d84315;\n}\np {\nline-height: 1.6;\n}\n.highlight {\nbackground-color: #fff9c4;\npadding: "
                    + "10px;\nborder-radius: 5px;\nmargin-bottom: 20px;\n}\n.distance {\ncolor: red;\nfont-weight: bold;\n}\nul {\nlist-style: none;\npadding: "
                    + "0;\n}\nul li {\nbackground: #e3f2fd;\nmargin: 5px 0;\npadding: 10px;\nborder-radius: 5px;\nbox-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n}\n</style>\n</head>\n"
                    + "<body>\n<header>\n<h1>Voyageur de Commerce</h1>\n</header>\n");

            // Ajout de l'image de la carte dans le HTML
            if (data.imageCarte != null) {
                String nomImage =  NomFichier + "_imageCarte.png"; // Image à côté du fichier HTML
                // Enregistrer l'image dans le répertoire de génération
                File imageFile = new File("GenerateFiles/" +nomImage);
                javax.imageio.ImageIO.write(data.imageCarte, "png", imageFile); // Sauvegarder l'image

                fichier.write("<div class=\"container\">\n<h2>Carte</h2>\n");
                fichier.write("<img src=\"" + nomImage + "\" alt=\"Carte\" style=\"width: 100%; height: auto;\" />\n");
                fichier.write("</div>\n");
            }

            if (data.tsp != null) {
                InputFileVille(fichier, Arrays.asList(data.tsp.getDerniereSolution()), data.tsp.getCoutDerniereSolution());
            }
            InputFilePersonne(fichier);
            fichier.write("<footer>\r\n<h1>Merci de votre visite!</h1>\r\n</footer>\n</body>\n</html>");

            fichier.close();

            // Ouvre automatiquement le fichier dans le navigateur
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(new File("GenerateFiles"), NomFichier + ".html"));

        } catch (IOException e) {
            System.out.println("Erreur dans l'ouverture du fichier");
        }
    }

    /**
     * Ajoute les informations sur les personnes dans le fichier HTML.
     * 
     * @param fichier Le FileWriter utilisé pour écrire dans le fichier HTML.
     * @return true si l'écriture s'est bien déroulée, false sinon.
     */
    private Boolean InputFilePersonne(FileWriter fichier) {
        try {
            if (!data.chercheurs.isEmpty() || !data.MCFs.isEmpty() || !data.etudiants.isEmpty()) {
                fichier.write("<h2>Liste des personnes</h2>\n");

                // Section Chercheurs
                if (!data.chercheurs.isEmpty()) {
                    fichier.write("<h3>Chercheurs:</h3>\n<ul>");
                    for (Chercheur c : data.chercheurs) {
                        fichier.write("<li>=> " + c.getPrenom() + " " + c.getNom() + ": " + c.getAge() + " ans, Ville [" + c.getVilleNom() + "], Bureau(" + c.getNumBureau() + "), Discipline(s){" + c.getDisciplines() + "}");
                        if (!c.getEtudiantsString().isEmpty()) {
                            fichier.write(", Etudiant(s) : " + c.getEtudiantsString());
                        }
                        fichier.write("</li>\n");
                    }
                    fichier.write("</ul>\n");
                }

                // Section MCFs
                if (!data.MCFs.isEmpty()) {
                    fichier.write("<h3>MCFs:</h3>\n<ul>");
                    for (MCF m : data.MCFs) {
                        fichier.write("<li>=> " + m.getPrenom() + " " + m.getNom() + ": " + m.getAge() + " ans, Ville [" + m.getVilleNom() + "], Bureau (" + m.getNumBureau() + "), Discipline(s) {" + m.getDisciplines() + "}");
                        if (!m.getEtudiantString().isEmpty()) {
                            fichier.write(", Etudiant : " + m.getEtudiantString());
                        }
                        fichier.write("</li>\n");
                    }
                    fichier.write("</ul>\n");
                }

                // Section Étudiants
                if (!data.etudiants.isEmpty()) {
                    fichier.write("<h3>Étudiants:</h3>\n<ul>");
                    for (Etudiant e : data.etudiants) {
                        fichier.write("<li>=> " + e.getPrenom() + " " + e.getNom() + ": " + e.getAge() + " ans, Ville [" + e.getVilleNom() + "], Sujet de Thèse {" + e.getSujetDeThese() + "}, Année de Thèse (" + e.getAnneeDeThese() + ")" + ", Discipline :" + e.getDiscipline());
                        if (!e.getEncadrantString().isEmpty()) {
                            fichier.write(", Encadrant : " + e.getEncadrantString());
                        }
                        fichier.write("</li>\n");
                    }
                    fichier.write("</ul>\n");
                }

                return true;
            }
        } catch (IOException e) {
            System.out.println("Erreur dans l'écriture du fichier");
        }
        return false;
    }

    /**
     * Ajoute les informations sur les villes parcourues dans le fichier HTML.
     * 
     * @param fichier     Le FileWriter utilisé pour écrire dans le fichier HTML.
     * @param villes      Liste des villes parcourues.
     * @param ValSolution La distance totale de la solution.
     * @return true si l'écriture s'est bien déroulée, false sinon.
     */
    private Boolean InputFileVille(FileWriter fichier, List<Ville> villes, double ValSolution) {
        String[] mot = {"Tourner vers ", "Faire demi-tour vers ", "Aller à ", "Voler jusqu'à ", "Ramper jusqu'à ", "Faire de l'auto-stop jusqu'à ", "Se diriger vers ", "Une bonne note nous attend à ", "J'aime bien la ville de ", "Je pense qu'il faut aller à ", "Attention notre prochaine destination est "};
        try {
            if (villes != null && !villes.isEmpty()) {
                fichier.write("<div class=\"container\">\n<h2>Solution</h2>\n<div class=\"highlight\">\n");
                for (Ville v : villes) {
                    fichier.write("<p>➞ " + genereDebutPhrase(mot) + "<i>" + v.getNomDepartement() + "</i></p>\n");
                }
                fichier.write("</div>\n<p>Cette solution nécessite de parcourir <span class=\"distance\">" + String.format("%.2f", ValSolution) + "</span> Km.</p>\n</div>\n");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Erreur dans l'écriture du fichier");
        }
        return false;
    }

    /**
     * Génère un début de phrase aléatoire à partir d'un tableau de chaînes.
     * 
     * @param mots Tableau contenant les débuts de phrases possibles.
     * @return Un début de phrase sélectionné aléatoirement.
     */
    private String genereDebutPhrase(String[] mots) {
        Random rand = new Random();
        return mots[rand.nextInt(mots.length)];
    }
}
