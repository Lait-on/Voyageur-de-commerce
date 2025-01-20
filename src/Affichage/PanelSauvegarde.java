package Affichage;

import Data.data;
import java.io.*;
import ecosysteme.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import java.awt.*;

/**
 * PanelSauvegarde est une classe qui gère l'interface utilisateur pour sauvegarder ou récupérer les données de l'écosystème.
 * Propose deux modes : "Sauvegarde" et "Récupération".
 */
public class PanelSauvegarde extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut du PanelSauvegarde.
     * Initialise l'interface graphique pour la gestion des sauvegardes.
     */
    public PanelSauvegarde() {
        setLayout(new BorderLayout(10, 10)); 

        // Panel supérieur avec le titre et le bouton de bascule (Sauvegarde/Récupération)
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("Création d'une Sauvegarde");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);  
        headerPanel.add(titleLabel, BorderLayout.CENTER); 

        // Bouton de bascule Sauvegarde/Récupération
        JButton modeButton = new JButton("Récupération");
        modeButton.setMaximumSize(new Dimension(100, 10));  
        modeButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
        headerPanel.add(modeButton, BorderLayout.EAST); 
        add(headerPanel, BorderLayout.NORTH);  

        // Panel central pour le formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); 
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        // Champ texte pour le nom du fichier
        JLabel label = new JLabel("Nom du fichier :");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT); 
        formPanel.add(label);
        formPanel.add(Box.createVerticalStrut(10)); 

        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(300, 30)); 
        textField.setAlignmentX(Component.CENTER_ALIGNMENT); 
        formPanel.add(textField);
        formPanel.add(Box.createVerticalStrut(10)); 

        // Bouton "Enregistrer"
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 12)); 
        saveButton.setMaximumSize(new Dimension(120, 30)); 
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(saveButton);
        formPanel.add(Box.createVerticalStrut(10)); 

        // ComboBox pour la liste des fichiers sauvegardés
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setMaximumSize(new Dimension(300, 30));

        // Chargement initial des fichiers dans la ComboBox
        updateCombobox(comboBox);

        // Action : Basculer entre "Sauvegarde" et "Récupération"
        modeButton.addActionListener(e -> {
            if (modeButton.getText().equals("Récupération") && comboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Pas de fichiers sauvegardés disponibles.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else if (modeButton.getText().equals("Récupération")) {
                modeButton.setText("Sauvegarde");
                titleLabel.setText("Récupération d'une Sauvegarde");
                formPanel.remove(saveButton);
                formPanel.remove(textField);
                formPanel.add(comboBox);
                formPanel.add(saveButton);
            } else {
                modeButton.setText("Récupération");
                titleLabel.setText("Création d'une Sauvegarde");
                formPanel.remove(saveButton);
                updateCombobox(comboBox);
                formPanel.remove(comboBox);
                formPanel.add(textField);
                formPanel.add(saveButton);
            }
            formPanel.revalidate(); 
            formPanel.repaint(); 
        });

        // Action : Enregistrer ou Récupérer selon le mode
        saveButton.addActionListener(e -> {
            if (modeButton.getText().equals("Récupération")) {
                String nomFichier = textField.getText().trim();
                if (nomFichier.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom du fichier ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if (nomFichier.contains("/") || nomFichier.contains("\\") || nomFichier.contains(":")) {
                    JOptionPane.showMessageDialog(this, "Le nom du fichier contient des caractères invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    Sauvegarde(nomFichier);
                }
            } else {
                String selectedFile = (String) comboBox.getSelectedItem();
                if (selectedFile != null) {
                    Recuperation(selectedFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier à récupérer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Met à jour les éléments de la JComboBox avec la liste des fichiers sauvegardés.
     *
     * @param comboBox La JComboBox à mettre à jour.
     */
    private void updateCombobox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        List<String> fichiers = ListeNomFichier();
        if (fichiers != null) {
            fichiers.forEach(comboBox::addItem);
        }
    }

    /**
     * Sauvegarde les données dans un fichier.
     *
     * @param nomFichier Le nom du fichier de sauvegarde.
     */
    private void Sauvegarde(String nomFichier) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("./GenerateFiles/Save/" + nomFichier + ".mp3"))) {
            for (Chercheur c : data.chercheurs) stream.writeObject(c);
            for (MCF m : data.MCFs) stream.writeObject(m);
            for (Etudiant e : data.etudiants) stream.writeObject(e);
            JOptionPane.showMessageDialog(this, "La sauvegarde a bien été effectué.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            this.removeAll();
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Récupère les données depuis un fichier.
     *
     * @param nomFichier Le nom du fichier à récupérer.
     */
    private void Recuperation(String nomFichier) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("./GenerateFiles/Save/" + nomFichier + ".mp3"))) {
        	
        	data.chercheurs.clear();
        	data.etudiants.clear();
        	data.MCFs.clear();

            List<Chercheur> tempC = new ArrayList<>();
            List<MCF> tempM = new ArrayList<>();
            List<Etudiant> tempE = new ArrayList<>();
            while (true) {
                try {
                    Object obj = stream.readObject();
                    if (obj instanceof Chercheur) tempC.add((Chercheur) obj);
                    else if (obj instanceof MCF) tempM.add((MCF) obj);
                    else if (obj instanceof Etudiant) tempE.add((Etudiant) obj);
                } catch (EOFException e) {
                    data.chercheurs.addAll(tempC);
                    data.MCFs.addAll(tempM);
                    data.etudiants.addAll(tempE);
                    break;
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(this, "Classe inconnue lors de la récupération.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, "La sauvegarde a bien été récupéré.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            this.removeAll();
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Liste les noms des fichiers de sauvegarde disponibles dans le dossier GenerateFiles/Save.
     *
     * @return Une liste des noms de fichiers sauvegardés, sans l'extension.
     */
    private List<String> ListeNomFichier() {
        File repertoire = new File("./GenerateFiles/Save/");
        if (repertoire.exists() && repertoire.isDirectory()) {
            return Arrays.stream(Objects.requireNonNull(repertoire.list()))
                    .filter(file -> file.endsWith(".mp3"))
                    .map(file -> file.replace(".mp3", ""))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
