package com.pauline.dm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.Commande.CommandeActivity;
import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireSelectBDDDistante;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DMProjet";
    private DBAdapter db;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Button btnValiderCommande;
    private TextView tv1, tv2;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBAdapter(this);
        db.open();

        GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "tables", db);
        GestionnaireSelectBDDDistante gestionnaire1 = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);
        GestionnaireSelectBDDDistante gestionnaire2 = new GestionnaireSelectBDDDistante(MainActivity.this, "commande", db);
        GestionnaireSelectBDDDistante gestionnaire3 = new GestionnaireSelectBDDDistante(MainActivity.this, "contient", db);
        GestionnaireSelectBDDDistante gestionnaire4 = new GestionnaireSelectBDDDistante(MainActivity.this, "utilisateurs", db);


        startActivity(new Intent(MainActivity.this, CommandeActivity.class));
        /*
        tv1 = findViewById(R.id.textView3);
        tv2 = findViewById(R.id.textView4);
        btnValiderCommande = findViewById(R.id.validerCommande);
        progressBar = findViewById(R.id.progressBar);

        db = new DBAdapter(this);
        db.open();

        btnValiderCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);
                afficherDonneesLocales(tv1);
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
                    Thread.sleep(15000);
                    /*GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);
                    String params = "table=produit&idProduit=997&nomProduit=ProduitInsere&categorie=plat&cuisson=1&prix=999";
                    new GestionnaireActionBDDDistante(MainActivity.this, "insert", params);
                    GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);

                    String params = "table=produit&nomProduit=ProduitMisAJour&categorie=plat&cuisson=1&prix=1500&where=idProduit=997";
                    new GestionnaireActionBDDDistante(MainActivity.this, "update", params);
                    GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);*/

                    /*String params = "table=produit&where=idProduit=997";
                    new GestionnaireActionBDDDistante(MainActivity.this, "delete", params);
                    GestionnaireSelectBDDDistante gestionnaire = new GestionnaireSelectBDDDistante(MainActivity.this, "produit", db);


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
        }*/
    }
}