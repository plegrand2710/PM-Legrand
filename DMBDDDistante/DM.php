<?php
$host = 'localhost';
$dbname = 'DM';
$user = 'root';
//$pass = '#pl2004UPF';
$pass = 'root';

try {
// Création de la connexion PDO
$pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $user, $pass);

// Définir le mode d'erreur de PDO sur Exception
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

//echo "Connexion réussie à la base de données.";
} catch (PDOException $e) {
// Gérer l'erreur de connexion
echo "Erreur de connexion : " . $e->getMessage();
}
/*
// Vérification de la table et des paramètres
if (isset($_GET['table'])) {
$table = $_GET['table'];

// Liste blanche des tables autorisées
$allowedTables = ['utilisateurs', 'tables', 'produit', 'commande', 'contient'];
if (!in_array($table, $allowedTables)) {
echo json_encode(["error" => "Table non autorisée."]);
exit;
}*/


?>
