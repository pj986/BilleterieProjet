-- ============================================================
-- 🎭 Projet : Tic’n Go - Système de billetterie de spectacles
-- Auteur : Pierre-Jordan Tchokote
-- Fichier : billetterie.sql (version corrigée utf8mb4)
-- ============================================================

CREATE DATABASE IF NOT EXISTS billetterie
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE billetterie;

-- ============================================================
-- 2️⃣ Table : Administrateur
-- ============================================================
CREATE TABLE Administrateur (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(191) UNIQUE NOT NULL,  -- ✅ Correction ici
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
    email VARCHAR(191) UNIQUE NOT NULL,  -- ✅ Même correction ici
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
-- 8️⃣ Données d'exemple
-- ============================================================

INSERT INTO Administrateur (email, mot_de_passe, nom)
VALUES ('admin@ticngo.fr', 'admin123', 'Administrateur Principal');

INSERT INTO Lieu (nom, adresse, capacite)
VALUES
('Théâtre des Lumières', '12 rue de la République, Paris', 300),
('Palais du Rire', '5 avenue des Arts, Lyon', 500);

INSERT INTO Spectacle (titre, affiche, id_lieu, duree, description_courte, langue, age_minimum, tags)
VALUES
('Le Rire en Scène', 'affiche_rire.jpg', 1, '01:30:00', 'Spectacle humoristique interactif.', 'Français', 12, 'humour,standup'),
('Magie et Illusion', 'affiche_magie.jpg', 2, '02:00:00', 'Show de magie moderne.', 'Français', 8, 'magie,illusion');

INSERT INTO Seance (id_spectacle, date_heure, places_total, places_disponibles, prix_base)
VALUES
(1, '2025-12-15 20:00:00', 300, 250, 25.00),
(2, '2025-12-20 19:30:00', 500, 450, 30.00);

INSERT INTO Client (nom, email, telephone, adresse)
VALUES
('Jean Dupont', 'jean.dupont@email.com', '0612345678', '10 rue des Lilas, Paris'),
('Marie Curie', 'marie.curie@email.com', '0698765432', '25 avenue des Fleurs, Lyon');

INSERT INTO Billet (numero_unique, id_client, id_seance, statut, prix_final, categorie_tarif)
VALUES
('TICN001', 1, 1, 'valide', 25.00, 'standard'),
('TICN002', 2, 2, 'valide', 30.00, 'premium');


-- =======================
-- 📅 SÉANCES (20 lignes)
-- =======================
INSERT INTO Seance (id_spectacle, date_heure, places_total, places_disponibles, prix_base) VALUES
(1, '2025-12-01 19:30:00', 300, 280, 25),
(1, '2025-12-10 20:00:00', 300, 260, 25),

(2, '2025-12-05 18:00:00', 450, 420, 30),
(2, '2025-12-18 21:00:00', 450, 430, 30),

(3, '2025-12-12 20:00:00', 250, 200, 20),
(3, '2025-12-22 19:00:00', 250, 230, 20),

(4, '2025-12-03 21:00:00', 500, 470, 35),
(4, '2025-12-14 20:30:00', 500, 450, 35),

(5, '2025-12-07 19:00:00', 320, 285, 28),
(5, '2025-12-15 19:30:00', 320, 275, 28),

(6, '2025-12-09 18:30:00', 400, 360, 22),
(6, '2025-12-20 20:00:00', 400, 350, 22),

(7, '2025-12-04 20:00:00', 380, 350, 32),
(7, '2025-12-17 21:00:00', 380, 340, 32),

(8, '2025-12-11 19:30:00', 260, 240, 18),
(8, '2025-12-21 20:30:00', 260, 235, 18),

(9, '2025-12-06 19:00:00', 450, 420, 26),
(9, '2025-12-16 19:00:00', 450, 410, 26),

(10,'2025-12-13 22:00:00', 600, 550, 40),
(10,'2025-12-27 23:00:00', 600, 540, 40);

-- =======================
-- 👥 CLIENTS (60 lignes)
-- =======================
INSERT INTO Client (nom, email, telephone, adresse) VALUES
('Jean Dupont', 'jean.dupont@example.com', '0612345678', 'Paris'),
('Marie Curie', 'marie.curie@example.com', '0698765432', 'Lyon'),
('Paul Leroy', 'paul.leroy@example.com', '0678123456', 'Marseille'),
('Lucie Martin', 'lucie.martin@example.com', '0654789632', 'Toulouse'),
('Nicolas Petit', 'nico.petit@example.com', '0645987132', 'Nice'),
('Sophie Bernard', 'sophie.bernard@example.com', '0632578941', 'Nantes'),
('David Fabre', 'david.fabre@example.com', '0665478392', 'Bordeaux'),
('Emma Charles', 'emma.charles@example.com', '0674125896', 'Lille'),
('Hugo Simon', 'hugo.simon@example.com', '0625897413', 'Strasbourg'),
('Clara Rolland', 'clara.rolland@example.com', '0614785239', 'Montpellier'),
('Kevin Giraud', 'kevin.giraud@example.com', '0698514732', 'Paris'),
('Julie Noël', 'julie.noel@example.com', '0632147859', 'Lyon'),
('Antoine Chevalier', 'antoine.chev@example.com', '0687452319', 'Nice'),
('Camille Delmas', 'camille.delmas@example.com', '0612547893', 'Nantes'),
('Léa Fournier', 'lea.fournier@example.com', '0678451293', 'Bordeaux'),
('Yanis Robert', 'yanis.robert@example.com', '0624785193', 'Marseille'),
('Elisa Bertrand', 'elisa.bertrand@example.com', '0698745123', 'Toulouse'),
('Thomas Lopez', 'thomas.lopez@example.com', '0647893215', 'Paris'),
('Anaïs Poulain', 'anais.poulain@example.com', '0667984512', 'Lille'),
('Rayan Dubois', 'rayan.dubois@example.com', '0625478931', 'Strasbourg'),
('Mélanie Lefevre', 'melanie.lefevre@example.com', '0613578942', 'Lyon'),
('Adrien Roussel', 'adrien.roussel@example.com', '0687954123', 'Nice'),
('Sarah Garcia', 'sarah.garcia@example.com', '0678234591', 'Bordeaux'),
('Mathis Barret', 'mathis.barret@example.com', '0614529837', 'Toulouse'),
('Laura Morel', 'laura.morel@example.com', '0658427913', 'Marseille'),
('Clément Hardy', 'clement.hardy@example.com', '0674985213', 'Montpellier'),
('Amélie Riviere', 'amelie.riviere@example.com', '0694125783', 'Paris'),
('Maxime Gauthier', 'maxime.gauthier@example.com', '0635794218', 'Lyon'),
('Eva Renault', 'eva.renault@example.com', '0612485937', 'Nice'),
('Sami Brun', 'sami.brun@example.com', '0623589741', 'Nantes'),
('Claire Didier', 'claire.didier@example.com', '0674123589', 'Bordeaux'),
('Lucas Perrot', 'lucas.perrot@example.com', '0654987321', 'Marseille'),
('Alicia Benard', 'alicia.benard@example.com', '0698752314', 'Strasbourg'),
('Nathan Picard', 'nathan.picard@example.com', '0623564789', 'Toulouse'),
('Jade Lemaitre', 'jade.lemaitre@example.com', '0687412596', 'Paris'),
('Corentin Vallet', 'corentin.vallet@example.com', '0671254893', 'Lille'),
('Inès Pons', 'ines.pons@example.com', '0614789253', 'Montpellier'),
('Louis Moulin', 'louis.moulin@example.com', '0632514789', 'Lyon'),
('Noémie Denis', 'noemie.denis@example.com', '0678941235', 'Nice'),
('Axel Legros', 'axel.legros@example.com', '0697124538', 'Nantes'),
('Chloé Roy', 'chloe.roy@example.com', '0647895123', 'Marseille'),
('Maël Adam', 'mael.adam@example.com', '0625893147', 'Paris'),
('Tiffany Marechal', 'tiffany.marechal@example.com', '0687412539', 'Strasbourg'),
('Ethan Hoarau', 'ethan.hoarau@example.com', '0678914523', 'Lille'),
('Romane Huret', 'romane.huret@example.com', '0614785926', 'Bordeaux'),
('Enzo Colin', 'enzo.colin@example.com', '0698475123', 'Lyon'),
('Lina Vasseur', 'lina.vasseur@example.com', '0621457893', 'Toulouse'),
('Victor Tessier', 'victor.tessier@example.com', '0678541239', 'Nice'),
('Aurelia Poulard', 'aurelia.poulard@example.com', '0614782539', 'Paris'),
('Ilan Rousseau', 'ilan.rousseau@example.com', '0645127893', 'Montpellier'),
('Nina Hamel', 'nina.hamel@example.com', '0687459321', 'Bordeaux'),
('Sacha Pineau', 'sacha.pineau@example.com', '0697412589', 'Strasbourg'),
('Lisa Merle', 'lisa.merle@example.com', '0687451296', 'Toulouse'),
('Timéo Fabry', 'timeo.fabry@example.com', '0612587943', 'Lyon'),
('Mya Schmitt', 'mya.schmitt@example.com', '0678451298', 'Marseille'),
('Noa Briand', 'noa.briand@example.com', '0654781239', 'Nice'),
('Elian Dupin', 'elian.dupin@example.com', '0625891437', 'Nantes'),
('Zélie Blondel', 'zelie.blondel@example.com', '0678914526', 'Paris');

-- =======================================
-- 🎟️ 100 BILLETS — Données réalistes
-- =======================================

INSERT INTO Billet (numero_unique, id_client, id_seance, statut, prix_final, categorie_tarif) VALUES
('TICB001', 1, 1, 'valide', 25, 'standard'),
('TICB002', 2, 1, 'valide', 25, 'standard'),
('TICB003', 3, 2, 'premium', 27, 'premium'),
('TICB004', 4, 2, 'valide', 25, 'standard'),
('TICB005', 5, 3, 'valide', 30, 'standard'),
('TICB006', 6, 3, 'premium', 32, 'premium'),
('TICB007', 7, 4, 'etudiant', 20, 'etudiant'),
('TICB008', 8, 4, 'valide', 30, 'standard'),
('TICB009', 9, 5, 'valide', 20, 'standard'),
('TICB010', 10, 5, 'annule', 0, 'standard'),

('TICB011', 11, 6, 'valide', 20, 'standard'),
('TICB012', 12, 6, 'valide', 20, 'standard'),
('TICB013', 13, 7, 'valide', 35, 'standard'),
('TICB014', 14, 7, 'premium', 38, 'premium'),
('TICB015', 15, 8, 'valide', 35, 'standard'),
('TICB016', 16, 8, 'annule', 0, 'standard'),
('TICB017', 17, 9, 'valide', 28, 'standard'),
('TICB018', 18, 9, 'valide', 28, 'standard'),
('TICB019', 19, 10, 'premium', 30, 'premium'),
('TICB020', 20, 10, 'valide', 28, 'standard'),

('TICB021', 21, 11, 'valide', 22, 'standard'),
('TICB022', 22, 11, 'valide', 22, 'standard'),
('TICB023', 23, 12, 'etudiant', 18, 'etudiant'),
('TICB024', 24, 12, 'valide', 22, 'standard'),
('TICB025', 25, 13, 'valide', 32, 'standard'),
('TICB026', 26, 13, 'premium', 35, 'premium'),
('TICB027', 27, 14, 'valide', 32, 'standard'),
('TICB028', 28, 14, 'annule', 0, 'standard'),
('TICB029', 29, 15, 'valide', 18, 'standard'),
('TICB030', 30, 15, 'valide', 18, 'standard'),

('TICB031', 31, 16, 'premium', 20, 'premium'),
('TICB032', 32, 16, 'valide', 18, 'standard'),
('TICB033', 33, 17, 'valide', 26, 'standard'),
('TICB034', 34, 17, 'valide', 26, 'standard'),
('TICB035', 35, 18, 'premium', 28, 'premium'),
('TICB036', 36, 18, 'valide', 26, 'standard'),
('TICB037', 37, 19, 'valide', 40, 'standard'),
('TICB038', 38, 19, 'valide', 40, 'standard'),
('TICB039', 39, 20, 'premium', 44, 'premium'),
('TICB040', 40, 20, 'valide', 40, 'standard'),

('TICB041', 41, 1, 'valide', 25, 'standard'),
('TICB042', 42, 1, 'premium', 28, 'premium'),
('TICB043', 43, 2, 'valide', 25, 'standard'),
('TICB044', 44, 2, 'annule', 0, 'standard'),
('TICB045', 45, 3, 'valide', 30, 'standard'),
('TICB046', 46, 3, 'premium', 33, 'premium'),
('TICB047', 47, 4, 'etudiant', 20, 'etudiant'),
('TICB048', 48, 4, 'valide', 30, 'standard'),
('TICB049', 49, 5, 'valide', 20, 'standard'),
('TICB050', 50, 5, 'premium', 23, 'premium'),

('TICB051', 51, 6, 'valide', 20, 'standard'),
('TICB052', 52, 6, 'valide', 20, 'standard'),
('TICB053', 53, 7, 'valide', 35, 'standard'),
('TICB054', 54, 7, 'annule', 0, 'standard'),
('TICB055', 55, 8, 'premium', 38, 'premium'),
('TICB056', 56, 8, 'valide', 35, 'standard'),
('TICB057', 57, 9, 'valide', 28, 'standard'),
('TICB058', 58, 9, 'valide', 28, 'standard'),
('TICB059', 59, 10, 'premium', 30, 'premium'),
('TICB060', 60, 10, 'valide', 28, 'standard'),

('TICB061', 1, 11, 'valide', 22, 'standard'),
('TICB062', 2, 11, 'valide', 22, 'standard'),
('TICB063', 3, 12, 'premium', 24, 'premium'),
('TICB064', 4, 12, 'valide', 22, 'standard'),
('TICB065', 5, 13, 'valide', 32, 'standard'),
('TICB066', 6, 13, 'premium', 35, 'premium'),
('TICB067', 7, 14, 'valide', 32, 'standard'),
('TICB068', 8, 14, 'valide', 32, 'standard'),
('TICB069', 9, 15, 'valide', 18, 'standard'),
('TICB070', 10, 15, 'annule', 0, 'standard'),

('TICB071', 11, 16, 'valide', 18, 'standard'),
('TICB072', 12, 16, 'premium', 20, 'premium'),
('TICB073', 13, 17, 'valide', 26, 'standard'),
('TICB074', 14, 17, 'premium', 29, 'premium'),
('TICB075', 15, 18, 'valide', 26, 'standard'),
('TICB076', 16, 18, 'valide', 26, 'standard'),
('TICB077', 17, 19, 'premium', 44, 'premium'),
('TICB078', 18, 19, 'valide', 40, 'standard'),
('TICB079', 19, 20, 'valide', 40, 'standard'),
('TICB080', 20, 20, 'premium', 44, 'premium'),

('TICB081', 21, 1, 'valide', 25, 'standard'),
('TICB082', 22, 1, 'valide', 25, 'standard'),
('TICB083', 23, 2, 'premium', 28, 'premium'),
('TICB084', 24, 2, 'valide', 25, 'standard'),
('TICB085', 25, 3, 'valide', 30, 'standard'),
('TICB086', 26, 3, 'etudiant', 18, 'etudiant'),
('TICB087', 27, 4, 'valide', 30, 'standard'),
('TICB088', 28, 4, 'premium', 32, 'premium'),
('TICB089', 29, 5, 'valide', 20, 'standard'),
('TICB090', 30, 5, 'premium', 23, 'premium'),

('TICB091', 31, 6, 'valide', 20, 'standard'),
('TICB092', 32, 6, 'premium', 22, 'premium'),
('TICB093', 33, 7, 'valide', 35, 'standard'),
('TICB094', 34, 7, 'premium', 39, 'premium'),
('TICB095', 35, 8, 'valide', 35, 'standard'),
('TICB096', 36, 8, 'annule', 0, 'standard'),
('TICB097', 37, 9, 'premium', 30, 'premium'),
('TICB098', 38, 9, 'valide', 28, 'standard'),
('TICB099', 39, 10, 'valide', 28, 'standard'),
('TICB100', 40, 10, 'premium', 30, 'premium');
