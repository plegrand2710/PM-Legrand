package com.pauline.dm.GestionBDD;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GestionnaireActionBDDDistante {
    private static final String BASE_URL = "http://192.168.3.140:8888/DMBDDDistante/";
    //private static final String BASE_URL = "http://10.16.0.41/";
    private static final String TAG = "DMProjet";

    public interface ActionCallback {
        void onComplete(boolean success);
    }

    public GestionnaireActionBDDDistante(Context context, String action, String parameters, ActionCallback callback) {
        String url = BASE_URL + action + "DM.php?" + parameters;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    boolean success = response.contains("success");
                    if (success) {
                        Toast.makeText(context, "Action réussie : " + action, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erreur sur l'action : " + action, Toast.LENGTH_SHORT).show();
                    }

                    if (callback != null) {
                        callback.onComplete(success);
                    }
                },
                error -> {
                    Toast.makeText(context, "Erreur de connexion : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    if (callback != null) {
                        callback.onComplete(false);
                    }
                }
        );

        requestQueue.add(stringRequest);
    }
}