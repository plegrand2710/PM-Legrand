
/*-- Table UTILISATEURS
CREATE TABLE utilisateurs (
idUtilisateur int PRIMARY KEY AUTO_INCREMENT,
identifiant varchar(40) NOT NULL,
mdp varchar(30) NOT NULL,
role varchar(20) CHECK (role IN ('Superviseur', 'Responsable', 'Serveur', 'Cuisinier')) NOT NULL);

-- Table TABLES
CREATE TABLE tables (
numTable int PRIMARY KEY,
nbConvives int DEFAULT 0,
numColonne int NOT NULL,
numLigne int NOT NULL);

-- Table PRODUIT
CREATE TABLE produit (
idProduit int PRIMARY KEY AUTO_INCREMENT,
nomProduit varchar(200) NOT NULL,
categorie varchar(200) CHECK (categorie IN ('plat', 'boisson', 'accompagnement')) NOT NULL,
cuisson BOOLEAN NOT NULL,
prix REAL NOT NULL);

-- Table COMMANDE
CREATE TABLE commande (
idCommande int PRIMARY KEY AUTO_INCREMENT,
status varchar(50) CHECK (status IN ('en cours', 'réglée')) DEFAULT 'en cours',
cuisson_Commande varchar(50) CHECK (cuisson_Commande IN ('0', '1', '2', '3', '4')) DEFAULT '0' NOT NULL,
numTable int,
FOREIGN KEY (numTable) REFERENCES tables(numTable) ON DELETE SET NULL
);

-- Table CONTIENT
CREATE TABLE contient (
idCommande int,
idProduit int,
quantite int NOT NULL,
traitement_contient varchar(100) CHECK (traitement_contient IN ('à préparer', 'en cours', 'terminé')),
PRIMARY KEY (idCommande, idProduit),
FOREIGN KEY (idCommande) REFERENCES commande(idCommande) ON DELETE CASCADE,
FOREIGN KEY (idProduit) REFERENCES produit(idProduit) ON DELETE CASCADE
);

-- Insertion des données dans la table TABLES
INSERT INTO tables (numTable, nbConvives, numColonne, numLigne) VALUES
(4, 4, 1, 4),
(8, 6, 2, 4),
(16, 7, 4, 4),
(2, 3, 1, 2);

-- Insertion des données dans la table PRODUIT
INSERT INTO produit (nomProduit, categorie, cuisson, prix) VALUES
('Burger', 'plat', TRUE, 1250),
('Salade César', 'plat', FALSE, 900),
('Pizza Margherita', 'plat', FALSE, 1000),
('Lasagne', 'plat', FALSE, 1350),
('Pâtes Carbonara', 'plat', FALSE, 1100),
('Steak Frites', 'plat', TRUE, 1500),
('Entrecôte Frites', 'plat', TRUE, 1800),
('Poulet Rôti', 'plat', FALSE, 1400),
('Risotto aux Champignons', 'plat', FALSE, 1200),
('Tartare de Boeuf', 'plat', FALSE, 1300),
('Quiche Lorraine', 'plat', FALSE, 850),

('Coca-Cola', 'boisson', FALSE, 200),
('Eau Minérale', 'boisson', FALSE, 150),
('Jus d''Orange', 'boisson', FALSE, 250),
('Café Expresso', 'boisson', FALSE, 180),
('Thé Vert', 'boisson', FALSE, 200),
('Limonade', 'boisson', FALSE, 220),
('Smoothie Fraise', 'boisson', FALSE, 300),
('Vin Rouge', 'boisson', FALSE, 500),
('Vin Blanc', 'boisson', FALSE, 500),
('Bière', 'boisson', FALSE, 350),

('Frites', 'accompagnement', FALSE, 350),
('Riz', 'accompagnement', FALSE, 250),
('Salade Verte', 'accompagnement', FALSE, 200),
('Légumes Grillés', 'accompagnement', FALSE, 400),
('Purée de Pommes de Terre', 'accompagnement', FALSE, 300),
('Pommes de Terre Sautées', 'accompagnement', FALSE, 350),
('Gratin Dauphinois', 'accompagnement', FALSE, 450),
('Haricots Verts', 'accompagnement', FALSE, 280),
('Chips Maison', 'accompagnement', FALSE, 300),
('Onion Rings', 'accompagnement', FALSE, 320);
*/

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS DM_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Utilisation de la base de données
USE DM_DB;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    idUtilisateur INT AUTO_INCREMENT PRIMARY KEY,
    identifiant VARCHAR(50) NOT NULL UNIQUE,
    mdp VARCHAR(255) NOT NULL,
    role ENUM('Superviseur', 'Responsable', 'Serveur', 'Cuisinier') NOT NULL
);

