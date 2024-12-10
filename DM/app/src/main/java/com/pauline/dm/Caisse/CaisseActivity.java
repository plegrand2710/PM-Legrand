package com.pauline.dm.Caisse;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireActionBDDDistante;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;

public class CaisseActivity extends AppCompatActivity {

    private static final String TAG = "CaisseActivity";

    private EditText etNumTable;
    private Button btnRechercherTable, btnValiderPaiement;
    private ListView lvDetailsCommande;
    private TextView tvTotal;

    private DBAdapter dbAdapter;
    private ArrayAdapter<String> commandeAdapter;
    private List<String> detailsCommande;
    private int idCommande;
    private double totalGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caisse);

        etNumTable = findViewById(R.id.etNumTable);
        btnRechercherTable = findViewById(R.id.btnRechercherTable);
        btnValiderPaiement = findViewById(R.id.btnValiderPaiement);
        lvDetailsCommande = findViewById(R.id.lvDetailsCommande);
        tvTotal = findViewById(R.id.tvTotal);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        detailsCommande = new ArrayList<>();
        commandeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, detailsCommande);
        lvDetailsCommande.setAdapter(commandeAdapter);

        btnRechercherTable.setOnClickListener(v -> rechercherCommandeParTable());

        btnValiderPaiement.setOnClickListener(v -> validerPaiement());
    }

    private void rechercherCommandeParTable() {
        String numTable = etNumTable.getText().toString().trim();

        if (numTable.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un numéro de table.", Toast.LENGTH_SHORT).show();
            return;
        }

        idCommande = dbAdapter.getCommandeIdParTable(Integer.parseInt(numTable));

        if (idCommande == -1) {
            Toast.makeText(this, "Aucune commande trouvée pour cette table.", Toast.LENGTH_SHORT).show();
            return;
        }

        detailsCommande.clear();
        detailsCommande.addAll(dbAdapter.getDetailsCommande(idCommande));
        totalGlobal = dbAdapter.getTotalCommande(idCommande);

        commandeAdapter.notifyDataSetChanged();
        tvTotal.setText("Total : " + totalGlobal + " €");
    }

    private void validerPaiement() {
        if (idCommande == -1) {
            Toast.makeText(this, "Aucune commande à valider.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updatedLocal = dbAdapter.marquerCommandePayee(idCommande);

        if (updatedLocal) {
            String action = "update";
            String parameters = "table=commande&status=réglée&where=idCommande=" + idCommande;

            new GestionnaireActionBDDDistante(this, action, parameters, success -> {
                if (success) {
                    Toast.makeText(this, "Commande réglée avec succès.", Toast.LENGTH_SHORT).show();
                    resetUI();
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour distante.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Erreur locale lors de la mise à jour.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetUI() {
        etNumTable.setText("");
        detailsCommande.clear();
        commandeAdapter.notifyDataSetChanged();
        tvTotal.setText("Total : 0 €");
        idCommande = -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}