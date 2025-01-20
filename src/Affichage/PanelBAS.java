package Affichage;

import javax.swing.*;
import Data.data;
import SQL.Ville;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * PanelBAS représente un Panel interactif permettant à l'utilisateur
 * de manipuler une liste de villes en mode "Bac à Sable".
 * Il permet d'ajouter des villes manuellement ou aléatoirement,
 * de visualiser les villes sélectionnées et de les transmettre au module d'algorithmes.
 */
public class PanelBAS extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Barre de recherche pour suggérer et ajouter des villes. */
    private TextFieldAvecSuggestion barreRecherche;

    /** Bouton pour ajouter une ville à partir de la barre de recherche. */
    private JButton bouttonAjout;

    /** Bouton pour ajouter une ville sélectionnée aléatoirement. */
    private JButton boutonAleatoire;

    /** Bouton pour réinitialiser la liste des villes sélectionnées. */
    private JButton boutonReini;

    /** Liste graphique affichant les noms des villes sélectionnées. */
    private JList<String> villesDisplay;

    /** Modèle sous-jacent pour la liste des villes sélectionnées. */
    private DefaultListModel<String> villesModel;

    /** Liste des objets Ville actuellement sélectionnés. */
    private ArrayList<Ville> villesSelect;

    /** Panel pour intégrer les fonctionnalités d'algorithmes. */
    private PanelAlgo panelAlgo;

    /**
     * Constructeur de la classe PanelBAS.
     * Initialise l'interface utilisateur et configure les actions associées.
     * 
     * @throws Exception si une erreur survient lors de la mise à jour des données initiales.
     */
    public PanelBAS() throws Exception {
        setLayout(new BorderLayout(10, 10));
        this.villesSelect = new ArrayList<>();
        data.updateVilleCarteSandBox(this.villesSelect);

        // Création du titre
        JLabel titreLabel = new JLabel("Bac à Sable", JLabel.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titreLabel, BorderLayout.NORTH);

        // Corps principal de la page
        JPanel corpsPage = new JPanel();
        corpsPage.setLayout(new BorderLayout(10, 10));

        // Section pour l'entrée utilisateur
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ajout de villes"));

        barreRecherche = new TextFieldAvecSuggestion(20,
                new ArrayList<>(data.villes.stream().map(Ville::getNomDepartement).toList()));
        inputPanel.add(barreRecherche, BorderLayout.CENTER);

        // Section des boutons
        JPanel boutonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        bouttonAjout = new JButton("Ajouter");
        boutonPanel.add(bouttonAjout);

        boutonAleatoire = new JButton("Ajouter une ville aléatoire");
        boutonPanel.add(boutonAleatoire);

        boutonReini = new JButton("Réinitialiser");
        boutonPanel.add(boutonReini);

        inputPanel.add(boutonPanel, BorderLayout.EAST);
        corpsPage.add(inputPanel, BorderLayout.NORTH);

        // Affichage des villes sélectionnées
        JPanel listeVillesPanel = new JPanel(new BorderLayout(10, 10));
        listeVillesPanel.setBorder(BorderFactory.createTitledBorder("Villes Sélectionnées"));
        villesModel = new DefaultListModel<>();
        villesDisplay = new JList<>(villesModel);
        JScrollPane scrollPane = new JScrollPane(villesDisplay);
        listeVillesPanel.add(scrollPane, BorderLayout.CENTER);

        corpsPage.add(listeVillesPanel, BorderLayout.CENTER);

        // Ajout du Panel des algorithmes
        panelAlgo = new PanelAlgo();
        panelAlgo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        corpsPage.add(panelAlgo, BorderLayout.SOUTH);

        add(corpsPage, BorderLayout.CENTER);

        // Configuration des actions des boutons
        configureActions();
    }

    /**
     * Configure les actions des boutons et des événements utilisateur.
     */
    private void configureActions() {
        bouttonAjout.addActionListener(e -> {
            String city = barreRecherche.getText().trim();
            barreRecherche.setText("");
            ajouterVille(city);
        });

        boutonAleatoire.addActionListener(e -> {
            if (!data.villes.isEmpty()) {
                Random random = new Random();
                String randomCity = data.villes.get(random.nextInt(data.villes.size())).getNomDepartement();
                ajouterVille(randomCity);
            }
        });

        villesDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Vérifie si une ville est double-cliquée pour la supprimer
                if (e.getClickCount() == 2) {
                    int index = villesDisplay.locationToIndex(e.getPoint());
                    if (index != -1) {
                        String selectedCity = villesModel.getElementAt(index);
                        supprimeVille(selectedCity);
                    }
                }
            }
        });

        boutonReini.addActionListener(e -> clearVille());
    }

    /**
     * Ajoute une ville à la liste des villes sélectionnées.
     * 
     * @param villeString le nom de la ville à ajouter.
     */
    private void ajouterVille(String villeString) {
        Ville ville = data.getVille(villeString);

        if (ville != null && !villesSelect.contains(ville)) {
            villesModel.addElement(ville.getNomDepartement());
            villesSelect.add(ville);
        }

        updateVillePanelAlgo();
        data.updateVilleCarteSandBox(villesSelect);
        tenteEasterEgg();
    }

    /**
     * Met à jour le Panel des algorithmes avec les villes actuellement sélectionnées.
     */
    private void updateVillePanelAlgo() {
        Ville[] villesArray = new Ville[villesSelect.size()];
        villesSelect.toArray(villesArray);
        panelAlgo.setVillesProbleme(villesArray);
    }

    /**
     * Réinitialise la liste des villes sélectionnées.
     */
    private void clearVille() {
        villesModel.clear();
        villesSelect.clear();
        updateVillePanelAlgo();
        data.updateVilleCarteSandBox(villesSelect);
    }

    /**
     * Supprime une ville de la liste des villes sélectionnées.
     * 
     * @param villeString le nom de la ville à supprimer.
     */
    private void supprimeVille(String villeString) {
        Ville ville = data.getVille(villeString);

        if (ville != null && villesSelect.contains(ville)) {
            villesModel.removeElement(ville.getNomDepartement());
            villesSelect.remove(ville);
            updateVillePanelAlgo();
            data.updateVilleCarteSandBox(villesSelect);
        }
    }
    
    private void tenteEasterEgg(){
    	
    	
    	String motPremiereLettreDesVilles = "";
    	for (Character c : this.villesSelect.stream().map(i -> i.getName().charAt(0)).toList()) {
    		motPremiereLettreDesVilles += c;
    	}
    	
    	if (motPremiereLettreDesVilles.equals("JAVA")) {
    		new EasterEgg();
    	}
    }
}
