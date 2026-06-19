# Tic'n Go - Application de Billetterie JavaFX

## Présentation

Tic'n Go est une application client lourd développée en JavaFX permettant de gérer une billetterie événementielle.

L'application propose un back-office administrateur pour gérer les événements, les billets, les clients, les réservations et les statistiques de la plateforme.

## Fonctionnalités principales

- Authentification administrateur
- Mot de passe oublié avec code temporaire
- Dashboard dynamique connecté à MySQL
- Gestion des événements
- Gestion des réservations
- Gestion des billets
- Statuts des billets : valide, annulé, remboursé, utilisé
- Recherche, filtres et tri des événements
- Connexion à une base de données MySQL
- Architecture MVC avec DAO
- Interface JavaFX stylisée avec CSS

## Technologies utilisées

- Java 21
- JavaFX 21
- Maven
- MySQL
- JDBC
- BCrypt
- IntelliJ IDEA
- CSS JavaFX

## Architecture du projet

```text
src/main/java
├── app
├── controllers
├── dao
├── database
└── models

src/main/resources
├── views
├── styles
└── images

Base de données

Le projet utilise une base de données MySQL nommée billetterie.

Tables principales :

administrateur
utilisateur
client
evenement
spectacle
seance
billet
reservation
lieu
Lancement du projet
Cloner le dépôt
Importer le projet dans IntelliJ IDEA
Configurer la connexion MySQL dans Database.java
Importer le script SQL de la base de données
Lancer l'application avec Maven ou IntelliJ
Un script `database.sql` est fourni pour recréer la base de données MySQL avec des données de test.
Auteur
## Déploiement et environnement de production

### Architecture retenue

L’application Tic’n Go est une application client lourd développée avec JavaFX.
Elle est distribuée sous la forme d’un fichier JAR exécutable.

La base de données MySQL n’est plus hébergée uniquement en local. Elle est déployée sur une instance Amazon EC2 afin de permettre à l’application de se connecter à une base distante.

Architecture mise en place :

```text
Application JavaFX
        |
        | Connexion JDBC sécurisée par les règles réseau AWS
        |
Instance Amazon EC2
        |
        | MySQL
        |
Base de données billetterie
```

### Hébergement de la base de données

La base de données est hébergée avec les éléments suivants :

* Hébergeur : Amazon Web Services
* Service : Amazon EC2
* Système d’exploitation : Ubuntu Server
* SGBD : MySQL
* Base de données : `billetterie`
* Port MySQL : `3306`
* Adresse réseau : adresse Elastic IP AWS
* Utilisateur applicatif dédié : `ticngo_app`

Une adresse Elastic IP a été associée à l’instance EC2 afin de conserver une adresse publique fixe, même après un redémarrage du serveur.

Le groupe de sécurité AWS autorise uniquement :

* le port `22` pour l’administration SSH ;
* le port `3306` pour la connexion MySQL ;
* les adresses IP explicitement autorisées.

Le compte MySQL `root` n’est pas utilisé par l’application. Un utilisateur dédié avec des droits limités à la base `billetterie` a été créé.

### Migration de la base de données

La base locale MySQL a été exportée depuis MySQL Workbench avec sa structure et ses données.

Le fichier SQL a ensuite été transféré sur le serveur AWS avec SCP :

```powershell
scp -i "CHEMIN_VERS_LA_CLE.pem" `
"CHEMIN_VERS\billetterie.sql" `
ubuntu@ADRESSE_IP_AWS:/home/ubuntu/
```

La base a ensuite été importée sur le serveur :

```bash
mysql -u ticngo_app -p billetterie < /home/ubuntu/billetterie.sql
```

La présence des tables a été vérifiée avec :

```sql
USE billetterie;
SHOW TABLES;
```

Tables principales :

```text
administrateur
billet
client
evenement
lieu
reservation
seance
spectacle
utilisateur
```

### Configuration externe

Les paramètres de connexion ne sont pas écrits directement dans le code source.

Ils sont stockés dans un fichier externe :

```text
config.properties
```

Exemple de structure :

```properties
db.url=jdbc:mysql://ADRESSE_IP_AWS:3306/billetterie?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&connectTimeout=10000
db.user=ticngo_app
db.password=VOTRE_MOT_DE_PASSE_MYSQL
```

Le fichier `config.properties` contient des informations sensibles. Il est donc exclu du dépôt Git grâce au fichier `.gitignore` :

```gitignore
config.properties
```

Un fichier d’exemple sans mot de passe réel peut être fourni :

```text
config.example.properties
```

### Génération du JAR exécutable

Le projet utilise Maven pour compiler l’application et générer le fichier JAR.

Depuis la racine du projet :

```powershell
mvn clean package
```

Le build est validé lorsque Maven affiche :

```text
BUILD SUCCESS
```

Le fichier généré se trouve ensuite dans :

```text
target/codebilletterie-1.0.0.jar
```

Le projet utilise une classe de lancement dédiée :

```text
app.Launcher
```

Le plugin Maven Shade permet :

* d’intégrer les dépendances dans le JAR ;
* d’ajouter la classe principale au manifeste ;
* d’exclure les anciennes signatures incompatibles des dépendances ;
* de rendre le fichier directement exécutable.

### Lancement de l’application

Le fichier `config.properties` doit être placé dans le même dossier que le JAR :

```text
production-test/
├── codebilletterie-1.0.0.jar
└── config.properties
```

Pour lancer l’application :

```powershell
cd production-test
java -jar codebilletterie-1.0.0.jar
```

L’application doit afficher la page de connexion Tic’n Go et établir une connexion avec la base MySQL hébergée sur AWS.

### Lancement depuis IntelliJ IDEA

Pour lancer l’application pendant le développement :

1. Ouvrir le projet dans IntelliJ IDEA.
2. Vérifier que le fichier `config.properties` est présent à la racine.
3. Ouvrir :

```text
src/main/java/app/App.java
```

4. Exécuter la méthode `main`.

Il est également possible d’utiliser Maven :

```powershell
mvn clean javafx:run
```

### Compte administrateur de démonstration

L’adresse email du compte administrateur est fournie séparément aux examinateurs.

Le mot de passe applicatif est stocké dans MySQL sous forme de hash BCrypt.
Le mot de passe en clair n’est jamais enregistré directement dans la base de données.

### Tests réalisés en production

Les tests suivants ont été effectués avec la base AWS :

* connexion administrateur ;
* affichage du Dashboard dynamique ;
* récupération des événements ;
* affichage des clients ;
* gestion des billets ;
* gestion des séances ;
* gestion des réservations ;
* recherche et filtrage ;
* lecture et écriture dans MySQL ;
* lancement de l’application hors d’IntelliJ ;
* lancement du JAR depuis un dossier de production séparé.

### Résultat du déploiement

Le projet dispose désormais :

* d’un JAR exécutable ;
* d’une base MySQL distante hébergée sur AWS ;
* d’une adresse Elastic IP fixe ;
* d’un utilisateur MySQL applicatif dédié ;
* d’une configuration externe ;
* d’un script SQL de sauvegarde et de restauration ;
* d’une documentation d’installation et de lancement ;
* d’un dépôt GitHub contenant le code source sans les secrets de production.

Projet réalisé par Pierre-Jordan Tchokote dans le cadre du BTS SIO option SLAM.