<?php

include 'DM.php';

// Récupération des colonnes et valeurs de l'URL
$columns = [];
$values = [];
$placeholders = [];

foreach ($_GET as $key => $value) {
if ($key !== 'table') { // Exclure le paramètre "table"
$columns[] = preg_replace('/[^a-zA-Z0-9_]/', '', $key); // Nettoyage des noms de colonnes
$values[] = $value;
$placeholders[] = '?';
}
}

// Construction de la requête d'insertion
$sql = "INSERT INTO $table (" . implode(", ", $columns) . ") VALUES (" . implode(", ", $placeholders) . ")";
$stmt = $pdo->prepare($sql);

// Exécution de la requête
try {
if ($stmt->execute($values)) {
echo json_encode(["success" => "L'insertion dans la table $table a réussi."]);
} else {
echo json_encode(["error" => "Erreur lors de l'insertion dans la table $table."]);
}
} catch (PDOException $e) {
echo json_encode(["error" => "Erreur PDO : " . $e->getMessage()]);
}

} else {
// Si le paramètre "table" est manquant
echo json_encode(["error" => "Veuillez spécifier une table et les paramètres dans l'URL, par exemple ?table=nom_de_la_table&colonne1=valeur1&colonne2=valeur2"]);
}
?>
