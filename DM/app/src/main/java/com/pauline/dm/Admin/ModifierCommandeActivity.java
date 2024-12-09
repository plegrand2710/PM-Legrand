package com.pauline.dm.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class ModifierCommandeActivity extends AppCompatActivity {

    private Commande commande;
    private DBAdapter dbAdapter;

    private EditText etNumTable;
    private ListView produitsListView;
    private ProduitsCommandeAdapter produitsCommandeAdapter;
    private Button btnSave, btnBack;
    static final String TAG = "DMProjet";


    public static void start(Context context, Commande commande, Runnable refreshCallback) {
        Intent intent = new Intent(context, ModifierCommandeActivity.class);
        intent.putExtra("commande", commande);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_commande);

        etNumTable = findViewById(R.id.etNumTableCommande);
        produitsListView = findViewById(R.id.produitsListView);
        btnSave = findViewById(R.id.btnSaveCommande);
        btnBack = findViewById(R.id.btnBackCommande);

        commande = (Commande) getIntent().getSerializableExtra("commande");
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        chargerCommande();

        btnSave.setOnClickListener(v -> enregistrerCommande());
        btnBack.setOnClickListener(v -> finish());
    }

    private void chargerCommande() {
        if (commande != null) {
            commande = dbAdapter.getCommandeById(commande.getIdCommande());
            etNumTable.setText(String.valueOf(commande.getNumTable()));
            produitsCommandeAdapter = new ProduitsCommandeAdapter(
                    this,
                    commande.getProduits(),
                    dbAdapter,
                    commande.getIdCommande());
            produitsListView.setAdapter(produitsCommandeAdapter);



            //etNumTable.setText(String.valueOf(commande.getNumTable()));

            List<Contient> produitsAssocies = dbAdapter.getProduitsPourCommande(commande.getIdCommande());
            Log.d(TAG, "chargerCommande: produitsAssocies = " + produitsAssocies);
            produitsCommandeAdapter.updateData(produitsAssocies);
        }
    }

    private void enregistrerCommande() {
        try {
            int numTable = Integer.parseInt(etNumTable.getText().toString().trim());

            dbAdapter.updateCommande(commande.getIdCommande(), commande.getStatus(), commande.getCuissonCommande(), numTable);

            Toast.makeText(this, "Commande modifiée avec succès.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez remplir tous les champs correctement.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }

}