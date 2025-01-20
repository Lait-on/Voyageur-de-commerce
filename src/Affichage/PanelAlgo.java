package Affichage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.*;

import Data.data;
import SQL.Ville;
import problemeDuVoyageurDeCommerce.TravellingSalesmanProblem;

/**
 * Panel représentant l'interface utilisateur pour la sélection de l'algorithme
 * et l'exécution du problème du voyageur de commerce (TSP).
 * Permet à l'utilisateur de choisir un algorithme, de modifier ses paramètres,
 * et d'exécuter le calcul tout en affichant les résultats.
 */
public class PanelAlgo extends JPanel {
    private static final long serialVersionUID = 1L;

    // ComboBox pour sélectionner l'algorithme 
    private JComboBox<String> algoSelectionBox;

    // Bouton pour modifier les paramètres de l'algorithme
    private JButton modifyAlgoParamsButton;

    // Bouton pour exécuter l'algorithme
    private JButton executionButton;

    // Zone d'affichage des informations des résultats
    private JTextPane infoDisplayArea;

    // Zone d'affichage de la description de l'algorithme
    private JTextArea algoDescriptionArea;

    // Zone de défilement pour la description de l'algorithme
    private JScrollPane algoDescriptionScrollPane;

    // Fenêtre pour la modification des paramètres de l'algorithme
    private FenetreModificationParametre fmp = null;

    // Liste des villes du problème
    private Ville[] villesDuProbleme = null;

