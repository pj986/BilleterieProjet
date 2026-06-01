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

Projet réalisé par Pierre-Jordan Tchokote dans le cadre du BTS SIO option SLAM.