package TriCycleHamiltonien;

import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ecosysteme.*;
import SQL.Ville;

/**
 * Classe représentant un tri des cheminements en fonction de la discipline.
 * Elle permet de filtrer les personnes en fonction de leur discipline et 
 * d'ajouter les villes associées dans un cycle hamiltonien.
 */
public class TriDomaine extends Tri {
    
    private List<Ville> CycleHamiltonien = new ArrayList<>();
    private static Discipline Domaine = Discipline.DROIT;

    /**
     * Constructeur de la classe TriDomaine, appelant le constructeur de la classe parente.
     */
    public TriDomaine() {
        super();
    }

    /**
     * Parcourt les personnes pour vérifier si elles appartiennent à la discipline donnée
     * et ajoute leurs villes au cycle hamiltonien si elles ne sont pas déjà présentes.
     * @param discipline La discipline à filtrer.
     */
    public void discipline(Discipline discipline) {
        for (Personne p : this.personnes) {
            if (p instanceof Titulaire) {
                if (((Titulaire) p).getDisciplineSet().contains(discipline) && !CycleHamiltonien.contains(p.getVilleObject())) {
                	Ville v = p.getVilleObject();
                	if (this.VerifVille(v)){
                		CycleHamiltonien.add(v);
                	}
                }
            } else {
                if (((Etudiant) p).getDiscipline().equals(discipline.toString()) && !CycleHamiltonien.contains(p.getVilleObject())) {
                	Ville v = p.getVilleObject();
                	if (this.VerifVille(v)){
                		CycleHamiltonien.add(v);
                	}
                }
            }
        }
    }

    /**
     * Récupère la liste des villes formant un cycle hamiltonien pour une discipline donnée.
     * @return La liste des villes du cycle hamiltonien.
     */
    @Override
    public List<Ville> Getter() {
        discipline(Domaine);
        CycleHamiltonien.removeIf(Objects::isNull);
        return CycleHamiltonien;
    }

    /**
     * Récupère le nom du type de tri.
     * @return Le nom du type de tri.
     */
    public static String GetName() {
        return "Tri Domaine";
    }

    /**
     * Ouvre une fenêtre de paramètres permettant à l'utilisateur de sélectionner une discipline.
     */
    public static void Parameter() {
        // Création de la boîte de dialogue
        JDialog param = new JDialog();
        param.setModal(true);
        param.setSize(400, 200);
        param.setTitle("Paramètres - Sélection de Discipline");
        param.setLayout(new BorderLayout()); // Utilisation d'un BorderLayout

        // Panel pour la sélection de la discipline
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Sélectionnez la discipline des personnes recherchées :");
        JComboBox<String> selectionRegion = new JComboBox<>(
                Arrays.stream(Discipline.values()).map(Enum::name).toArray(String[]::new) // Directement depuis l'enum
        );
        panel.add(label);
        panel.add(selectionRegion);

        // Bouton "Enregistrer"
        JButton save = new JButton("Enregistrer");
        save.addActionListener(e -> {
            try {
                Domaine = Discipline.valueOf((String) selectionRegion.getSelectedItem());
            } catch (Exception z) {
                // Rien à faire dans le catch car la discipline existe forcément
            }
            param.dispose(); // Ferme la boîte de dialogue
        });

        // Ajout des composants dans le JDialog
        param.add(panel, BorderLayout.CENTER);
        param.add(save, BorderLayout.SOUTH);

        // Positionner et rendre visible
        param.setLocationRelativeTo(null); // Centre la boîte de dialogue sur l'écran
        SwingUtilities.invokeLater(() -> param.setVisible(true));
    }
}
