package Affichage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.*;

import javax.swing.*;

import Data.data;
import ecosysteme.*;
import SQL.Ville;

/**
 * PannelModifEcosysteme est un Panel d'interface utilisateur permettant de gérer la modification, l'ajout et la suppression de titulaires et d'élèves.
 * Il fournit des fonctionnalités de modification des informations des titulaires, de suppression d'individus, et de mise à jour de la base de données.
 */
public class PanelModifEcosysteme extends JPanel{

	private static final long serialVersionUID = 1L;
	/** Indicateur de type de titulaire (0 pour MCF, 1 pour Chercheur)*/
	private int Type =0; 
	
	public PanelModifEcosysteme() {
		

		// Boutons
        JButton newEleve = new JButton("Nouvel élève");
        JButton newTitulaire = new JButton("Nouveau titulaire");
        JButton InitSQL = new JButton("Actualiser la Base de Données");
        JButton SupprPersonne = new JButton("Supprimer une personne");
        JButton ModifPersonne = new JButton("Modifier une personne");

        // Dimension des boutons
        newEleve.setPreferredSize(new Dimension(200, 45)); 
        newEleve.setFont(new Font("Arial", Font.BOLD, 15));
        newTitulaire.setPreferredSize(new Dimension(200, 45));
        newTitulaire.setFont(new Font("Arial", Font.BOLD, 15));
        InitSQL.setPreferredSize(new Dimension(170, 35));
        InitSQL.setFont(new Font("Arial", Font.BOLD, 9));
        SupprPersonne.setPreferredSize(new Dimension(170, 35));
        SupprPersonne.setFont(new Font("Arial", Font.BOLD, 9));
        ModifPersonne.setPreferredSize(new Dimension(170, 35));
        ModifPersonne.setFont(new Font("Arial", Font.BOLD, 9));

        
        
        // Panels secondaires
        JPanel PanelMain = new JPanel();
        JPanel PanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JPanel PanelBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 3));

        // Ajout des boutons dans les sous-Panelx
        PanelTop.add(newEleve);
        PanelTop.add(newTitulaire);
        PanelBot.add(ModifPersonne);
        PanelBot.add(InitSQL);
        PanelBot.add(SupprPersonne);

        // Organisation du PanelMain
        PanelMain.setLayout(new BoxLayout(PanelMain, BoxLayout.Y_AXIS));
        PanelMain.add(PanelTop);
        PanelMain.add(PanelBot);

        // Panel principal avec centrage
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(-150, 0, 0, 0);
        
        // Ajout de PanelMain
        add(PanelMain, gbc);
		
		newEleve.addActionListener(e ->{
			this.EleveModif(0, null);
		});
		
		newTitulaire.addActionListener(e ->{
			this.TitulaireModif(0, null);
		});
		
		InitSQL.addActionListener(e ->{
			new InitSQL();
		});
		
		SupprPersonne.addActionListener(e ->{
			this.SupprPersonne();
		});
		
		ModifPersonne.addActionListener(e ->{
			this.ModificationPersonne();
		});
	}
	
	/**
     * Permet de modifier les informations d'une personne (chercheur, MCF, ou étudiant).
     * Affiche un formulaire pour sélectionner et modifier un individu existant.
     */
	private void ModificationPersonne() {
		this.removeAll();
	    this.revalidate();
	    this.repaint();
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    
	    DefaultComboBoxModel<String> choixIndividu = new DefaultComboBoxModel<>(new String[]{});
	    JComboBox<String> IndividuBox = new JComboBox<>(choixIndividu);
	    setupIndividuBox(choixIndividu);
	    add(creationZoneTexte("Individu à modifier :", IndividuBox));
	    
	    JToggleButton BouttonSuiv = new JToggleButton("Valider");
	    add(BouttonSuiv);
	    BouttonSuiv.addActionListener(e -> {
	    	ModificationData(IndividuBox);
	    	data.updateVilleCarteEcosysteme();
	    });
		
	}
	
	/**
     * Applique les modifications aux données de l'individu sélectionné dans le combo box.
     * @param box ComboBox contenant la liste des individus à modifier.
     */
	private void ModificationData(JComboBox<String> box) {
		int indice = box.getSelectedIndex();
		if(indice < data.chercheurs.size()) {
			modifChercheur(data.chercheurs.get(indice));
		}
		else if(indice < data.chercheurs.size() + data.MCFs.size()) {
			modifMCFs(data.MCFs.get(indice - data.chercheurs.size()));
		}
		else {
			modifEtudiant(data.etudiants.get(indice - (data.chercheurs.size() + data.MCFs.size())));
		}
		
	}

	private void modifEtudiant(Etudiant etudiant) {
		EleveModif(1, etudiant);
		
	}

	private void modifMCFs(MCF mcf) {
		Type = 0;
		TitulaireModif(1, mcf);
		
	}

	private void modifChercheur(Chercheur chercheur) {
		Type = 1;
		TitulaireModif(1, chercheur);
	}

	/**
     * Permet de supprimer un individu de la base de données.
     * Affiche un formulaire pour sélectionner et supprimer un individu existant.
     */
	private void SupprPersonne() {
		this.removeAll();
	    this.revalidate();
	    this.repaint();
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    
	    DefaultComboBoxModel<String> choixIndividu = new DefaultComboBoxModel<>(new String[]{});
	    JComboBox<String> IndividuBox = new JComboBox<>(choixIndividu);
	    setupIndividuBox(choixIndividu);
	    add(creationZoneTexte("Individu à supprimer :", IndividuBox));
	    
	    JToggleButton BouttonValider = new JToggleButton("Valider");
	    add(BouttonValider);
	    BouttonValider.addActionListener(e -> {
	    	SuppressionData(IndividuBox);
	    	data.updateVilleCarteEcosysteme();
	    });
	    
	}

	/**
     * Supprime les données d'un individu sélectionné dans le combo box.
     * @param box ComboBox contenant la liste des individus à supprimer.
     */
	private void SuppressionData(JComboBox<String> box) {
		int indice = box.getSelectedIndex();
		if(indice < data.chercheurs.size()) {
			Chercheur c = data.chercheurs.get(indice);
			if(c.getEtudiants()!=null && !c.getEtudiants().isEmpty()) {
				for(Etudiant e: c.getEtudiants()) {
					e.suppr_titulaire();
				}
			}
			data.chercheurs.remove(indice);
			
		} else if (indice < data.chercheurs.size() + data.MCFs.size()) {
			MCF m = data.MCFs.get(indice - data.chercheurs.size());
			if(m.getEtudiant()!=null) {m.getEtudiant().suppr_titulaire();}	
			data.MCFs.remove(indice - data.chercheurs.size());
			
		} else {
			Etudiant e = data.etudiants.get(indice - (data.chercheurs.size() + data.MCFs.size()));
			if(e.getTitulaire()!=null) {e.getTitulaire().supprEtudiant(e);}
			data.etudiants.remove(indice - (data.chercheurs.size() + data.MCFs.size()));
		}
		JOptionPane.showMessageDialog(this, "L'individu a bien été supprimé", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
		this.removeAll();
	    this.revalidate();
	    this.repaint();
	}
	
	
	/**
     * Remplit la liste des individus dans le combo box pour la modification ou la suppression.
     * @param box ComboBox à remplir avec les individus.
     * @return true si des individus ont été ajoutés, false sinon.
     */
	private Boolean setupIndividuBox(DefaultComboBoxModel<String> box) {
		if(data.chercheurs.isEmpty() && data.MCFs.isEmpty() && data.etudiants.isEmpty()) {
			return false;
		}
		else {
			for(Chercheur c: data.chercheurs) {
				if(c!=null) {box.addElement(c.getPrenom() + " " + c.getNom());}
			}
			for(MCF mcf: data.MCFs) {
				if(mcf!=null) {box.addElement(mcf.getPrenom() + " " + mcf.getNom());}
			}
			for(Etudiant e: data.etudiants) {
				if(e!=null) {box.addElement(e.getPrenom() + " " + e.getNom());}
			}
			return true;
		}
		
	}
	
	/**
	 * Méthode permettant de créer et gérer la modification d'un titulaire (MCF ou Chercheur) en fonction du type de modification demandé.
	 * Cette méthode ajoute tous les composants nécessaires pour modifier ou ajouter un titulaire, ainsi que pour valider les informations.
	 *
	 * @param TypeModif Le type de modification (0 pour ajout, 1 pour modification).
	 * @param titulaire Le titulaire à modifier (si TypeModif est 1), ou null si TypeModif est 0 (ajout).
	 */
	private void TitulaireModif(int TypeModif, Titulaire titulaire){
		this.removeAll();
	    this.revalidate();
	    this.repaint();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//Création des components

			//Type de Titulaire
		if(TypeModif == 0) {
			JToggleButton ChoixTypeTitulaire = new JToggleButton("MCF");
			JPanel PanelType = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			PanelType.add(ChoixTypeTitulaire);
		    PanelType.setMaximumSize(new Dimension(400, 40));
			add(PanelType);
			
			ChoixTypeTitulaire.addActionListener(e -> {
	            if (ChoixTypeTitulaire.isSelected()) {
	            	ChoixTypeTitulaire.setText("Chercheur");
	                Type = 1;
	            } else {
	            	ChoixTypeTitulaire.setText("MCF");
	            	Type = 0;
	            }
	            data.updateVilleCarteEcosysteme();
	        });
		}

			// Nom
	    JTextField nomField = new JTextField(20);
	    add(creationZoneTexte("Nom :", nomField));

	    	// Prénom
	    JTextField prenomField = new JTextField(20);
	    add(creationZoneTexte("Prénom :", prenomField));

	    	// Numéro de bureau
	    JTextField numBureau = new JTextField(20);
	    add(creationZoneTexte("Numéro de Bureau :", numBureau));


			// Discipline(s)
		JComboBox<String> box1 = new JComboBox<>();
		JComboBox<String> box2 = new JComboBox<>();	
		JPanel disciplinePanel = creationZoneBox(box1,  box2);
		add(disciplinePanel);


			// Âge
	    JComboBox<Integer> ageBox = new JComboBox<>();
	    for (int i = 0; i <= 99; i++) ageBox.addItem(i);
	    add(creationZoneTexte("Âge :", ageBox));

	    
	    TextFieldAvecSuggestion texteVille = new TextFieldAvecSuggestion(20, new ArrayList<>(data.villes.stream().map(Ville::getNomDepartement).toList()));
	    add(creationZoneTexte("Ville :", texteVille));

			// Étudiant
	    JButton etudiants = new JButton("Liste d'étudiant");
	    JPanel buttonEtudiantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    buttonEtudiantPanel.add(etudiants);
	    buttonEtudiantPanel.setMaximumSize(new Dimension(600, 40));
	    add(buttonEtudiantPanel);

			// Bouton Enregistrer
	    JButton saveButton = new JButton("Enregistrer");
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    buttonPanel.add(saveButton);
	    add(buttonPanel);

	    // Remplissage des components si modification
	    if (TypeModif==1) {SetDefaultValuesT(titulaire, nomField, prenomField, numBureau, box1, box2, ageBox, texteVille);}
	    
	    
		//ajout des actions listener
		

	    List<Etudiant> Liste_Etudiant = new ArrayList<>();
	    Liste_Etudiant.add(null);
	    etudiants.addActionListener(e-> {
	    	if (Type==1) {
	    		Liste_Etudiant.clear();
	    		afficherEtudiantsSelectionChercheur(Liste_Etudiant);
	    	}
	    	else {
	    		Liste_Etudiant.clear();
	    		afficherEtudiantSelectionMCF(Liste_Etudiant);
	    	}
	    	data.updateVilleCarteEcosysteme();
		});

	    saveButton.addActionListener(e -> {
			List<String> liste_ville = data.villes.stream().map(Ville::getNomDepartement).toList();
			String nom = nomField.getText();
			String prenom = prenomField.getText();
			Set<Discipline> disciplines = getDiscipline(box1,box2);
			int age = (int) ageBox.getSelectedItem();
			String ville = (String) texteVille.getText();
			
			String numeroBureau = (String) numBureau.getText();
			
			
			if (nom.isEmpty() || prenom.isEmpty() || age == 0 || !liste_ville.contains(ville) || !isInteger(numBureau.getText())) {
				JOptionPane.showMessageDialog(this, "Un champ n'est pas renseigné ou incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
			else {
				if(TypeModif == 1) {
					if (Type==1) {
						Chercheur Chercheur = (Chercheur) titulaire;
						Set<Etudiant> SetEtudiants = new HashSet<>();
						for(Etudiant etu: Liste_Etudiant) {if(etu!=null) {SetEtudiants.add(etu);}}
						Chercheur.modif(nom, prenom, age, data.getVille(ville), disciplines, Integer.parseInt(numeroBureau.trim()) , SetEtudiants);
					}
					else {
						MCF mcf = (MCF) titulaire;
						mcf.modif(nom, prenom, age, data.getVille(ville), disciplines, Integer.parseInt(numeroBureau.trim()) , Liste_Etudiant.get(0));
					}
					JOptionPane.showMessageDialog(this, "L'individu a bien été modifié", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
				}
				
				else {ajoutTitulaire(nom, prenom, age, ville, disciplines, numeroBureau, Liste_Etudiant);}
				
				
				this.removeAll();
				data.updateVilleCarteEcosysteme();
				this.revalidate();
				this.repaint();		
			}	    
		});

	}
	
	/**
	 * Cette méthode permet de remplir les champs d'un formulaire avec les valeurs du titulaire (MCF ou Chercheur).
	 * Elle est utilisée pour la modification d'un titulaire, en initialisant les champs avec ses données actuelles.
	 *
	 * @param titulaire Le titulaire (MCF ou Chercheur) dont les informations doivent être récupérées et affichées dans le formulaire.
	 * @param nomField Champ de texte pour le nom du titulaire.
	 * @param prenomField Champ de texte pour le prénom du titulaire.
	 * @param numBureau Champ de texte pour le numéro de bureau du titulaire.
	 * @param box1 JComboBox pour la première discipline (si applicable).
	 * @param box2 JComboBox pour la deuxième discipline (si applicable).
	 * @param ageBox JComboBox pour l'âge du titulaire.
	 * @param texteVille Champ de texte pour la ville du titulaire.
	 */
	private void SetDefaultValuesT(Titulaire titulaire, JTextField nomField, JTextField prenomField, JTextField numBureau, JComboBox<String> box1, JComboBox<String> box2,JComboBox<Integer> ageBox,JTextField texteVille ) {
		if(Type == 0) {MCF mcf = (MCF) titulaire; if(mcf.getEtudiant()!=null) { mcf.getEtudiant().change_titulaire(null);}}
		else {Chercheur c = (Chercheur) titulaire; for(Etudiant e: c.getEtudiants()) {e.change_titulaire(null);}}
		
		nomField.setText(titulaire.getNom());
		prenomField.setText(titulaire.getPrenom());
		numBureau.setText(titulaire.getNumBureau());
		texteVille.setText(titulaire.getVilleNom());
		ageBox.setSelectedIndex(Integer.valueOf(titulaire.getAge())-1);
		data.updateVilleCarteEcosysteme();
	}
	
	
	/**
	 * Cette méthode crée et retourne un Panel (JPanel) contenant deux JComboBox pour sélectionner des disciplines.
	 * Elle remplit les JComboBox avec des valeurs prédéfinies et les organise dans un Panel avec un label.
	 * 
	 * @param box1 La première JComboBox pour afficher et sélectionner une discipline.
	 * @param box2 La deuxième JComboBox pour afficher et sélectionner une autre discipline.
	 * @return Le JPanel contenant le label et les deux JComboBox.
	 */
	public JPanel creationZoneBox(JComboBox<String> box1, JComboBox<String> box2){	
		// Remplissage des ComboBox
		DefaultComboBoxModel<String> choixDiscipline = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<String> choixDiscipline2 = new DefaultComboBoxModel<>(new String[]{"Null"});
		setupdisciplinebox(choixDiscipline);
		setupdisciplinebox(choixDiscipline2);
		box1.setModel(choixDiscipline);
		box2.setModel(choixDiscipline2);

		// Création du Panel et Du Label pour structurer la zone des box
		JPanel disciplinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		disciplinePanel.setMaximumSize(new Dimension(600, 40));
		JLabel disciplineLabel = new JLabel("Discipline(s) :");
		disciplineLabel.setPreferredSize(new Dimension(120, 20));
		
		
		disciplinePanel.add(disciplineLabel);
		disciplinePanel.add(box1);
		disciplinePanel.add(box2);
		
		return disciplinePanel;
	}
	
	/**
	 * Cette méthode permet d'ajouter un nouveau titulaire (MCF ou Chercheur) dans la base de données.
	 * Si le type de titulaire est MCF, un MCF est créé et ajouté à la liste des MCFs. 
	 * Si le type est Chercheur, un Chercheur est créé et ajouté à la liste des chercheurs.
	 * 
	 * @param nom Le nom du titulaire.
	 * @param prenom Le prénom du titulaire.
	 * @param age L'âge du titulaire.
	 * @param ville La ville du titulaire.
	 * @param disciplines Un ensemble de disciplines associées au titulaire.
	 * @param numeroBureau Le numéro de bureau du titulaire.
	 * @param Liste_Etudiant La liste d'étudiants associés au titulaire (uniquement pour Chercheur).
	 */
	private void ajoutTitulaire(String nom, String prenom, int age, String ville, Set<Discipline> disciplines, String numeroBureau, List<Etudiant> Liste_Etudiant){
		if(Type == 0) {
			MCF prof = new MCF(nom, prenom, age, data.getVille(ville), disciplines, Integer.parseInt(numeroBureau.trim()), Liste_Etudiant.get(0));
			data.MCFs.add(prof);
			JOptionPane.showMessageDialog(this, "Nouvel Titulaire ajouté : " + prof.getPrenom(), "Confirmation", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			Set<Etudiant> SetEtudiants = new HashSet<>();
			for(Etudiant etu: Liste_Etudiant) {if(etu!=null) {SetEtudiants.add(etu);}}
			Chercheur prof = new Chercheur(nom, prenom,age, data.getVille(ville), disciplines, Integer.parseInt(numeroBureau.trim()), SetEtudiants );
			data.chercheurs.add(prof);
			JOptionPane.showMessageDialog(this, "Nouvel Titulaire ajouté : " + prof.getPrenom(), "Confirmation", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Cette méthode permet de modifier les informations d'un étudiant ou d'ajouter un nouvel étudiant.
	 * Elle affiche un formulaire pour la modification des informations de l'étudiant, y compris son nom, prénom,
	 * sujet de thèse, discipline, âge, année de thèse, ville, et titulaire associé.
	 * 
	 * @param TypeModif Indique si c'est une modification ou un ajout : 1 pour modification, 0 pour ajout.
	 * @param etudiant L'étudiant dont les informations doivent être modifiées (seulement pour la modification).
	 */
	private void EleveModif(int TypeModif, Etudiant etudiant) {
	    this.removeAll();
	    this.revalidate();
	    this.repaint();

	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//Création des components
	    	// Nom
	    JTextField nomField = new JTextField(20);
	    add(creationZoneTexte("Nom :", nomField));

	    	// Prénom
	    JTextField prenomField = new JTextField(20);
	    add(creationZoneTexte("Prénom :", prenomField));

	    	// Énoncé de Thèse
	    JTextField sujetThese = new JTextField(20);
	    add(creationZoneTexte("Enoncé de Thèse :", sujetThese));

	    	// Discipline
	    DefaultComboBoxModel<String> choixDiscipline = new DefaultComboBoxModel<>();
	    JComboBox<String> disciplineBox = new JComboBox<>(choixDiscipline);
	    setupdisciplinebox(choixDiscipline);
	    add(creationZoneTexte("Discipline :", disciplineBox));

	    	// Âge
	    JComboBox<Integer> ageBox = new JComboBox<>();
	    for (int i = 0; i <= 99; i++) ageBox.addItem(i);
	    add(creationZoneTexte("Âge :", ageBox));

	    	// Année de Thèse
	    JComboBox<Integer> debutthese = new JComboBox<>();
	    for (int i = 1; i <= 3; i++) debutthese.addItem(i);
	    add(creationZoneTexte("Année de Thèse :", debutthese));
	    
	    TextFieldAvecSuggestion texteVille = new TextFieldAvecSuggestion(20, new ArrayList<>(data.villes.stream().map(Ville::getNomDepartement).toList()));
	    add(creationZoneTexte("Ville :", texteVille));

	    	// Titulaire
	    DefaultComboBoxModel<String> choixTitulaire = new DefaultComboBoxModel<>(new String[]{"Null"});
	    JComboBox<String> TitulaireBox = new JComboBox<>(choixTitulaire);
	    setupTitulaireBox(choixTitulaire);
	    add(creationZoneTexte("Titulaire :", TitulaireBox));

	    	// Bouton Enregistrer
	    JButton saveButton = new JButton("Enregistrer");
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    buttonPanel.add(saveButton);
	    add(buttonPanel);

		// Remplissage des components si modification
		if(TypeModif ==1){SetDefaultValuesE(etudiant,nomField, prenomField,disciplineBox, texteVille, sujetThese, ageBox, debutthese);}

	    // Ajout de l'ActionListener pour gérer l'enregistrement
	    saveButton.addActionListener(e -> {
	        List<String> liste_ville = data.villes.stream().map(Ville::getNomDepartement).toList();
	        String nom = nomField.getText();
	        String prenom = prenomField.getText();
	        Discipline discipline = getDiscipline(disciplineBox, null).iterator().next();
	        int age = (int) ageBox.getSelectedItem();
	        String ville = texteVille.getText();
	        String Titulaire = (String) TitulaireBox.getSelectedItem();
	        String sujetthese = sujetThese.getText();
	        int anneeThese = (int) debutthese.getSelectedItem();

	        if (nom.isEmpty() || prenom.isEmpty() ||  age == 0 || !liste_ville.contains(ville)) {
	            JOptionPane.showMessageDialog(this, "Un champ n'est pas renseigné.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        } else {
	            
				if(TypeModif == 0){
					Etudiant eleve = new Etudiant(nom, prenom, age, data.getVille(ville), sujetthese, discipline, anneeThese, getTitulaire(Titulaire));
					data.etudiants.add(eleve);
					JOptionPane.showMessageDialog(this, "Nouvel élève ajouté : " + eleve.getPrenom(), "Confirmation", JOptionPane.INFORMATION_MESSAGE);}
				else{
					etudiant.modif(nom, prenom, age, data.getVille(ville), sujetthese, discipline, anneeThese, getTitulaire(Titulaire));
					JOptionPane.showMessageDialog(this, "L'individu a bien été modifié", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
				}
				data.updateVilleCarteEcosysteme();
                this.removeAll();
                this.revalidate();
                this.repaint();
	            
	        }
	    });
	}
	
	/**
	 * Cette méthode permet de remplir les champs du formulaire avec les valeurs actuelles d'un étudiant pour la modification.
	 * Elle réinitialise les informations de l'étudiant dans les champs du formulaire.
	 * 
	 * @param eleve L'étudiant dont les informations doivent être affichées.
	 * @param nomField Le champ de texte pour le nom de l'étudiant.
	 * @param prenomField Le champ de texte pour le prénom de l'étudiant.
	 * @param box1 La JComboBox pour la discipline de l'étudiant.
	 * @param texteVille Le champ de texte pour la ville de l'étudiant.
	 * @param sujetThese Le champ de texte pour le sujet de thèse de l'étudiant.
	 * @param ageBox La JComboBox pour l'âge de l'étudiant.
	 * @param AnneeTheseBox La JComboBox pour l'année de thèse de l'étudiant.
	 */
	private void SetDefaultValuesE(Etudiant eleve, JTextField nomField, JTextField prenomField, JComboBox<String> box1, JTextField texteVille, JTextField sujetThese, JComboBox<Integer> ageBox, JComboBox<Integer> AnneeTheseBox) {	
		if(eleve.getTitulaire()!=null) {eleve.getTitulaire().supprEtudiant(eleve);}
		nomField.setText(eleve.getNom());
		prenomField.setText(eleve.getPrenom());
		texteVille.setText(eleve.getVilleNom());
		ageBox.setSelectedIndex(Integer.valueOf(eleve.getAge()));
		sujetThese.setText(eleve.getSujetDeThese());
		AnneeTheseBox.setSelectedIndex(Integer.valueOf(eleve.getAnneeDeThese())-1);
		box1.setSelectedItem(eleve.getDiscipline());
		
		
	}
	
	/**
	 * Retourne un titulaire (chercheur ou MCF) correspondant à l'input sous forme de prénom et nom.
	 * 
	 * @param input Le nom et prénom du titulaire à rechercher sous forme de chaîne de caractères.
	 * @return Le titulaire correspondant, ou null si aucun titulaire n'est trouvé.
	 */
    private Titulaire getTitulaire(String input) {
    	for(Titulaire chercheur: data.chercheurs) {
    		if (input.equals(chercheur.getPrenom() + " " + chercheur.getNom())){
    			return chercheur;
    		}
    	}
    	for(Titulaire mcf: data.MCFs) {
    		if (input.equals(mcf.getPrenom() + " " + mcf.getNom())){
    			return mcf;
    		}
    	}
    	return null;
    }
	
    /**
     * Retourne un ensemble de disciplines sélectionnées dans deux JComboBox.
     * 
     * @param Box1 Le premier JComboBox contenant les disciplines possibles.
     * @param Box2 Le deuxième JComboBox contenant les disciplines possibles (peut être null).
     * @return Un ensemble de disciplines sélectionnées.
     */
    private Set<Discipline> getDiscipline(JComboBox<String> Box1, JComboBox<String> Box2){ 
    	Set<Discipline> disciplines = new HashSet<>();
    	String valBox1 = (String) Box1.getSelectedItem();
    	String valBox2;
    	if(Box2 != null) {
    		valBox2= (String) Box2.getSelectedItem();
    	}
    	else {
    		valBox2 = null;
    	}
    	for (Discipline discipline : Discipline.values()) {
    		if(discipline.toString().equals(valBox1) ) {
    			disciplines.add(discipline);
    		}
    		else if(valBox2 != null && !valBox2.equals(valBox1) && discipline.toString().equals(valBox2)) {
    			disciplines.add(discipline);
    		}
    	}
    	if (valBox2 != null && valBox2.equals("null")){
    		disciplines.add(null);
    	}
    	
    	return disciplines;
    }
    
    /**
     * Remplit un JComboBox avec les noms des chercheurs et des MCF disponibles.
     * Les chercheurs sont ajoutés à la liste sans conditions particulières.
     * Les MCF sont ajoutés uniquement s'ils n'ont pas d'étudiants assignés.
     * 
     * @param box Le modèle de JComboBox à remplir avec les titulaires.
     */
    private void setupTitulaireBox(DefaultComboBoxModel<String> box) {
    	for(Chercheur chercheur: data.chercheurs) {
    		box.addElement(chercheur.getPrenom() + " " + chercheur.getNom());
    	}
    	for(MCF mcf: data.MCFs) {
    		if(mcf.getEtudiantString().isEmpty()) {box.addElement(mcf.getPrenom() + " " + mcf.getNom());}
    	}
    	
    }
    
    /**
     * Remplit un JComboBox avec toutes les disciplines disponibles.
     * 
     * @param box Le modèle de JComboBox à remplir avec les disciplines.
     */
    private void setupdisciplinebox(DefaultComboBoxModel<String> box) {
    	for(Discipline discipline : Discipline.values()) {
    		box.addElement(discipline.toString());
    	}
    }
   
    /**
     * Crée un Panel contenant un label et un composant (zone de texte, JComboBox, etc.), 
     * disposés horizontalement avec un alignement à gauche.
     * 
     * @param texte Le texte de l'étiquette à afficher.
     * @param ZoneTexte Le composant à ajouter au Panel (JTextField, JComboBox, etc.).
     * @return Un JPanel contenant l'étiquette et le composant.
     */
     private JPanel creationZoneTexte(String texte, JComponent ZoneTexte) {
       JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alignement à gauche
       panel.setMaximumSize(new Dimension(600, 40)); // Taille fixe pour homogénéité
       JLabel label = new JLabel(texte);
       label.setPreferredSize(new Dimension(120, 20)); // Alignement visuel des étiquettes
       panel.add(label);
       panel.add(ZoneTexte);
       return panel;
     }
    
    
    /**
     * Affiche une boîte de dialogue pour sélectionner un étudiant à attribuer à un MCF.
     * Si aucun étudiant n'est disponible, un message d'erreur est affiché.
     * 
     * @param Liste_Res La liste qui contiendra l'étudiant sélectionné ou null si "Aucun" est sélectionné.
     */
    private void afficherEtudiantSelectionMCF(List<Etudiant> Liste_Res) {
        List<Etudiant> etudiants = new ArrayList<>();
        for (Etudiant e : data.etudiants) {
            if (e.getEncadrantString().isEmpty()) {
                etudiants.add(e);
            }
        }

        if (etudiants.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun étudiant disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Création d'une boîte de dialogue modale
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();

        // Ajout d'un bouton pour "Aucun"
        JRadioButton aucun = new JRadioButton("Aucun");
        group.add(aucun);
        panel.add(aucun);

        // Ajout d'un bouton pour chaque étudiant
        for (Etudiant etu : etudiants) {
            JRadioButton bouton = new JRadioButton(etu.getNom() + " " + etu.getPrenom());
            group.add(bouton);
            panel.add(bouton);

            // Écoute pour sélectionner l'étudiant
            bouton.addActionListener(e -> Liste_Res.add(0,etu));
        }

        // Bouton de confirmation
        JButton confirmButton = new JButton("Confirmer");
        confirmButton.addActionListener(e -> {
            if (aucun.isSelected()) {
            	Liste_Res.add(0,null); // Si "Aucun" est sélectionné
            }
            dialog.dispose(); // Fermer la boîte de dialogue
        });

        panel.add(confirmButton);

        dialog.add(new JScrollPane(panel)); // Ajout du panel avec défilement
        dialog.setVisible(true); // Affiche la boîte de dialogue et bloque l'exécution

        return; // Retourne l'étudiant sélectionné ou null
    }

   
    /**
     * Affiche une fenêtre permettant à un chercheur de sélectionner plusieurs étudiants.
     * 
     * @param Liste_Etudiant La liste qui contiendra les étudiants sélectionnés.
     */
    public void afficherEtudiantsSelectionChercheur(List<Etudiant> Liste_Etudiant) {
	   List<Etudiant> etudiants= new ArrayList<>();
	   for (Etudiant e: data.etudiants) {
		   if (e.getEncadrantString().isEmpty()) {
			   etudiants.add(e);
		   }
	   } 
	    
	    // Création d'un modèle de liste pour afficher les étudiants
	    DefaultListModel<String> etudiantModel = new DefaultListModel<>();
	    for (Etudiant etudiant : etudiants) {
	        etudiantModel.addElement(etudiant.getNom() + " " + etudiant.getPrenom());
	    }

	    // Création d'une liste pour la sélection multiple
	    JList<String> etudiantList = new JList<>(etudiantModel);
	    etudiantList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    etudiantList.setVisibleRowCount(10); // Limiter les lignes visibles

	    // Ajout d'un Panel avec un JScrollPane pour faciliter le défilement
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(new JScrollPane(etudiantList), BorderLayout.CENTER);

	    // Boutons pour valider ou annuler la sélection
	    JButton validerButton = new JButton("Valider");
	    JButton annulerButton = new JButton("Annuler");
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.add(validerButton);
	    buttonPanel.add(annulerButton);
	    panel.add(buttonPanel, BorderLayout.SOUTH);

	    // Création de la fenêtre
	    JFrame frame = new JFrame("Sélectionner des étudiants");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setSize(400, 300);
	    frame.add(panel);
	    frame.setLocationRelativeTo(null);

	    // Ajouter une action sur le bouton Valider
	    validerButton.addActionListener(e -> {
	        // Récupérer les étudiants sélectionnés
	        for (Integer i : etudiantList.getSelectedIndices()) {
	        	if(etudiants.get(i)!=null) {
	        		Liste_Etudiant.add(etudiants.get(i));
	        	}
	        	
	        }
	        frame.dispose();
	    });

	    // Ajouter une action sur le bouton Annuler
	    annulerButton.addActionListener(e -> {
	        // Fermer la fenêtre sans rien faire
	        frame.dispose();
	    });

	    // Afficher la fenêtre
	    SwingUtilities.invokeLater(() -> frame.setVisible(true));

	}
    
    /**
     * Vérifie si une chaîne de caractères peut être convertie en entier.
     * 
     * @param str La chaîne de caractères à vérifier.
     * @return true si la chaîne représente un entier valide, false sinon.
     */
    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
