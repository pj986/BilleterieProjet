CREATE DATABASE IF NOT EXISTS billetterie
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE billetterie;

DROP TABLE IF EXISTS billet;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS seance;
DROP TABLE IF EXISTS spectacle;
DROP TABLE IF EXISTS evenement;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS administrateur;
DROP TABLE IF EXISTS lieu;

CREATE TABLE administrateur (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(191) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(100),
    reset_code VARCHAR(10),
    reset_code_expiration DATETIME
);

CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'client'
);

CREATE TABLE client (
    id_client INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(191) NOT NULL UNIQUE,
    telephone VARCHAR(20),
    adresse TEXT,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lieu (
    id_lieu INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    adresse TEXT NOT NULL,
    capacite INT
);

CREATE TABLE evenement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT,
    date_evt VARCHAR(10) NOT NULL,
    heure_evt TIME,
    lieu VARCHAR(150) NOT NULL,
    prix DECIMAL(10,2) NOT NULL,
    image VARCHAR(255),
    categorie VARCHAR(50) NOT NULL
);

CREATE TABLE spectacle (
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
    FOREIGN KEY (id_lieu) REFERENCES lieu(id_lieu)
);

CREATE TABLE seance (
    id_seance INT AUTO_INCREMENT PRIMARY KEY,
    id_spectacle INT NOT NULL,
    date_heure DATETIME NOT NULL,
    places_total INT,
    places_disponibles INT,
    prix_base DECIMAL(10,2),
    FOREIGN KEY (id_spectacle) REFERENCES spectacle(id_spectacle)
);

CREATE TABLE billet (
    id_billet INT AUTO_INCREMENT PRIMARY KEY,
    numero_unique VARCHAR(50) NOT NULL UNIQUE,
    id_client INT,
    id_seance INT,
    statut ENUM('valide', 'annule', 'rembourse', 'utilise') DEFAULT 'valide',
    prix_final DECIMAL(10,2),
    date_achat DATETIME DEFAULT CURRENT_TIMESTAMP,
    categorie_tarif VARCHAR(50),
    place_numero VARCHAR(50),
    qr_code VARCHAR(255),
    FOREIGN KEY (id_client) REFERENCES client(id_client),
    FOREIGN KEY (id_seance) REFERENCES seance(id_seance)
);

CREATE TABLE reservation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_client VARCHAR(150) NOT NULL,
    evenement_id INT NOT NULL,
    date_reservation DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evenement_id) REFERENCES evenement(id)
);

INSERT INTO administrateur (email, mot_de_passe, nom)
VALUES
('admin@admin.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhiPvVYHZnp1W5k0XoG2H6P2a8Y2Dq2W', 'Administrateur');

INSERT INTO utilisateur (email, mot_de_passe, role)
VALUES
('client@test.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhiPvVYHZnp1W5k0XoG2H6P2a8Y2Dq2W', 'client');

INSERT INTO client (nom, email, telephone, adresse)
VALUES
('Pierre', 'pierre@test.com', '0600000000', 'Paris'),
('Jordan', 'jordan@test.com', '0611111111', 'Chelles'),
('Client Demo', 'demo@test.com', NULL, 'France');

INSERT INTO lieu (nom, adresse, capacite)
VALUES
('Théâtre Mogador', '25 Rue de Mogador, 75009 Paris', 1600),
('Accor Arena', '8 Boulevard de Bercy, 75012 Paris', 20000),
('Stade de France', '93200 Saint-Denis', 80000);

INSERT INTO evenement (titre, description, date_evt, heure_evt, lieu, prix, image, categorie)
VALUES
('Le Roi Lion', 'Comédie musicale familiale à Paris.', '2026-06-15', '20:30:00', 'Théâtre Mogador', 59.90, 'event1.jpg', 'Spectacle'),
('Concert Pop Paris', 'Grand concert pop avec plusieurs artistes.', '2026-07-01', '21:00:00', 'Accor Arena', 75.00, 'event2.jpg', 'Concert'),
('Finale Football Experience', 'Grand événement sportif au Stade de France.', '2026-08-10', '20:45:00', 'Stade de France', 99.00, 'event3.jpg', 'Sport');

INSERT INTO spectacle (titre, affiche, id_lieu, duree, description_courte, description_longue, langue, age_minimum, tags)
VALUES
('Le Roi Lion', 'roi_lion.jpg', 1, '02:30:00', 'Comédie musicale familiale.', 'Un spectacle musical avec chants, décors et mise en scène professionnelle.', 'Français', 3, 'spectacle,comédie musicale'),
('Concert Pop Paris', 'concert_pop.jpg', 2, '02:00:00', 'Concert pop.', 'Un événement musical avec plusieurs artistes sur scène.', 'Français', NULL, 'concert,musique');

INSERT INTO seance (id_spectacle, date_heure, places_total, places_disponibles, prix_base)
VALUES
(1, '2026-06-15 20:30:00', 500, 500, 59.90),
(1, '2026-06-15 15:00:00', 300, 300, 45.00),
(2, '2026-07-01 21:00:00', 1000, 1000, 75.00);

INSERT INTO billet (numero_unique, id_client, id_seance, statut, prix_final, categorie_tarif, place_numero, qr_code)
VALUES
('TNG-001', 1, 1, 'valide', 59.90, 'plein', 'A12', 'QR-TNG-001'),
('TNG-002', 2, 1, 'annule', 39.90, 'reduit', 'B08', 'QR-TNG-002'),
('TNG-003', 3, 3, 'rembourse', 75.00, 'plein', 'C15', 'QR-TNG-003');

INSERT INTO reservation (email_client, evenement_id)
VALUES
('client@test.com', 1),
('pierre@test.com', 2);