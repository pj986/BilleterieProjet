# 🎭 Tic'n Go — Système de Billetterie Spectacle
📋 Description du projet

Tic’n Go est une application de billetterie en ligne développée en Java permettant la gestion complète des spectacles, billets, clients et administrateurs.
Le système propose :

Un back-office pour la gestion des spectacles, utilisateurs et réservations.

Une interface client pour consulter les événements et acheter des billets.

# 🧩 Modélisation de la base de données

J’ai réalisé la modélisation de la base de données à l’aide d’un diagramme UML représentant les différentes entités et leurs relations.
Cette modélisation m’a permis de concevoir la structure du futur fichier SQL.

🧱 Diagramme de la base de données

Le schéma ci-dessous représente la modélisation de la base de données de l’application Tic’n Go.
Il a été conçu avec PlantUML et sert de référence pour la génération du fichier SQL billetterie.sql.

📊 Entités principales

Administrateur : gère les spectacles, les séances et les utilisateurs.

Client : utilisateur du site pouvant acheter des billets.

Spectacle : événement culturel proposé dans un Lieu donné.

Séance : représentation d’un spectacle à une date précise.

Billet : ticket associé à un client et à une séance.

Lieu : espace où les spectacles sont accueillis.

🔗 Relations clés

Un Lieu accueille plusieurs Spectacles.

Un Spectacle possède plusieurs Séances.

Une Séance peut donner lieu à plusieurs Billets.

Un Client peut posséder plusieurs Billets.

Un Administrateur gère l’ensemble des données.

🖼️ Diagramme UML

📎 Ce diagramme est utilisé comme base pour la création du fichier billetterie.sql dans phpMyAdmin.

# 📘 Outil utilisé : PlantUML

📄 Fichier rendu : billetterie.png (diagramme UML)

💡 Le modèle est évolutif afin de permettre des ajustements futurs lors du développement.

# 🛠️ Création de la base de données

La création de la base de données s’effectue à partir du fichier :

billetterie.sql

Ce script contient la structure complète de la base (tables, clés primaires/étrangères, contraintes, etc.).

Ce fichier contient :

La création complète des tables (avec contraintes)

Les clés primaires & étrangères

Les types de données adaptés à l’application Java

Les options de suppression en cascade (ON DELETE CASCADE, ON DELETE SET NULL)

DROP TABLE IF EXISTS ...
SET FOREIGN_KEY_CHECKS = 0;

👉 Cela permet d’importer la base sans erreur, même si les tables existent déjà.

# 📦 Jeu de Données (volumétrie réaliste)

Pour simuler un environnement réel, un second script a été généré :

📄 billetterie_data.sql
(inclus directement à la suite dans le fichier principal)

Il contient un jeu de données complet basé sur une thématique cohérente.

🎨 Thématique : Festival des Cultures & Saveurs

Les données sont basées sur un univers culturel et gastronomique :

🍷 Dégustations

🎤 Concerts

🍣 Évènements culinaires

🎭 Spectacles d'humour et shows culturels

🌍 Villes françaises principales

📊 Volumétrie incluse dans le script :

10 Lieux

10 Spectacles

20 Séances

60 Clients

100 Billets

Les données sont réalistes et compatibles avec la logique métier :

billets aléatoires mais cohérents

prix basés sur le spectacle

clients distribués sur les séances

catégories : standard, premium, etudiant


#  🧰 Installation et Importation de la Base (phpMyAdmin)
1️⃣ Ouvrir phpMyAdmin via WampServer64

→ http://localhost/phpmyadmin

2️⃣ Créer une base vide :
CREATE DATABASE billetterie CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

3️⃣ Importer le fichier billetterie.sql

Onglet Importer

Sélectionner le fichier

Valider

✔️ Tous les tables + données seront créés proprement
✔️ Compatible avec les versions MySQL de WampServer
# Étapes de mise en place

Ouvrir phpMyAdmin (via WampServer64).

Créer une nouvelle base de données nommée :

CREATE DATABASE billetterie CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


Importer le fichier billetterie.sql depuis l’onglet Importer.

Les tables principales (exemples) :

utilisateur

spectacle

billet

reservation

paiement

🏠 Accueil

Page d’accueil centrale

Navigation vers :

Connexion

Inscription

Liste des événements

📝 Inscription

Création de compte utilisateur

Vérification des champs obligatoires

Vérification de la correspondance des mots de passe

Messages d’erreur et de succès dynamiques (Label JavaFX + CSS)

Navigation vers la page de connexion

🔑 Connexion

Connexion utilisateur (simulation, prêt pour SQL)

Gestion des erreurs (champs vides / identifiants incorrects)

Navigation vers :

Accueil

Inscription

🎟️ Événements

Liste d’événements générée dynamiquement depuis MySQL

Affichage sous forme de cartes (type Eventbrite)

Informations affichées :

Image

Titre

Date

Lieu

Bouton « Acheter »

Retour vers la page d’accueil

💾 Outils utilisés
 phpMyAdmin — création et gestion de la base

WampServer64 — environnement local (Apache, MySQL, PHP)

PlantUML — modélisation UML

Java (MVC) — logique applicative (modèles, vues, contrôleurs)

GitHub — hébergement du projet

# 🧱 Architecture du projet (MVC)
src/
 ├─ app/
 │    └─ App.java                  # Point d’entrée JavaFX
 │
 ├─ controllers/
 │    ├─ HomeController.java
 │    ├─ LoginController.java
 │    ├─ InscriptionController.java
 │    └─ EventsController.java
 │
 ├─ views/
 │    ├─ Home.fxml
 │    ├─ Login.fxml
 │    ├─ Inscription.fxml
 │    └─ Events.fxml
 │
 ├─ models/
 │    └─ Evenement.java            # Modèle métier
 │
 ├─ dao/
 │    └─ EvenementDAO.java         # Accès aux données (CRUD)
 │
 ├─ database/
 │    └─ Database.java             # Connexion MySQL
 │
 ├─ styles/
 │    └─ app.css                   # Style global JavaFX
 │
 └─ assets/
      ├─ event1.jpg
      ├─ event2.jpg
      └─ event3.jpg
