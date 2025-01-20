# LeonLouisSujet2024java

LeonLouisSujet2024javan est une application Java permettant de résoudre le problème du voyageur de commerce sur des cycles hamiltoniens dans un écosystème fictif composé de chercheurs, de MCF (Maîtres de Conférences) et d'étudiants.

## Description
Cette application propose une solution au problème du voyageur de commerce en modélisant un écosystème fictif intégrant des entités interconnectées (chercheurs, MCF et étudiants). L'application permet de manipuler ces entités grâce à une interface graphique intuitive, tout en exploitant une base de données PostgreSQL contenant les villes de France. Elle sert à la fois à résoudre le problème de manière efficace et à afficher les résultats de façon claire et interactive.

## Prérequis
Avant de lancer l'application, assurez-vous d'avoir installé :

- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) (version 22 ou supérieure)
- [pgAdmin](https://www.pgadmin.org/download/) pour la gestion de la base de données PostgreSQL

## Installation

1. Exécutez le script SQL pour préparer la base de données :
   - Ouvrez pgAdmin et connectez-vous à votre serveur PostgreSQL.
   - Créez une nouvelle base de données (par exemple, `LouisLeonDB`).
   - Importez le fichier `ScriptCreationVilleEtDepartement.sql` fourni dans le projet dans le dossier `Fichier pré-SQL`.

2. Compilez et exécutez l'application depuis la racine du projet:
   ```bash
   javac -d bin --module-path lib -cp "lib/*" src/module-info.java src/algorithmeGenetiquePack/*.java src/Data/*.java src/Main/*.java src/map/*.java src/ParametresHandler/*.java src/problemeDuVoyageurDeCommerce/*.java src/SQL/*.java src/tests/*.java src/TriCycleHamiltonien/*.java

   java -Xms512m -Xmx12g -cp "bin;lib/*" Main.Main #Si vous voulez changer la taille de l'allocation maximale de mémoire, il va falloir changer le parametre -Xmx12g en réduisant le 12
   ```

3. Lancer l'application sur Eclipse
   - Importer le projet dans votre Workspace
   - Executer le programme depuis le fichier `Main.Main`

4. Lancer l'application depuis l'explorateur de fichier
    - Executer le fichier `Application.jar` trouvable dans la racine du projet
    ```bash
    java -jar -Xms512m -Xmx12g Application.jar
    ```
        

## Utilisation

1. Lancez l'application Java.
2. Connectez-vous à la base de données.
3. Naviguez à travers les options de l'interface pour :
   - Se connecter à la base de donnée
   - Créer un écosystème fictif
   - Gérer les entités (chercheurs, MCF, étudiants)
   - Résoudre et visualiser le problème du voyageur de commerce.
4. Une sauvegarde existe déjà pour pleinement pouvoir tester l'application
   - Cliquer sur Fichier -> Sauvegarder l'écosystème
   - Cliquer sur le bouton `récupération` en haut à gauche
   - Selectionner la sauvegarde `Save`

## Fonctionnalités

- Modélisation des relations entre chercheurs, MCF et étudiants.
- Modification de l'écosystème (ajout, modification ou suppression d'individus).
- Consultation des informations de l'écosystème.
- Connexion dynamique avec une base de données PostgreSQL.
- Génération de cycles hamiltoniens en fonction de contraintes.
- Visualisation graphique des résultats.
- Génération d'un fichier pour visualiser la dernière solution au problème.
- Sauvegarde des informations ou restauration à partir d'une sauvegarde.


## Licence

Ce projet est sous licence [MIT](https://en.wikipedia.org/wiki/MIT_License).

## Auteurs

- **Léon Hogenmuller**
- **Louis De Domingo**

