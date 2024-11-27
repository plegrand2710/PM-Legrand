package com.pauline.dm.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pauline.dm.GestionBDD.DBAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Fragments extends Fragment {
    protected DBAdapter dbAdapter;
    protected String TAG = "DMProjet";
    HashMap<String, String> mapPlats = new HashMap<>();
    HashMap<String, String> mapAccompagnements = new HashMap<>();
    HashMap<String, String> mapBoissons = new HashMap<>();
    ArrayList<String> listePlats = new ArrayList<>();
    ArrayList<String> listeAccompagnements = new ArrayList<>();
    ArrayList<String> listeBoissons = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(requireContext());
        dbAdapter.open();

        logCursorContent(dbAdapter.getProduitsByCategorie("plat"), "DMProjet");
        mapPlats = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("plat"));
        mapBoissons = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("boisson"));
        mapAccompagnements = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("accompagnement"));

        Log.d(TAG, "onCreate: mapPlat = " + mapPlats);
        Log.d(TAG, "onCreate: mapAccompagnement = " + mapAccompagnements);

        listePlats.addAll(mapPlats.keySet());
        listeBoissons.addAll(mapBoissons.keySet());
        listeAccompagnements.addAll(mapAccompagnements.keySet());
    }

    private void logCursorContent(Cursor cursor, String tag) {
        if (cursor == null || cursor.getCount() == 0) {
            Log.d(tag, "Cursor is empty or null.");
            return;
        }

        StringBuilder logContent = new StringBuilder("Cursor Content:\n");
        try {
            int columnCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                logContent.append("Row ").append(cursor.getPosition()).append(":\n");
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    logContent.append("  ").append(columnName).append(": ").append(columnValue).append("\n");
                }
            }
        } catch (Exception e) {
            Log.e(tag, "Error reading cursor", e);
        } finally {
            cursor.moveToPosition(-1); // Reset cursor position after reading
        }

        Log.d(tag, logContent.toString());
    }

    protected HashMap<String, String> listeProduitParCategorie(Cursor cursor) {
        HashMap<String, String> produitsAvecCuisson = new HashMap<>();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int nomProduitIndex = cursor.getColumnIndexOrThrow(DBAdapter.KEY_NOMPRODUIT);
                    int cuissonIndex = cursor.getColumnIndexOrThrow(DBAdapter.KEY_CUISSON);

                    String nomProduit = cursor.getString(nomProduitIndex);
                    String cuisson = cursor.getString(cuissonIndex); // 1 ou 0

                    produitsAvecCuisson.put(nomProduit, cuisson);
                }
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la récupération des produits", e);
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(requireContext(), "Aucun produit trouvé pour cette catégorie.", Toast.LENGTH_SHORT).show();
        }
        return produitsAvecCuisson;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}