    /**
     * Constructeur du Panel. Initialise les composants graphiques,
     * configure les actions des boutons et met en place les éléments d'interface.
     * 
     * @throws Exception si une erreur se produit lors de l'initialisation
     */
    public PanelAlgo() throws Exception {
        setLayout(new BorderLayout(10, 10));

        // Titre principal
        JLabel titleLabel = new JLabel("Selection Algorithme", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal pour le contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Ligne 1 : Sélection de l'algorithme et modification des paramètres
        JLabel algoLabel = new JLabel("Choix Algorithmes");
        algoLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(algoLabel, gbc);

        algoSelectionBox = new JComboBox<>();
        setupChoixAlgo(algoSelectionBox);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(algoSelectionBox, gbc);

        algoSelectionBox.addActionListener(e -> {
            try {
                updateButtonModifierLesParametres();
                updateAlgoDescription((String) algoSelectionBox.getSelectedItem());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        
        this.fmp = new FenetreModificationParametre();
        modifyAlgoParamsButton = new JButton("Modifier les parametres");
        
        updateButtonModifierLesParametres();
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        contentPanel.add(modifyAlgoParamsButton, gbc);

        // Ligne 2 : Description de l'algorithme sélectionné
        algoDescriptionArea = new JTextArea(5, 0);
        algoDescriptionArea.setEditable(false);
        algoDescriptionArea.setWrapStyleWord(true);
        algoDescriptionArea.setLineWrap(true); 
        algoDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 15));
        algoDescriptionArea.setText("Selectionnez un algorithme");
        algoDescriptionArea.setSize(new Dimension(this.getWidth(), 20));
        algoDescriptionScrollPane = new JScrollPane(algoDescriptionArea);
        algoDescriptionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth  = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5; // Permet de s'étendre sur la droite
        gbc.weighty = 0; 
        contentPanel.add(algoDescriptionScrollPane, gbc);
        
        // Ligne 3 : Bouton d'exécution de l'algorithme
        executionButton = new JButton("Execution");
        executionButton.addActionListener(e -> execution(this.villesDuProbleme));
        executionButton.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.gridwidth  = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(executionButton, gbc);
        add(contentPanel, BorderLayout.CENTER);
        
     // Zone d'affichage des résultats sous forme HTML
        infoDisplayArea = new JTextPane();
        infoDisplayArea.setEditable(false);
        infoDisplayArea.setBorder(BorderFactory.createTitledBorder("Résultats:"));
        infoDisplayArea.setContentType("text/html");
        infoDisplayArea.setPreferredSize(new Dimension(400, 100)); //New
        infoDisplayArea.setMinimumSize(new Dimension(400, 100)); //New

        JScrollPane infoScrollPane = new JScrollPane(infoDisplayArea);
        infoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoScrollPane.setPreferredSize(new Dimension(400, 120)); //New
        infoScrollPane.setMinimumSize(new Dimension(400, 120)); //New

        // Ajout de la zone de défilement pour les résultats
        add(infoScrollPane, BorderLayout.SOUTH);
   
    }

    /**
     * Remplie la combo-box avec les noms des algorithmes implémentés.
     * 
     * @param Box La JComboBox à remplir avec les noms d'algorithmes.
     * @throws Exception si une erreur se produit lors de l'ajout des algorithmes.
     */
    private void setupChoixAlgo(JComboBox<String> Box) throws Exception {
        for (String nomAlgo : TravellingSalesmanProblem.getImplementedSolveurNames()) {
            Box.addItem(nomAlgo);
        }
    }

    /**
     * Met à jour la visibilité et l'action du bouton pour modifier les paramètres
     * en fonction de l'algorithme sélectionné.
     * 
     * @throws Exception si une erreur se produit lors de la récupération des paramètres.
     */
    private void updateButtonModifierLesParametres() throws Exception {
        if (TravellingSalesmanProblem.getPrametersFromSolveurName((String) algoSelectionBox.getSelectedItem()) == null) {
            modifyAlgoParamsButton.setVisible(false);
            this.fmp.setVisible(false);
        } else {
            modifyAlgoParamsButton.setVisible(true);
            for (ActionListener al : modifyAlgoParamsButton.getActionListeners()) {
                modifyAlgoParamsButton.removeActionListener(al);
            }
            modifyAlgoParamsButton.addActionListener(e -> {
                try {
                    this.fmp.loadParameters(TravellingSalesmanProblem.getPrametersFromSolveurName((String) algoSelectionBox.getSelectedItem()));
                    this.fmp.setVisible(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'initialisation de la fenetre " + e1.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    /**
     * Met à jour la description de l'algorithme sélectionné dans la zone de texte.
     * 
     * @param selectedAlgo Le nom de l'algorithme sélectionné.
     */
    private void updateAlgoDescription(String selectedAlgo) {
        try {
            String description = TravellingSalesmanProblem.getDescriptionFromSolveurName(selectedAlgo);
            algoDescriptionArea.setText(description);
        } catch (Exception e) {
            algoDescriptionArea.setText("Erreur lors du chargement de la description.");
        }
    }

    /**
     * Exécute l'algorithme sélectionné pour résoudre le problème du voyageur de commerce.
     * 
     * @param villes Les villes du problème de voyageur de commerce.
     */
    private void execution(Ville[] villes) {
        data.tsp = null;

        if (villes == null || villes.length == 0) {
            data.CF.clearChemins();
            updateZoneAffichageResultats();
            return;
        }

        String selectedAlgo = (String) algoSelectionBox.getSelectedItem();
        double[][] Distance = data.SQL.getDistance(villes);

        try {
            TravellingSalesmanProblem tsp = new TravellingSalesmanProblem(villes, Distance);
            tsp.setSolveur(selectedAlgo);
            data.tsp = tsp;
        } catch (Exception e) {
            return;
        }

        if (data.tsp.solveurEstParametrable() && this.fmp != null)
            this.fmp.updateParametres(data.tsp.getParametresSolveur());

        data.tsp.executeSolveur();

        if (data.tsp.estExecutionReussit()) {
            Ville[] solution = data.tsp.getDerniereSolution();
            data.CF.clearChemins();
            data.CF.addChemin(solution);
            data.imageCarte = data.CF.captureImageCarte();
        }

        updateZoneAffichageResultats();
    }

    /**
     * Permet de définir les villes du problème de voyageur de commerce.
     * 
     * @param villes Les villes à affecter au problème.
     */
    public void setVillesProbleme(Ville[] villes) {
        this.villesDuProbleme = villes.clone();
    }

    /**
     * Met à jour la zone d'affichage des résultats en fonction des résultats de l'algorithme.
     */
    private void updateZoneAffichageResultats() {
        if (data.tsp == null) {
            infoDisplayArea.setText(""); // Effacer le texte
            return;
        }

        String displayHtml = data.tsp.presentationResultatsHTML();

        // Mise à jour efficace du contenu
        SwingUtilities.invokeLater(() -> {
            infoDisplayArea.setText(displayHtml); // Mise à jour du contenu
        });
    }
}
