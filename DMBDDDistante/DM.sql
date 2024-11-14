
-- Table UTILISATEURS
CREATE TABLE utilisateurs (
idUtilisateur int PRIMARY KEY AUTO_INCREMENT,
identifiant varchar(40) UNIQUE NOT NULL,
mdp varchar(30) NOT NULL,
role varchar(20) CHECK (role IN ('Superviseur', 'Responsable', 'Serveur', 'Cuisinier')) NOT NULL
);

-- Table TABLES
CREATE TABLE tables (
numTable int PRIMARY KEY,
nbConvives int DEFAULT 0,
numColonne int NOT NULL,
numLigne int NOT NULL
);

-- Table PRODUIT
CREATE TABLE produit (
idProduit int PRIMARY KEY AUTO_INCREMENT,
nomProduit varchar(200) NOT NULL,
categorie varchar(200) CHECK (categorie IN ('plat', 'boisson', 'accompagnement')) NOT NULL,
cuisson BOOLEAN NOT NULL,
prix REAL NOT NULL
);

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
