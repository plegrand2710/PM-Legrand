package com.pauline.dm.GestionBDD;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pauline.dm.GestionBDD.DBAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GestionnaireSelectBDDDistante {

    private static final String BASE_URL = "http://192.168.3.128:8888/DMBDDDistante/";
    private static RequestQueue requestQueue; // Singleton RequestQueue
    private final Context context;

    public GestionnaireSelectBDDDistante(Context context, String tableName, DBAdapter dbAdapter) {
        this.context = context;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext()); // Initialise la RequestQueue une seule fois
        }

        fetchTableData(tableName, dbAdapter);
    }

    private void fetchTableData(String tableName, DBAdapter dbAdapter) {
        String url = BASE_URL + "selectDM.php?table=" + tableName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> handleResponse(response, tableName, dbAdapter),
                this::handleError
        );

        // Ajoute la requête à la RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void handleResponse(JSONArray response, String tableName, DBAdapter dbAdapter) {
        try {
            dbAdapter.open(); // Ouvre la base de données locale

            // Supprime toutes les données existantes pour la table spécifiée
            dbAdapter.clearTable(tableName);

            // Insère les nouvelles données
            for (int i = 0; i < response.length(); i++) {
                // Convertissez les données JSON en objets appropriés et insérez-les
                dbAdapter.insertFromJson(tableName, response.getJSONObject(i));
            }

            dbAdapter.close();
            Toast.makeText(context, "Table " + tableName + " mise à jour avec succès !", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Erreur lors du traitement des données JSON.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void handleError(VolleyError error) {
        Toast.makeText(context, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_SHORT).show();
        error.printStackTrace();
    }
}