-- Table des tables (topologie du restaurant)
CREATE TABLE IF NOT EXISTS tables (
    numTable INT PRIMARY KEY,
    nbConvives INT DEFAULT 0,
    numColonne INT NOT NULL,
    numLigne INT NOT NULL
);

-- Table des produits
CREATE TABLE IF NOT EXISTS produit (
    idProduit INT AUTO_INCREMENT PRIMARY KEY,
    nomProduit VARCHAR(100) NOT NULL,
    categorie ENUM('plat', 'boisson', 'accompagnement') NOT NULL,
    cuisson BOOLEAN NOT NULL DEFAULT 0,
    prix DECIMAL(10, 2) NOT NULL
);

-- Table des commandes
CREATE TABLE IF NOT EXISTS commande (
    idCommande INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('en cours', 'réglée') NOT NULL DEFAULT 'en cours',
    cuisson_Commande ENUM('0', '1', '2', '3', '4') NOT NULL DEFAULT '0',
    numTable INT,
    FOREIGN KEY (numTable) REFERENCES tables(numTable) ON DELETE SET NULL
);

-- Table contient (relation commande-produit)
CREATE TABLE IF NOT EXISTS contient (
    idCommande INT,
    idProduit INT,
    quantite INT NOT NULL,
    traitement_contient ENUM('à préparer', 'en cours', 'terminé'),
    PRIMARY KEY (idCommande, idProduit),
    FOREIGN KEY (idCommande) REFERENCES commande(idCommande) ON DELETE CASCADE,
    FOREIGN KEY (idProduit) REFERENCES produit(idProduit) ON DELETE CASCADE
);

-- Insertion des données dans `utilisateurs`
INSERT INTO utilisateurs (identifiant, mdp, role) VALUES
('admin', 'adminpassword', 'Superviseur'),
('responsable1', 'responsablepassword', 'Responsable'),
('serveur1', 'serveurpassword', 'Serveur'),
('cuisinier1', 'cuisinierpassword', 'Cuisinier');

-- Insertion des données dans `tables`
INSERT INTO tables (numTable, nbConvives, numColonne, numLigne) VALUES
(1, 4, 1, 1),
(2, 6, 2, 1),
(3, 2, 3, 1),
(4, 8, 4, 2);

-- Insertion des données dans `produit`
INSERT INTO produit (nomProduit, categorie, cuisson, prix) VALUES
-- Plats
('Burger', 'plat', 1, 12.50),
('Salade César', 'plat', 0, 9.00),
('Pizza Margherita', 'plat', 0, 10.00),
('Lasagne', 'plat', 0, 13.50),
('Pâtes Carbonara', 'plat', 0, 11.00),
('Steak Frites', 'plat', 1, 15.00),
('Entrecôte Frites', 'plat', 1, 18.00),
('Poulet Rôti', 'plat', 0, 14.00),
('Risotto aux Champignons', 'plat', 0, 12.00),
('Tartare de Boeuf', 'plat', 0, 13.00),
-- Boissons
('Coca-Cola', 'boisson', 0, 2.00),
('Eau Minérale', 'boisson', 0, 1.50),
('Jus d\'Orange', 'boisson', 0, 2.50),
('Café Expresso', 'boisson', 0, 1.80),
('Thé Vert', 'boisson', 0, 2.00),
('Limonade', 'boisson', 0, 2.20),
('Smoothie Fraise', 'boisson', 0, 3.00),
('Vin Rouge', 'boisson', 0, 5.00),
('Vin Blanc', 'boisson', 0, 5.00),
('Bière', 'boisson', 0, 3.50),
-- Accompagnements
('Frites', 'accompagnement', 0, 3.50),
('Riz', 'accompagnement', 0, 2.50),
('Salade Verte', 'accompagnement', 0, 2.00),
('Légumes Grillés', 'accompagnement', 0, 4.00),
('Purée de Pommes de Terre', 'accompagnement', 0, 3.00),
('Pommes de Terre Sautées', 'accompagnement', 0, 3.50),
('Gratin Dauphinois', 'accompagnement', 0, 4.50),
('Haricots Verts', 'accompagnement', 0, 2.80),
('Chips Maison', 'accompagnement', 0, 3.00),
('Onion Rings', 'accompagnement', 0, 3.20);

-- Insertion des données dans `commande`
INSERT INTO commande (status, cuisson_Commande, numTable) VALUES
('en cours', '0', 1),
('en cours', '1', 2),
('réglée', '3', 3);

-- Insertion des données dans `contient`
INSERT INTO contient (idCommande, idProduit, quantite, traitement_contient) VALUES
(1, 1, 2, 'à préparer'),
(1, 2, 1, 'en cours'),
(2, 5, 1, 'terminé'),
(2, 7, 1, 'en cours'),
(3, 10, 1, 'terminé');