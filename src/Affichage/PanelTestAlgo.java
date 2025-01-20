package Affichage;

import javax.swing.*;
import SQL.Ville;
import SQL.VilleUtils;
import TriCycleHamiltonien.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de tester différents algorithmes de tri liés au problème du voyageur de commerce.
 * Cette classe génère une interface utilisateur pour sélectionner un tri, 
 *  selectionner un algorithme, modifier ses paramètres si nécessaire et visualiser les résultats dans un panel dédié.
 */ 
public class PanelTestAlgo extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Liste des classes de tri disponibles. Chaque classe doit implémenter des méthodes statiques 
     * comme {@code GetName}, {@code Getter}, et {@code Parameter} pour interagir dynamiquement.
     */
    private static final ArrayList<Class<? extends Tri>> TypesTri = new ArrayList<>();

    static {
        TypesTri.add(TriAge.class);
        TypesTri.add(TriCheminTitulaire.class);
        TypesTri.add(TriDomaine.class);
        TypesTri.add(TriRegion.class);
        TypesTri.add(TriType.class);
    }

    // Composants principaux de l'interface utilisateur
    /**Panel affichant les résultats des algorithmes*/
    private PanelAlgo panelAlgo; 
    /**Menu déroulant pour choisir un tri*/
    private JComboBox<String> cycleSelectionBox; 
    /**Bouton pour modifier les paramètres du tri sélectionné*/
    private JButton modifyCycleParamsButton; 

    /**
     * Constructeur de la classe PanelTestAlgo.
     * Initialise les composants graphiques et configure les interactions utilisateur.
     * 
     * @throws Exception en cas d'erreur lors de l'initialisation des composants dynamiques.
     */
    public PanelTestAlgo() throws Exception {
        setLayout(new BorderLayout(10, 10));
        panelAlgo = new PanelAlgo();

        // Titre principal
        JLabel titleLabel = new JLabel("Test des Algorithmes", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Conteneur principal pour les contrôles
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label pour la sélection de tri
        JLabel cycleLabel = new JLabel("Sélectionnez un tri pour le cycle:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(cycleLabel, gbc);

        // Menu déroulant pour choisir un tri
        cycleSelectionBox = new JComboBox<>();
        setupChoixTri(cycleSelectionBox);
        cycleSelectionBox.addItemListener(e -> updateModifyCycleButton());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(cycleSelectionBox, gbc);

        // Bouton pour modifier les paramètres du tri sélectionné
        modifyCycleParamsButton = new JButton("Modifier les paramètres du tri");
        updateModifyCycleButton();
        modifyCycleParamsButton.addChangeListener(e -> updateAlgoPanelVille());
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        contentPanel.add(modifyCycleParamsButton, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Panel pour afficher les résultats des algorithmes
        add(panelAlgo, BorderLayout.SOUTH);
    }

    /**
     * Initialise les éléments du menu déroulant avec les noms des tris disponibles.
     * Chaque nom est obtenu dynamiquement via la méthode statique {@code GetName()}.
     *
     * @param box Le menu déroulant à remplir.
     */
    private void setupChoixTri(JComboBox<String> box) {
        for (Class<? extends Tri> tri : TypesTri) {
            try {
                String name = (String) tri.getMethod("GetName").invoke(null);
                box.addItem(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Met à jour le comportement du bouton de modification des paramètres
     * en fonction du tri sélectionné dans le menu déroulant.
     */
    private void updateModifyCycleButton() {
        int selectedTri = cycleSelectionBox.getSelectedIndex();

        // Retirer tous les ActionListener existants pour éviter des doublons
        for (ActionListener al : modifyCycleParamsButton.getActionListeners()) {
            modifyCycleParamsButton.removeActionListener(al);
        }

        // Ajouter un nouvel ActionListener pour ouvrir les paramètres du tri sélectionné
        modifyCycleParamsButton.addActionListener(e -> {
            try {
                TypesTri.get(selectedTri).getMethod("Parameter").invoke(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Mettre à jour l'affichage des villes
        updateAlgoPanelVille();
    }

    /**
     * Met à jour le panel contenant les données des villes en fonction du tri sélectionné.
     * Les données sont obtenues dynamiquement via la méthode {@code Getter()} de la classe de tri.
     */
    private void updateAlgoPanelVille() {
        int selectedCycle = cycleSelectionBox.getSelectedIndex();
        Ville[] villes = null;

        try {
            @SuppressWarnings("unchecked")
            List<Ville> ville = (List<Ville>) TypesTri
                    .get(selectedCycle)
                    .getMethod("Getter")
                    .invoke(TypesTri.get(selectedCycle).getConstructor().newInstance());

            villes = ville.toArray(new Ville[0]);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        if (villes != null && panelAlgo != null) {

            panelAlgo.setVillesProbleme(VilleUtils.filtrerVillesUnique(villes));
        }
    }
}
