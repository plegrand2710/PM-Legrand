<?php

include 'DM.php';

// Vérification de la connexion à la base de données
if (!isset($pdo)) {
echo json_encode(["error" => "Erreur de connexion à la base de données."]);
exit;
}

// Vérification de la table et des paramètres
if (isset($_GET['table']) && isset($_GET['where'])) {
$table = $_GET['table'];

// Liste blanche des tables autorisées
$allowedTables = ['utilisateurs', 'tables', 'produit', 'commande', 'contient'];
if (!in_array($table, $allowedTables)) {
echo json_encode(["error" => "Table non autorisée."]);
exit;
}

// Récupération des colonnes et valeurs de l'URL pour la mise à jour
$setClauses = [];
$values = [];
$whereClause = $_GET['where'];

// Récupération des colonnes et valeurs pour l'UPDATE, en excluant 'table' et 'where'
foreach ($_GET as $key => $value) {
if ($key !== 'table' && $key !== 'where') { // Exclure les paramètres 'table' et 'where'
$column = preg_replace('/[^a-zA-Z0-9_]/', '', $key); // Nettoyage des noms de colonnes
$setClauses[] = "$column = ?";
$values[] = $value;
}
}

// Ajout du WHERE en dernière position pour éviter toute manipulation potentielle
$sql = "UPDATE $table SET " . implode(", ", $setClauses) . " WHERE $whereClause";
$stmt = $pdo->prepare($sql);

// Exécution de la requête
try {
if ($stmt->execute($values)) {
echo json_encode(["success" => "Mise à jour réussie dans la table $table."]);
} else {
echo json_encode(["error" => "Erreur lors de la mise à jour dans la table $table."]);
}
} catch (PDOException $e) {
echo json_encode(["error" => "Erreur PDO : " . $e->getMessage()]);
}

} else {
// Si les paramètres 'table' et 'where' sont manquants
echo json_encode(["error" => "Veuillez spécifier une table, une condition 'where', et les paramètres à mettre à jour dans l'URL. Exemple : ?table=nom_de_la_table&colonne1=valeur1&colonne2=valeur2&where=condition"]);
}

?>
