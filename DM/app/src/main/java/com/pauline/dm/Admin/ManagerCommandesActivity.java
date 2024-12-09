package com.pauline.dm.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class ManagerCommandesActivity extends AppCompatActivity {
    private static final String TAG = "DMProjet";

    private DBAdapter dbAdapter;
    private ListView commandesListView;
    private CommandesAdapter commandesAdapter;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_commandes);
        commandesListView = findViewById(R.id.commandesListView);
        btnBack = findViewById(R.id.btnBackCommandes);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        chargerCommandes();

        btnBack.setOnClickListener(v -> finish());
    }

    private void chargerCommandes() {
        List<Commande> commandes = dbAdapter.getCommandeEnCours();
        commandesAdapter = new CommandesAdapter(this, commandes, dbAdapter, this::chargerCommandes);
        commandesListView.setAdapter(commandesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}