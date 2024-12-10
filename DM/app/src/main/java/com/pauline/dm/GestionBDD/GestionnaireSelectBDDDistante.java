package com.pauline.dm.GestionBDD;

import android.content.Context;
import android.util.Log;
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
    private static final String TAG = "DMProjet";

    private static final String BASE_URL = "http://192.168.3.140:8888/DMBDDDistante/";
    //private static final String BASE_URL = "http://10.16.0.41/";
    private static RequestQueue requestQueue;
    private final Context context;

    public GestionnaireSelectBDDDistante(Context context, String tableName, DBAdapter dbAdapter) {
        this.context = context;

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        fetchTableData(tableName, dbAdapter);
    }

    private void fetchTableData(String tableName, DBAdapter dbAdapter) {
        String url = BASE_URL + "selectDM.php?table=" + tableName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(TAG, "Réponse brute reçue : " + response.toString());
                    handleResponse(response, tableName, dbAdapter);
                },
                this::handleError
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void handleResponse(JSONArray response, String tableName, DBAdapter dbAdapter) {
        Log.d(TAG, "Réponse JSON : " + response.toString());

        try {
            dbAdapter.open();

            dbAdapter.clearTable(tableName);

            for (int i = 0; i < response.length(); i++) {
                dbAdapter.insertFromJson(tableName, response.getJSONObject(i));
            }

            Toast.makeText(context, "Table " + tableName + " mise à jour avec succès !", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Erreur lors du traitement des données JSON." + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "handleResponse: erreur lors du traitement des données JSON = " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleError(VolleyError error) {
        Toast.makeText(context, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_SHORT).show();
        error.printStackTrace();
    }
}