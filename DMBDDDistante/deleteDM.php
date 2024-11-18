<?php

include 'DM.php';

// Vérification de la table et de la condition WHERE
if (isset($_GET['table']) && isset($_GET['where'])) {
$table = $_GET['table'];

// Liste blanche des tables autorisées
$allowedTables = ['utilisateurs', 'tables', 'produit', 'commande', 'contient'];
if (!in_array($table, $allowedTables)) {
echo json_encode(["error" => "Table non autorisée."]);
exit;
}

// Récupération de la clause WHERE pour spécifier l'enregistrement à supprimer
$whereClause = $_GET['where'];

// Construction de la requête DELETE
$sql = "DELETE FROM $table WHERE $whereClause";
$stmt = $pdo->prepare($sql);

// Exécution de la requête
try {
if ($stmt->execute()) {
echo json_encode(["success" => "Suppression réussie dans la table $table."]);
} else {
echo json_encode(["error" => "Erreur lors de la suppression dans la table $table."]);
}
} catch (PDOException $e) {
echo json_encode(["error" => "Erreur PDO : " . $e->getMessage()]);
}

} else {
// Si les paramètres 'table' et 'where' sont manquants
echo json_encode(["error" => "Veuillez spécifier une table et une condition 'where' dans l'URL. Exemple : ?table=nom_de_la_table&where=condition"]);
}

?>
