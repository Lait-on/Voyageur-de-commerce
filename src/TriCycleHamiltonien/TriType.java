package TriCycleHamiltonien;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;
import java.awt.*;

import ecosysteme.*;
import SQL.Ville;

/**
 * Classe représentant un tri des cheminements en fonction du type de personne.
 * Elle permet de filtrer les personnes en fonction de leur type (Titulaire, Chercheur, MCF, Étudiant)
 * et d'ajouter les villes associées dans un cycle hamiltonien.
 */
public class TriType extends Tri {

    private List<Ville> CycleHamiltonien = new ArrayList<>();
    private static int type = 0; // 0: Titulaire, 1: Chercheur, 2: MCF, 3: Étudiant

    /**
     * Constructeur de la classe TriType, appelant le constructeur de la classe parente.
     */
    public TriType() {
        super(); 
    }

    /**
     * Filtre les personnes en fonction de leur type et ajoute leurs villes
     * au cycle hamiltonien si elles ne sont pas déjà présentes.
     * 
     * @param type Le type de personne à filtrer (0 = Titulaire, 1 = Chercheur, 2 = MCF, 3 = Étudiant)
     */
    public void Type(int type) {
        // Tri des personnes par type
        switch (type) {
            case 0: // Titulaires
                for (Personne p : this.personnes) {
                    if (p instanceof Titulaire && !CycleHamiltonien.contains(p.getVilleObject())) {
                    	Ville v = p.getVilleObject();
                    	if (this.VerifVille(v)){
                    		CycleHamiltonien.add(v);
                    	}
                    }
                }
                break;
            case 1: // Chercheurs
                for (Personne p : this.personnes) {
                    if (p instanceof Chercheur && !CycleHamiltonien.contains(p.getVilleObject())) {
                    	Ville v = p.getVilleObject();
                    	if (this.VerifVille(v)){
                    		CycleHamiltonien.add(v);
                    	}
                    }
                }
                break;
            case 2: // MCF
                for (Personne p : this.personnes) {
                    if (p instanceof MCF && !CycleHamiltonien.contains(p.getVilleObject())) {
                    	Ville v = p.getVilleObject();
                    	if (this.VerifVille(v)){
                    		CycleHamiltonien.add(v);
                    	}
                    }
                }
                break;
            case 3: // Étudiants
                for (Personne p : this.personnes) {
                    if (p instanceof Etudiant && !CycleHamiltonien.contains(p.getVilleObject())) {
                    	Ville v = p.getVilleObject();
                    	if (this.VerifVille(v)){
                    		CycleHamiltonien.add(v);
                    	}
                    }
                }
                break;
        }
    }

    /**
     * Récupère la liste des villes formant un cycle hamiltonien pour un type de personne donné.
     * 
     * @return La liste des villes du cycle hamiltonien.
     */
    @Override
    public List<Ville> Getter() {
        Type(type); // Applique le filtre en fonction du type sélectionné
        CycleHamiltonien.removeIf(Objects::isNull); // Nettoie les entrées nulles
        return CycleHamiltonien;
    }

    /**
     * Récupère le nom du type de tri.
     * 
     * @return Le nom du type de tri.
     */
    public static String GetName() {
        return "Tri Type";
    }

    /**
     * Ouvre une fenêtre de paramètres permettant à l'utilisateur de sélectionner un type de personne.
     */
    public static void Parameter() {
        // Création de la boîte de dialogue
        JDialog param = new JDialog();
        param.setSize(400, 300);
        param.setTitle("Paramètres");
        param.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel pour la sélection du type
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Sélectionner le type de personne recherché : ");
        JComboBox<String> selectionType = new JComboBox<>(new String[] { "Titulaire", "Chercheur", "MCF", "Étudiant" });
        panel.add(label);
        panel.add(selectionType);

        // Bouton "Enregistrer"
        JButton save = new JButton("Enregistrer");
        save.addActionListener(e -> {
            type = selectionType.getSelectedIndex(); // Mise à jour du type sélectionné
            param.dispose(); // Ferme la boîte de dialogue
        });

        // Ajout des composants dans le JDialog
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(save, BorderLayout.SOUTH);

        // Positionner et rendre visible
        param.add(mainPanel);
        param.setLocationRelativeTo(null); // Centre la boîte de dialogue sur l'écran
        SwingUtilities.invokeLater(() -> param.setVisible(true));
    }
}
