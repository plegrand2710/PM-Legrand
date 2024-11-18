<?php 

include 'DM.php' ;

// Préparation et exécution de la requête
$stmt = $pdo->prepare("SELECT * FROM $table");
$stmt->execute();

// Récupération des résultats sous forme de tableau associatif
$results = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Conversion en JSON et affichage
header('Content-Type: application/json');
echo json_encode($results);

} else {
// Si le paramètre "table" est manquant
echo json_encode(["error" => "Veuillez spécifier une table dans l'URL, par exemple ?table=nom_de_la_table"]);
}

?>
