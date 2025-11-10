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

💾 Outils utilisés

MySQL Workbench / phpMyAdmin — création et gestion de la base

WampServer64 — environnement local (Apache, MySQL, PHP)

PlantUML — modélisation UML

Java (MVC) — logique applicative (modèles, vues, contrôleurs)

GitHub — hébergement du projet

🚀 Structure du projet (Java)
BilletterieProjet/
├── src/
│   ├── model/           # Classes du modèle (Spectacle, Billet, Client, etc.)
│   ├── controller/      # Contrôleurs pour la gestion des interactions
│   └── view/            # Interfaces graphiques
├── resources/
│   └── billetterie.png  # Diagramme UML
