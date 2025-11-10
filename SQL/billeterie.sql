-- ============================================================
-- 🎭 Projet : Tic’n Go - Système de billetterie de spectacles
-- Auteur : Pierre-Jordan Tchokote
-- Fichier : billetterie.sql
-- Description : Création de la base de données et insertion d'exemples
-- ============================================================

-- 1️⃣ Création de la base de données
CREATE DATABASE IF NOT EXISTS billetterie
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE billetterie;

-- ============================================================
-- 2️⃣ Table : Administrateur
-- ============================================================
CREATE TABLE Administrateur (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(100)
);

-- ============================================================
-- 3️⃣ Table : Lieu
-- ============================================================
CREATE TABLE Lieu (
    id_lieu INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    adresse TEXT NOT NULL,
    capacite INT
);

-- ============================================================
-- 4️⃣ Table : Spectacle
-- ============================================================
CREATE TABLE Spectacle (
    id_spectacle INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    affiche VARCHAR(255),
    id_lieu INT,
    duree TIME,
    description_courte TEXT,
    description_longue TEXT,
    langue VARCHAR(50),
    age_minimum INT,
    tags VARCHAR(255),
    FOREIGN KEY (id_lieu) REFERENCES Lieu(id_lieu) ON DELETE SET NULL
);

-- ============================================================
-- 5️⃣ Table : Séance
-- ============================================================
CREATE TABLE Seance (
    id_seance INT AUTO_INCREMENT PRIMARY KEY,
    id_spectacle INT NOT NULL,
    date_heure DATETIME NOT NULL,
    places_total INT,
    places_disponibles INT,
    prix_base DECIMAL(10,2),
    FOREIGN KEY (id_spectacle) REFERENCES Spectacle(id_spectacle) ON DELETE CASCADE
);

-- ============================================================
-- 6️⃣ Table : Client
-- ============================================================
CREATE TABLE Client (
    id_client INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 7️⃣ Table : Billet
-- ============================================================
CREATE TABLE Billet (
    id_billet INT AUTO_INCREMENT PRIMARY KEY,
    numero_unique VARCHAR(50) UNIQUE NOT NULL,
    id_client INT,
    id_seance INT,
    statut ENUM('valide', 'annule', 'rembourse') DEFAULT 'valide',
    prix_final DECIMAL(10,2),
    date_achat DATETIME DEFAULT CURRENT_TIMESTAMP,
    categorie_tarif VARCHAR(50),
    FOREIGN KEY (id_client) REFERENCES Client(id_client) ON DELETE CASCADE,
    FOREIGN KEY (id_seance) REFERENCES Seance(id_seance) ON DELETE CASCADE
);

-- ============================================================
-- 8️⃣ Insertion d’exemples de données
-- ============================================================

-- Administrateurs
INSERT INTO Administrateur (email, mot_de_passe, nom)
VALUES
('admin@ticngo.fr', 'admin123', 'Administrateur Principal');

-- Lieux
INSERT INTO Lieu (nom, adresse, capacite)
VALUES
('Théâtre des Lumières', '12 rue de la République, Paris', 300),
('Palais du Rire', '5 avenue des Arts, Lyon', 500);

-- Spectacles
INSERT INTO Spectacle (titre, affiche, id_lieu, duree, description_courte, langue, age_minimum, tags)
VALUES
('Le Rire en Scène', 'affiche_rire.jpg', 1, '01:30:00', 'Un spectacle humoristique interactif.', 'Français', 12, 'humour,standup'),
('Magie et Illusion', 'affiche_magie.jpg', 2, '02:00:00', 'Un show spectaculaire de magie moderne.', 'Français', 8, 'magie,illusion');

-- Séances
INSERT INTO Seance (id_spectacle, date_heure, places_total, places_disponibles, prix_base)
VALUES
(1, '2025-12-15 20:00:00', 300, 250, 25.00),
(2, '2025-12-20 19:30:00', 500, 450, 30.00);

-- Clients
INSERT INTO Client (nom, email, telephone, adresse)
VALUES
('Jean Dupont', 'jean.dupont@email.com', '0612345678', '10 rue des Lilas, Paris'),
('Marie Curie', 'marie.curie@email.com', '0698765432', '25 avenue des Fleurs, Lyon');

-- Billets
INSERT INTO Billet (numero_unique, id_client, id_seance, statut, prix_final, categorie_tarif)
VALUES
('TICN001', 1, 1, 'valide', 25.00, 'standard'),
('TICN002', 2, 2, 'valide', 30.00, 'premium');

-- ============================================================
-- ✅ Fin du script
-- ============================================================
