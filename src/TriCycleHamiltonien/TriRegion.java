package TriCycleHamiltonien;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.*;
import java.awt.*;

import Data.data;
import ecosysteme.Personne;
import SQL.*;

/**
 * Classe représentant un tri des cheminements en fonction de la région.
 * Elle permet de filtrer les personnes en fonction de leur région et 
 * d'ajouter les villes associées dans un cycle hamiltonien.
 */
public class TriRegion extends Tri {
    
    private List<Ville> CycleHamiltonien = new ArrayList<>();
    private Java_to_SQL SQL;
    private static String region = Region.BRETAGNE.name();

    /**
     * Constructeur de la classe TriRegion, appelant le constructeur de la classe parente
     * et initialisant l'objet SQL pour l'accès à la base de données.
     */
    public TriRegion() {
        super();
        this.SQL = data.SQL;
    }

    /**
     * Parcourt toutes les personnes pour vérifier si elles appartiennent à la région donnée
     * et ajoute leurs villes au cycle hamiltonien si elles ne sont pas déjà présentes.
     * 
     * @param NomRegion Le nom de la région à filtrer.
     */
    public void Region(String NomRegion) {
        List<Personne> personnes = new ArrayList<>();
        personnes.addAll(data.chercheurs);
        personnes.addAll(data.etudiants);
        personnes.addAll(data.MCFs);

        // Parcourt les personnes et vérifie leur région
        for (Personne p : personnes) {
            if (p != null && NomRegion.equalsIgnoreCase(Region.ville_to_region(p.getVilleObject(), SQL)) && !CycleHamiltonien.contains(p.getVilleObject())) {
            	Ville v = p.getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	}
            }
        }
    }

    /**
     * Récupère la liste des villes formant un cycle hamiltonien pour une région donnée.
     * 
     * @return La liste des villes du cycle hamiltonien.
     */
    @Override
    public List<Ville> Getter() {
        Region(region); // Filtrage des villes en fonction de la région
        CycleHamiltonien.removeIf(Objects::isNull); // Nettoie les entrées nulles
        return CycleHamiltonien;
    }

    /**
     * Récupère le nom du type de tri.
     * 
     * @return Le nom du type de tri.
     */
    public static String GetName() {
        return "Tri Region";
    }

    /**
     * Ouvre une fenêtre de paramètres permettant à l'utilisateur de sélectionner une région.
     */
    public static void Parameter() {
        JDialog param = new JDialog();
        param.setModal(true);
        param.setSize(400, 200);
        param.setTitle("Paramètres");
        param.setLayout(new BorderLayout()); // Utilisation d'un BorderLayout

        // Panel pour la sélection de la région
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Sélectionnez la région des personnes recherchées :");
        JComboBox<String> selectionRegion = new JComboBox<>(
                Arrays.stream(Region.values()).map(Enum::name).toArray(String[]::new) // Directement depuis l'enum
        );
        panel.add(label);
        panel.add(selectionRegion);

        // Bouton "Enregistrer"
        JButton save = new JButton("Enregistrer");
        save.addActionListener(e -> {
            region = (String) selectionRegion.getSelectedItem(); // Mise à jour de la région sélectionnée
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
