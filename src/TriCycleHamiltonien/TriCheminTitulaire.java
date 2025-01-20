package TriCycleHamiltonien;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;

import java.util.*;

import Data.data;
import ecosysteme.*;
import SQL.Ville;

/**
 * Classe représentant un tri des cheminements des titulaires avec l'ajout des villes
 * dans un cycle hamiltonien en fonction des titulaires, chercheurs et MCF.
 */
public class TriCheminTitulaire extends Tri {
    private List<Ville> CycleHamiltonien = new ArrayList<>();
    private static Titulaire titulaire = null;

    /**
     * Constructeur de la classe TriCheminTitulaire, appelant le constructeur de la classe parente.
     */
    public TriCheminTitulaire() {
        super();
    }

    /**
     * Ajoute les villes des titulaires (MCF ou Chercheur) et des étudiants associés au cycle hamiltonien.
     * @param t Le titulaire à traiter (MCF ou Chercheur).
     */
    public void CheminEleves(Titulaire t) {
        if (t == null) {
            return;
        } else if (t instanceof MCF) {
            if (!CycleHamiltonien.contains(t.getVilleObject())) {
            	Ville v = t.getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	}
            }
            if (((MCF) t).getEtudiant() != null && !CycleHamiltonien.contains(((MCF) t).getEtudiant().getVilleObject())) {
                Ville v = ((MCF) t).getEtudiant().getVilleObject();
            	if (this.VerifVille(v)){
            		CycleHamiltonien.add(v);
            	}
            }
        } else {
            if (!CycleHamiltonien.contains(t.getVilleObject())) {
                CycleHamiltonien.add(t.getVilleObject());
            }
            Set<Etudiant> etudiants = ((Chercheur) t).getEtudiants();
            if (etudiants != null && !etudiants.isEmpty()) {
                for (Etudiant e : etudiants) {
                    if (!CycleHamiltonien.contains(e.getVilleObject())) {
                    	Ville v = e.getVilleObject();
                    	if (this.VerifVille(v)){
                    		CycleHamiltonien.add(v);
                    	}
                    }
                }
            }
        }
    }

    /**
     * Récupère la liste des villes formant un cycle hamiltonien après avoir parcouru
     * les titulaires, chercheurs et MCF.
     * @return La liste des villes du cycle hamiltonien.
     */
    @Override
    public List<Ville> Getter() {
        CheminEleves(titulaire);
        CycleHamiltonien.removeIf(Objects::isNull);
        return CycleHamiltonien;
    }

    /**
     * Récupère le nom du type de tri.
     * @return Le nom du type de tri.
     */
    public static String GetName() {
        return "Tri Chemin Titulaire";
    }

    /**
     * Ouvre une fenêtre de paramètres pour sélectionner un titulaire.
     * L'utilisateur peut choisir entre un chercheur ou un MCF.
     */
    public static void Parameter() {
        JDialog param = new JDialog();
        param.setSize(400, 300);
        param.setTitle("Paramètres");
        param.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel label = new JLabel("Sélectionner le type de personne recherché : ");
        JComboBox<String> selectionTitulaire = new JComboBox<>();
        setTitulaireBox(selectionTitulaire);

        // Si aucun titulaire n'est disponible, affiche un message d'erreur
        if (selectionTitulaire.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Aucun Titulaire disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        
        panel.add(label);
        panel.add(selectionTitulaire);

        JButton save = new JButton("Enregistrer");
        save.addActionListener(e -> {
            int indice = selectionTitulaire.getSelectedIndex();
            // Sélectionne le titulaire en fonction de l'indice
            if (indice < data.chercheurs.size()) {
                titulaire = data.chercheurs.get(indice);
            } else {
                titulaire = data.MCFs.get(indice - data.chercheurs.size());
            }
            param.dispose();
        });

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(save, BorderLayout.SOUTH);

        param.add(mainPanel);

        param.setLocationRelativeTo(null); 
        SwingUtilities.invokeLater(() -> param.setVisible(true));
    }

    /**
     * Remplit la JComboBox avec les noms des titulaires disponibles (chercheurs et MCFs).
     * @param selectionTitulaire La JComboBox à remplir avec les titulaires.
     */
    private static void setTitulaireBox(JComboBox<String> selectionTitulaire) {
        // Ajoute les chercheurs dans la JComboBox
        for (Titulaire t : data.chercheurs) {
            selectionTitulaire.addItem(t.getPrenom() + " " + t.getNom());
        }
        // Ajoute les MCFs dans la JComboBox
        for (Titulaire t : data.MCFs) {
            selectionTitulaire.addItem(t.getPrenom() + " " + t.getNom());
        }
    }
}
