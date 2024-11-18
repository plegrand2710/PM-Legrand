package com.pauline.dm;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GestionnaireSelectBDDDistante {

    String TAG = "DMProjet";
    private static final String BASE_URL = "http://192.168.3.128:8888/DMBDDDistante/";
    private Context context;
    private String tableName;
    private DBAdapter dbAdapter;

    public GestionnaireSelectBDDDistante(Context context, String tableName, DBAdapter dbAdapter) {
        this.context = context;
        this.tableName = tableName;
        this.dbAdapter = dbAdapter;
        dbAdapter.open();
        recupererTable();
    }

    private void recupererTable() {
        String url = BASE_URL + "selectDM.php?table=" + tableName;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //afficherResultats(response);
                        traiterDonnees(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erreur de connexion : " + error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void afficherResultats(JSONArray response) {
        try {
            StringBuilder resultat = new StringBuilder("Données pour la table " + tableName + " :\n");
            for (int i = 0; i < response.length(); i++) {
                JSONArray ligne = response.optJSONArray(i);

                if (ligne != null) {
                    for (int j = 0; j < ligne.length(); j++) {
                        resultat.append("Colonne ").append(j + 1).append("=").append(ligne.optString(j));
                        if (j < ligne.length() - 1) {
                            resultat.append(", ");
                        }
                    }
                    resultat.append("\n");
                } else {
                    resultat.append(response.getString(i)).append("\n");
                }
            }

            Toast.makeText(context, resultat.toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Erreur de traitement JSON.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void traiterDonnees(JSONArray response) {
        try {
            viderTable();

            for (int i = 0; i < response.length(); i++) {
                JSONObject ligne = response.getJSONObject(i);

                switch (tableName) {
                    case "utilisateurs":
                        insererUtilisateurs(ligne);
                        break;
                    case "tables":
                        insererTables(ligne);
                        break;
                    case "produit":
                        insererProduits(ligne);
                        break;
                    case "commande":
                        insererCommandes(ligne);
                        break;
                    case "contient":
                        insererContient(ligne);
                        break;
                    default:
                        Toast.makeText(context, "Table non prise en charge : " + tableName, Toast.LENGTH_SHORT).show();
                }
            }

            dbAdapter.close();
            Toast.makeText(context, "Table " + tableName + " mise à jour avec succès.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Erreur de traitement JSON.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void viderTable() {
        switch (tableName) {
            case "utilisateurs":
                dbAdapter.db.delete(DBAdapter.TABLE_UTILISATEURS, null, null);
                break;
            case "tables":
                dbAdapter.db.delete(DBAdapter.TABLE_TABLES, null, null);
                break;
            case "produit":
                dbAdapter.db.delete(DBAdapter.TABLE_PRODUIT, null, null);
                break;
            case "commande":
                dbAdapter.db.delete(DBAdapter.TABLE_COMMANDE, null, null);
                break;
            case "contient":
                dbAdapter.db.delete(DBAdapter.TABLE_CONTIENT, null, null);
                break;
            default:
                Toast.makeText(context, "Impossible de vider la table : " + tableName, Toast.LENGTH_SHORT).show();
        }
    }

    private void insererUtilisateurs(JSONObject row) throws JSONException {
        int idUtilisateur = row.getInt("idUtilisateur");
        String identifiant = row.getString("identifiant");
        String mdp = row.getString("mdp");
        String role = row.getString("role");

        dbAdapter.insertUtilisateur(idUtilisateur, identifiant, mdp, role);
    }

    private void insererTables(JSONObject row) throws JSONException {
        int numTable = row.getInt("numTable");
        int nbConvives = row.getInt("nbConvives");
        int nbColonne = row.getInt("numColonne");
        int nbLigne = row.getInt("numLigne");

        dbAdapter.insertTable(numTable, nbConvives, nbColonne, nbLigne);
    }

    private void insererProduits(JSONObject row) throws JSONException {
        int idProduit = row.getInt("idProduit");
        String nomProduit = row.getString("nomProduit");
        String categorie = row.getString("categorie");
        boolean cuisson = Boolean.valueOf(String.valueOf(row.getInt("cuisson")));
        double prix = row.getDouble("prix");

        dbAdapter.insertProduit(idProduit, nomProduit, categorie, cuisson, prix);
        Log.d(TAG, "insererProduits: insertion okay");
    }

    private void insererCommandes(JSONObject row) throws JSONException {
        int idCommande = row.getInt("idCommande");
        String status = row.getString("status");
        String cuisson = row.getString("cuisson_Commande");
        int numTable = row.getInt("numTable");

        dbAdapter.insertCommande(idCommande, status, cuisson, numTable);
    }

    private void insererContient(JSONObject row) throws JSONException {
        int idCommande = row.getInt("idCommande");
        int idProduit = row.getInt("idProduit");
        int quantite = row.getInt("quantite");
        String traitement = row.getString("traitement_contient");

        dbAdapter.insertContient(idCommande, idProduit, quantite, traitement);
    }
}