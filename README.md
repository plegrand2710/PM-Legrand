# PM-Legrand
**Gestion de Salle pour Restaurant**

Ce projet est une application de gestion de salle pour un restaurant, permettant aux utilisateurs de gérer les commandes, les paiements, les utilisateurs, ainsi que les activités en cuisine. L'application utilise une base de données pour stocker les informations et offre différents accès en fonction des rôles des utilisateurs.

## Fonctionnalités

### Modules disponibles
L'application est divisée en quatre modules principaux :

- **Commandes** : Permet de prendre les commandes des clients.
- **Caisse** : Permet de faire payer un client qui a terminé son repas.
- **Admin** : Permet d'ajouter, supprimer ou modifier des utilisateurs, des plats et la disposition de la salle.
- **Cuisine** : Affiche les plats à préparer dans l'ordre et permet de marquer les plats comme "prêts".

### Gestion des rôles
L'application offre différents niveaux d'accès en fonction des rôles :
- **Superviseur** : Accès à tous les modules.
- **Responsable de salle** : Accès aux modules Caisse, Cuisine et Commandes.
- **Serveur** : Accès uniquement au module Commandes.
- **Cuisinier** : Accès uniquement au module Cuisine.

### Fonctionnalités supplémentaires
- **Vérification et mise à jour des données** : Lors de la connexion, l'application vérifie les données locales par rapport au serveur et les met à jour si nécessaire. Un bouton permet également de forcer la mise à jour.
- **Enregistrement des commandes** : Les commandes et les menus sont stockés localement. Le bouton "Valider commande" permet de synchroniser les données avec le serveur.

## Structure de la base de données
Le projet utilise une base de données SQLite avec les tables suivantes :

- **Utilisateurs** : Stocke les informations des utilisateurs, leurs identifiants, mots de passe et rôles.
- **Tables** : Stocke les informations des tables dans le restaurant (numéro, convives, disposition).
- **Produit** : Contient les informations des plats et boissons (nom, catégorie, prix).
- **Commande** : Gère les commandes passées par les clients (numéro de table, statut, cuisson).
- **Contient** : Associe les produits aux commandes (quantité, état de préparation).

## Gestion des branches

### Branches principales
- `main` : Branche de production avec la version stable de l'application.
- `baseDeDonnées` : Branche pour le développement et la gestion de la base de données.
- `commandes`, `caisses`, `modules`, `cuisine`, `admin` : Branches pour chaque fonctionnalité ou module spécifique.

### Remarque
**NB** : Je n'avais pas très bien compris comment fonctionnait GitHub au début donc je n'ai pas utilisé la fonctionnalité des branches. À partir du 03 nov. 24, la gestion est correcte.

## Lancer l'application

1. **Connexion** : Connectez-vous avec votre identifiant et votre mot de passe pour accéder aux modules selon votre rôle.
2. **Prendre une commande** : Allez dans le module Commandes, sélectionnez une table, saisissez le nombre de convives, puis ajoutez les plats, boissons, et instructions spécifiques.
3. **Paiement** : Dans le module Caisse, sélectionnez la table pour afficher la note, puis validez le paiement une fois le client prêt à payer.
4. **Admin et gestion de la salle** : Dans le module Admin, ajoutez ou modifiez les utilisateurs et la disposition de la salle.

## Licence
Ce projet est sous licence MIT.
