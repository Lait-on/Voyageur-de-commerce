package Affichage;

import javax.swing.*;

import ParametresHandler.ParametresHandler;

import java.awt.*;
import java.util.ArrayList;

/**
 * La classe {@code FenetreModificationParametre} représente une fenêtre modale permettant
 * de modifier les paramètres d'un algorithme via une interface graphique.
 * Elle permet de charger, afficher et mettre à jour les paramètres modifiables de l'algorithme.
 * 
 * @author [Votre Nom]
 */
public class FenetreModificationParametre extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    /** Panel contenant les éléments de l'interface pour modifier les paramètres. */
    private JPanel contentPanel;

    /**
     * Constructeur de la fenêtre de modification des paramètres.
     */
    public FenetreModificationParametre() {
        setTitle("Modification Parametres");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Initialisation du Panel principal pour les paramètres
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(contentPanel));
    }

    /**
     * Charge les parametre avec ParametresHandler.
     * 
     * @param ParametresHandler les parametres
     * @throws IllegalArgumentException si parametresHandler est null.
     */
    public void loadParameters(ParametresHandler parametresHandler) {
        contentPanel.removeAll();

        if (parametresHandler == null) {
  
            contentPanel.add(new JLabel("Cet algorithme n'a pas de paramètres modifiables."));
            
        } else {
            ArrayList<String> nomsParamtres = parametresHandler.getNomsParametres();
            ArrayList<Class<?>> typesParametres = parametresHandler.getTypesParametres();

            // Création des champs de saisie pour chaque paramètre
            for (int i = 0; i < nomsParamtres.size(); i++) {
                String nomParam = nomsParamtres.get(i);
                @SuppressWarnings("unused")
				Class<?> typeParam = typesParametres.get(i);

                // Panel pour chaque paramètre
                JPanel paramPanel = new JPanel(new BorderLayout());
                JLabel paramLabel = new JLabel(nomParam + ":");

                try {
                    Object currentValue = parametresHandler.getValeurParametre(nomParam);
                    JTextField textField = new JTextField(currentValue.toString());
                    textField.putClientProperty("nomParam", nomParam);
                    paramPanel.add(textField, BorderLayout.CENTER);
                    paramPanel.add(paramLabel, BorderLayout.WEST);
                    contentPanel.add(paramPanel);

                } catch (Exception e) {
                    contentPanel.add( new JLabel("Erreur: Impossible d'afficher le paramètre " + nomParam));
                }
            }
        }

        // Recalcul de la disposition après modification du contenu
        revalidate();
        repaint();
    }

    /**
     * Met à jour les valeurs des paramètres dans le gestionnaire de paramètres en fonction
     * des saisies de l'utilisateur.
     * 
     * @param parametersHandler Le gestionnaire de paramètres à mettre à jour.
     */
    public void updateParametres(ParametresHandler parametersHandler) {
        if (parametersHandler != null) {
            // Récupération des noms et types des paramètres
            ArrayList<String> paramNames = parametersHandler.getNomsParametres();
            ArrayList<Class<?>> paramTypes = parametersHandler.getTypesParametres();

            // Mise à jour des valeurs en fonction des champs modifiés
            for (int i = 0; i < paramNames.size(); i++) {
                String paramName = paramNames.get(i);
                Class<?> paramType = paramTypes.get(i);

                // Parcours des composants de la fenêtre pour récupérer les nouvelles valeurs
                for (Component component : contentPanel.getComponents()) {
                    if (component instanceof JPanel) {
                        JPanel paramPanel = (JPanel) component;
                        for (Component innerComponent : paramPanel.getComponents()) {
                            // Récupération du nom du paramètre associé à ce composant
                            String storedParamName = (String) ((JComponent) innerComponent).getClientProperty("nomParam");

                            // Si le nom du paramètre correspond, mettre à jour la valeur
                            if (paramName.equals(storedParamName)) {
                                if (innerComponent instanceof JTextField) {
                                    JTextField textField = (JTextField) innerComponent;
                                    String inputText = textField.getText();
                                    try {
                                        Object value = null;

                                        // Conversion de la saisie selon le type du paramètre
                                        if (paramType == Integer.class) {
                                            value = Integer.parseInt(inputText);
                                        } else if (paramType == Double.class) {
                                            value = Double.parseDouble(inputText);
                                        } else {
                                            value = inputText;
                                        }

                                        // Mise à jour du paramètre dans le gestionnaire
                                        parametersHandler.setValeurParametre(paramName, value);
                                    } catch (NumberFormatException ex) {
                                        // Affichage d'un message d'erreur en cas de format invalide
                                        JOptionPane.showMessageDialog(this, 
                                            "Erreur lors de la mise à jour du paramètre " + paramName + ": " + ex.getMessage(),
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
