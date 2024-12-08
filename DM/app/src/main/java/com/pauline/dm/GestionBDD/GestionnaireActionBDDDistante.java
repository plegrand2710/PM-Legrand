package com.pauline.dm.GestionBDD;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GestionnaireActionBDDDistante {private static final String BASE_URL = "http://192.168.3.140:8888/DMBDDDistante/";
    private Context context;
    private String action;
    private String parameters;

    public GestionnaireActionBDDDistante(Context context, String action, String parameters) {
        this.context = context;
        this.action = action;
        this.parameters = parameters;

        executerAction();
    }

    private void executerAction() {
        String url = BASE_URL + action + "DM.php?" + parameters;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            Toast.makeText(context, "Réponse : " + response, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Réponse vide du serveur.", Toast.LENGTH_SHORT).show();
                        }
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

        requestQueue.add(stringRequest);
    }
}