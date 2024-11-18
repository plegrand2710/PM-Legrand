package com.pauline.dm;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DMProjet";
    private DBAdapter db;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Button btnValiderCommande;
    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.textView3);
        tv2 = findViewById(R.id.textView4);
        btnValiderCommande = findViewById(R.id.validerCommande);
        progressBar = findViewById(R.id.progressBar);

        db = new DBAdapter(this);
        db.open();

        afficherDonneesLocales(tv1);

        btnValiderCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireSelect gestionnaire = new GestionnaireSelect(MainActivity.this, "produit", db);
                processusValidation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    private void processusValidation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);

                    GestionnaireSelect gestionnaire = new GestionnaireSelect(MainActivity.this, "produit", db);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Commande validée avec succès", Toast.LENGTH_SHORT).show();
                            afficherDonneesLocales(tv2); // Mise à jour de la TextView
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void afficherDonneesLocales(TextView tv) {
        if (tv == null) {
            Log.e(TAG, "afficherDonneesLocales: TextView non initialisée");
            return;
        }

        Cursor cursor = db.db.query(DBAdapter.TABLE_PRODUIT, null, null, null, null, null, null);

        if (cursor != null) {
            StringBuilder result = new StringBuilder("Données locales:\n");

            while (cursor.moveToNext()) {
                int idProduitIndex = cursor.getColumnIndex(DBAdapter.KEY_IDPRODUIT);
                int nomProduitIndex = cursor.getColumnIndex(DBAdapter.KEY_NOMPRODUIT);
                int categorieIndex = cursor.getColumnIndex(DBAdapter.KEY_CATEGORIE);
                int prixIndex = cursor.getColumnIndex(DBAdapter.KEY_PRIX);

                int idProduit = cursor.getInt(idProduitIndex);
                String nomProduit = cursor.getString(nomProduitIndex);
                String categorie = cursor.getString(categorieIndex);
                double prix = cursor.getDouble(prixIndex);

                result.append("ID: ").append(idProduit)
                        .append(", Nom: ").append(nomProduit)
                        .append(", Catégorie: ").append(categorie)
                        .append(", Prix: ").append(prix)
                        .append("\n");
            }
            cursor.close();
            Log.d(TAG, "Données locales : \n" + result);
            tv.setText(result.toString());
        } else {
            tv.setText("Aucune donnée locale trouvée.");
            Log.d(TAG, "Aucune donnée locale trouvée.");
        }
    }
}