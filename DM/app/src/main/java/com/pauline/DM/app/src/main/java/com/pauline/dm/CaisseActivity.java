package com.pauline.dm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CaisseActivity extends AppCompatActivity {

    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9, ib10, ib11, ib12, ib13, ib14, ib15, ib16;
    Button bValide;
    EditText etNum ;
    ArrayList<Integer> tableOccupe = null;
    Integer nbTable = 16 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        bValide = (Button) findViewById(R.id.valide);
        etNum = (EditText) findViewById(R.id.NumEntre);
        ib1 = (ImageButton) findViewById(R.id.table1);
        ib2 = (ImageButton) findViewById(R.id.table2);
        ib3 = (ImageButton) findViewById(R.id.table3);
        ib4 = (ImageButton) findViewById(R.id.table4);
        ib5 = (ImageButton) findViewById(R.id.table5);
        ib6 = (ImageButton) findViewById(R.id.table6);
        ib7 = (ImageButton) findViewById(R.id.table7);
        ib8 = (ImageButton) findViewById(R.id.table8);
        ib9 = (ImageButton) findViewById(R.id.table9);
        ib10 = (ImageButton) findViewById(R.id.table10);
        ib11 = (ImageButton) findViewById(R.id.table11);
        ib12 = (ImageButton) findViewById(R.id.table12);
        ib13 = (ImageButton) findViewById(R.id.table13);
        ib14 = (ImageButton) findViewById(R.id.table14);
        ib15 = (ImageButton) findViewById(R.id.table15);
        ib16 = (ImageButton) findViewById(R.id.table16);


        // Initialiser la liste de commande
        /*commandeList = new ArrayList<>();

        btnSelectTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableNumber = etNumTable.getText().toString();
                if (!tableNumber.isEmpty()) {
                    // Charger la commande de la table sélectionnée
                    chargerCommandeTable(Integer.parseInt(tableNumber));
                } else {
                    Toast.makeText(CaisseActivity.this, "Veuillez entrer un numéro de table", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnValiderNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stocker l'information que la note est réglée
                validerNote();
            }
        });
    }

    // Exemple de fonction pour charger les commandes d'une table
    private void chargerCommandeTable(int tableNumber) {
        // Simuler des données de commande
        commandeList.clear();

        HashMap<String, String> item1 = new HashMap<>();
        item1.put("plat", "Pizza");
        item1.put("prix", "12.50");
        commandeList.add(item1);

        HashMap<String, String> item2 = new HashMap<>();
        item2.put("plat", "Coca");
        item2.put("prix", "3.00");
        commandeList.add(item2);

        // Calculer le total de la commande
        totalCommande = 0.0;
        for (HashMap<String, String> item : commandeList) {
            totalCommande += Double.parseDouble(item.get("prix"));
        }

        // Afficher le total
        tvTotalCommande.setText("Total : " + String.format("%.2f", totalCommande) + " €");

        // Mettre à jour la liste des commandes dans ListView
        // Utiliser un adapter personnalisé ou ArrayAdapter pour afficher les commandes
        CommandeAdapter adapter = new CommandeAdapter(this, commandeList);
        lvCommande.setAdapter(adapter);
    }

    // Exemple de fonction pour valider la note
    private void validerNote() {
        Toast.makeText(CaisseActivity.this, "Note validée et stockée", Toast.LENGTH_SHORT).show();

        // Simuler l'envoi des données au serveur
        // Envoyer les données de la table et du montant au serveur
        // Code pour l'envoi des données...

        // Réinitialiser l'interface après validation
        etNumTable.setText("");
        tvTotalCommande.setText("Total : 0.00 €");
        commandeList.clear();
        lvCommande.setAdapter(null);*/
    }
}

