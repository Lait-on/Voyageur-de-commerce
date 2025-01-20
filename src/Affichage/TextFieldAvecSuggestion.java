package Affichage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un champ de texte avec suggestions dynamiques basées sur l'entrée utilisateur.
 * Cette classe étend {@link JTextField} et propose un menu déroulant contenant des suggestions
 * basées sur une liste d'éléments et la proximité avec le texte entré.
 */
public class TextFieldAvecSuggestion extends JTextField {

    private static final long serialVersionUID = 1L;
    
    // Le menu déroulant pour afficher les suggestions
    private JPopupMenu suggestionsMenu;
    
    // La liste des suggestions disponibles
    private List<String> suggestions;
    
    // Référence à cette instance de TextFieldAvecSuggestion
    private TextFieldAvecSuggestion self;

    /**
     * Constructeur pour initialiser le champ de texte avec un nombre de colonnes et une liste de suggestions.
     * @param columns Le nombre de colonnes du champ de texte.
     * @param suggestions La liste des suggestions à utiliser.
     */
    public TextFieldAvecSuggestion(int columns, List<String> suggestions) {
        super(columns);
        this.suggestions = suggestions;
        this.self = this;
        suggestionsMenu = new JPopupMenu();
        setupSuggestionListener();
    }

    /**
     * Configure un écouteur de document pour mettre à jour les suggestions à chaque modification du texte.
     */
    private void setupSuggestionListener() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                afficheSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                afficheSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                afficheSuggestions();
            }
            
            /**
             * Affiche les suggestions en fonction de l'entrée actuelle de l'utilisateur.
             */
            private void afficheSuggestions() {
                // Obtenir l'entrée actuelle
                String entreeUtilisateur = getText().trim().toUpperCase();
                suggestionsMenu.removeAll(); // clear les anciennes suggestions
                
                if (!entreeUtilisateur.isEmpty()) {
                    List<String> sugg = new ArrayList<>();
                    genere_suggestion(sugg, suggestions, entreeUtilisateur);
                    for (int i = 0; i < Math.min(sugg.size(), 5); i++) {
                        String suggestion = sugg.get(i);
                        
                        JMenuItem item = new JMenuItem(suggestion);
                        item.addActionListener(e -> {
                            // Met à jour le texte sans perdre le focus
                            setText(suggestion);
                            suggestionsMenu.setVisible(false); // Cache les suggestions après sélection
                        });
                        suggestionsMenu.add(item);
                    }
                    suggestionsMenu.revalidate();
                    suggestionsMenu.repaint();
                    
                    if (suggestionsMenu.getComponentCount() > 0) {
                        SwingUtilities.invokeLater(() -> {
                            // Affiche le menu sous le champ texte
                            suggestionsMenu.show(self, 0, getHeight());
                            requestFocusInWindow(); // Restaure le focus sur le champ texte
                        });
                    } else {
                        suggestionsMenu.setVisible(false); // Cache si aucune suggestion
                    }
                } else {
                    suggestionsMenu.setVisible(false); // Cache si l'entrée est vide
                }
            }
        });
    }

    /**
     * Génère une liste de suggestions triée en fonction de la distance de Levenshtein entre l'entrée de l'utilisateur et les suggestions.
     * @param sugg Liste de suggestions à remplir avec les suggestions générées.
     * @param suggestions Liste de toutes les suggestions disponibles.
     * @param input Texte entré par l'utilisateur à comparer avec les suggestions.
     */
    private void genere_suggestion(List<String> sugg, List<String> suggestions, String input) {
        List<Integer> distance = new ArrayList<>();
        List<Integer> classement = new ArrayList<>();
        for (String suggestion : suggestions) {
            distance.add(distance_Levenshtein(suggestion, input));
        }
        for (int j = 0; j < suggestions.size(); j++) {
            if (classement.size() == 0) {
                classement.add(j);
            } else {
                int i = classement.size() - 1;
                while (i >= 0 && distance.get(j) < distance.get(classement.get(i))) {
                    i -= 1;
                }
                classement.add(i + 1, j);
            }
            if (classement.size() > 5) {
                classement.remove(5);
            }
        }
        for (int i : classement) {
            sugg.add(suggestions.get(i));
        }
    }
    
    /**
     * Calcule la distance de Levenshtein entre deux chaînes de caractères.
     * Cette méthode mesure le nombre minimal d'opérations nécessaires pour transformer une chaîne en une autre.
     * @param a Première chaîne de caractères.
     * @param b Deuxième chaîne de caractères.
     * @return La distance de Levenshtein entre les deux chaînes.
     */
    private int distance_Levenshtein(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        
        int[][] distance = new int[lenA + 1][lenB + 1];
        
        for (int i = 0; i <= lenA; i++) {
            for (int j = 0; j <= lenB; j++) {
                if (i == 0) {
                    distance[i][j] = j;
                } else if (j == 0) {
                    distance[i][j] = i;
                } else {
                    int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                    distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), distance[i - 1][j - 1] + cost);
                    
                }
                
            }
            
        }
        int penalty = lenA < 8 ? 7 - lenA : 0;
        return distance[lenA][lenB] + penalty;
    }
    
    /**
     * Met à jour la liste des suggestions.
     * @param suggestions La nouvelle liste des suggestions.
     */
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
