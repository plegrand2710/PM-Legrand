package com.pauline.dm.Cuisine;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireActionBDDDistante;
import com.pauline.dm.GestionBDD.GestionnaireSelectBDDDistante;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;

public class CuisineActivity extends AppCompatActivity {

    private static final String TAG = "DMProjet";
    private ListView platsListView;
    private DBAdapter dbAdapter;
    private CuisineAdapter cuisineAdapter;
    private List<PlatAccompagnementBoisson> platsBoissons;
    private Handler handler;
    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        platsListView = findViewById(R.id.platsListView);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        platsBoissons = new ArrayList<>();
        cuisineAdapter = new CuisineAdapter(this, platsBoissons);
        platsListView.setAdapter(cuisineAdapter);

        platsListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            PlatAccompagnementBoisson platBoisson = platsBoissons.get(position);

            int idProduit = platBoisson.getIdProduit();
            int idCommande = platBoisson.getIdCommande();

            String action = "update";
            String parameters = "table=contient&traitement_contient=terminé&where=idProduit=" + String.valueOf(idProduit) + "&idCommande=" + String.valueOf(idCommande);

            new GestionnaireActionBDDDistante(this, action, parameters, success -> {
                if (success) {
                    boolean updated = dbAdapter.marquerPlatServi(idProduit, idCommande);
                    if (updated) {
                        platsBoissons.remove(position);
                        cuisineAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Produit marqué comme servi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erreur lors de la mise à jour locale.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour distante.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        handler = new Handler();
        updateRunnable = this::synchroniserEtRafraichir;
        handler.post(updateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
        if (handler != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    private void synchroniserEtRafraichir() {
        new GestionnaireSelectBDDDistante(this, "commande", dbAdapter);
        new GestionnaireSelectBDDDistante(this, "contient", dbAdapter);
        new GestionnaireSelectBDDDistante(this, "produit", dbAdapter);

        handler.postDelayed(() -> {
            try {
                platsBoissons.clear();
                platsBoissons.addAll(dbAdapter.getPlatsBoissonsEnAttente());
                cuisineAdapter.notifyDataSetChanged();
                Log.d(TAG, "synchroniserEtRafraichir: Mise à jour de l'affichage réussie.");
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la mise à jour de l'affichage : ", e);
            }
        }, 1000);

        handler.postDelayed(updateRunnable, 30000);
    }
}