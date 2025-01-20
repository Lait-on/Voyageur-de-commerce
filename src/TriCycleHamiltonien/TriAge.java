package TriCycleHamiltonien;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.*;

import javax.swing.*;

import ecosysteme.Personne;
import SQL.Ville;

/**
 * Classe TriAge implémentant un tri des villes basées sur des contraintes d'âge 
 * appliquées à une liste de personnes. Hérite de la classe abstraite {@code Tri}.
 */
public class TriAge extends Tri {

    /** Liste des villes formant le cycle hamiltonien après application du tri. */
    private List<Ville> CycleHamiltonien = new ArrayList<>();

    /** Type de comparaison d'âge (0: supérieur, 1: inférieur, 2: égal). */
    private static int type = 0;

    /** Contrainte d'âge utilisée pour le tri. */
    private static int age = 0;

    /**
     * Constructeur par défaut de la classe TriAge.
     * Appelle le constructeur de la classe parente {@code Tri}.
     */
    public TriAge() {
        super();
    }

    /**
     * Ajoute à la liste {@code CycleHamiltonien} les villes associées aux personnes
     * ayant un âge strictement supérieur à la contrainte définie.
     * 
     * @param age Contrainte d'âge minimale.
     */
    public void supAge(int age) {
        for (Personne p : this.personnes) {
            if (p.getAgeInt() > age && !CycleHamiltonien.contains(p.getVilleObject())) {
                Ville v = p.getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	}
            }
        }
    }

    /**
     * Ajoute à la liste {@code CycleHamiltonien} les villes associées aux personnes
     * ayant un âge strictement inférieur à la contrainte définie.
     * 
     * @param age Contrainte d'âge maximale.
     */
    public void infAge(int age) {
        for (Personne p : this.personnes) {
            if (p.getAgeInt() < age && !CycleHamiltonien.contains(p.getVilleObject())) {
            	Ville v = p.getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	};
            }
        }
    }

    /**
     * Ajoute à la liste {@code CycleHamiltonien} les villes associées aux personnes
     * ayant un âge égal à la contrainte définie.
     * 
     * @param age Contrainte d'âge exacte.
     */
    public void equalAge(int age) {
        for (Personne p : this.personnes) {
            if (p.getAgeInt() == age && !CycleHamiltonien.contains(p.getVilleObject())) {
            	Ville v = p.getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	}
            }
        }
    }

    /**
     * Retourne une liste des villes après application du tri selon la contrainte d'âge.
     * 
     * @return Liste des villes correspondant au critère d'âge.
     */
    @Override
    public List<Ville> Getter() {
        switch (type) {
            case 0 -> supAge(age);
            case 1 -> infAge(age);
            case 2 -> equalAge(age);
        }
        // Retire les entrées null de la liste
        CycleHamiltonien.removeIf(Objects::isNull);
        //retire doublon


        
        return CycleHamiltonien;
    }

    /**
     * Retourne le nom du tri pour identification.
     * 
     * @return Une chaîne de caractères représentant le nom du tri ("Tri Age").
     */
    public static String GetName() {
        return "Tri Age";
    }

    /**
     * Ouvre une boîte de dialogue permettant de configurer les paramètres du tri.
     * L'utilisateur peut choisir le type de comparaison d'âge et la contrainte d'âge.
     */
    public static void Parameter() {
        // Création de la boîte de dialogue
        JDialog param = new JDialog();
        param.setModal(true);
        param.setSize(400, 200);
        param.setTitle("Paramètres");
        param.setLayout(new BorderLayout());

        // Panel principal pour les champs de sélection
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelType = new JLabel("Sélectionnez le type de comparaison sur l'âge :");
        JComboBox<String> selectionType = new JComboBox<>(new String[]{"Supérieur", "Inférieur", "Égal"});
        JLabel labelAge = new JLabel("Sélectionnez la contrainte d'âge :");
        JComboBox<Integer> selectionAge = new JComboBox<>();

        // Ajout des valeurs d'âge (0 à 99)
        for (int i = 0; i < 100; i++) {
            selectionAge.addItem(i);
        }

        // Ajout des composants au Panel
        panel.add(labelType);
        panel.add(selectionType);
        panel.add(labelAge);
        panel.add(selectionAge);

        // Bouton pour enregistrer les paramètres
        JButton save = new JButton("Enregistrer");
        save.addActionListener(e -> {
            type = selectionType.getSelectedIndex();
            age = (int) selectionAge.getSelectedItem();
            param.dispose(); // Ferme la boîte de dialogue après sauvegarde
        });

        // Ajout des composants dans la boîte de dialogue
        param.add(panel, BorderLayout.CENTER);
        param.add(save, BorderLayout.SOUTH);

        // Centre la boîte de dialogue à l'écran et la rend visible
        param.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> param.setVisible(true));
    }
}
