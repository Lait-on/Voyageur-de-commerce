package Affichage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;

import Data.data;
import SQL.Java_to_SQL;

/**
 * Classe {@code InitSQL} représentant une boîte de dialogue permettant
 * l'initialisation des paramètres de connexion à une base de données relationnelle (BDR).
 * L'utilisateur peut entrer l'URL, le nom d'utilisateur et le mot de passe pour configurer la connexion.
 */
public class InitSQL extends JDialog {

    private static final long serialVersionUID = 1L;
 
    /**
     * Constructeur de la classe {@code InitSQL}.
     * Initialise la boîte de dialogue avec des champs de saisie pour l'URL, l'utilisateur et le mot de passe,
     * ainsi qu'un bouton pour valider la connexion.
     */
    public InitSQL() {
        // Configuration de la fenêtre principale
        setTitle("Initialisation de la BDR");
        ImageIcon icon = new ImageIcon("assets/PGAdminLOGO.png");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setModal(true);

        // Panel principal contenant tous les éléments
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Champs de texte pour URL, utilisateur et mot de passe
        JTextField urlField = new JTextField(20);
        JTextField usrField = new JTextField(20);
        JTextField passwrdField = new JTextField(20);

        // Création et ajout des Panelx pour chaque champ de saisie
        JPanel urlPanel = creationZoneTexte("URL: ", urlField);
        JPanel userPanel = creationZoneTexte("User: ", usrField);
        JPanel passwrdPanel = creationZoneTexte("Password: ", passwrdField);
        mainPanel.add(urlPanel);
        mainPanel.add(userPanel);
        mainPanel.add(passwrdPanel);

        // Bouton pour valider la connexion
        JButton validerButton = new JButton("Valider");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(validerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        Java_to_SQL SQL = data.SQL;

        // Action associée au bouton "Valider"
        validerButton.addActionListener(e -> {
            String url = urlField.getText();
            String usr = usrField.getText();
            String passwrd = passwrdField.getText();

            if (!url.isEmpty() && !usr.isEmpty() && !passwrd.isEmpty()) {
                if (tryConnection(url, usr, passwrd)) {
                    SQL.Setter(url, usr, passwrd);
                    data.Init_Ville(); // Initialiser les villes après connexion réussie
                    dispose(); // Fermer la boîte de dialogue
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "La connexion n'a pas pu être établie. Vérifiez les informations saisies.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Tous les champs doivent être renseignés.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Rendre la boîte de dialogue visible
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    /**
     * Crée un Panel contenant un label et un champ de texte alignés.
     *
     * @param texte Le texte à afficher dans le label.
     * @param zoneTexte Le composant de saisie associé.
     * @return Un {@code JPanel} contenant le label et le champ de saisie.
     */
    private JPanel creationZoneTexte(String texte, JComponent zoneTexte) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alignement à gauche
        panel.setMaximumSize(new Dimension(600, 40)); // Taille fixe pour homogénéité
        JLabel label = new JLabel(texte);
        label.setPreferredSize(new Dimension(120, 20)); // Alignement visuel des étiquettes
        panel.add(label);
        panel.add(zoneTexte);
        return panel;
    }

    /**
     * Tente d'établir une connexion à une base de données avec les informations fournies.
     *
     * @param url L'URL de la base de données.
     * @param user Le nom d'utilisateur pour la connexion.
     * @param password Le mot de passe pour la connexion.
     * @return {@code true} si la connexion est réussie, {@code false} sinon.
     */
    private Boolean tryConnection(String url, String user, String password) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
