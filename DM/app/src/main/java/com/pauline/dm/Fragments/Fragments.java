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

public abstract class Fragments extends Fragment {
    protected DBAdapter dbAdapter;
    protected String TAG = "DMProjet";
    ArrayList<String> listePlats = new ArrayList<>();
    ArrayList<String> listeAccompagnements = new ArrayList<>();
    ArrayList<String> listeBoissons = new ArrayList<>();
    ArrayAdapter<String> adapterPlats = null ;
    ArrayAdapter<String> adapterAccompagnements = null ;
    ArrayAdapter<String> adapterBoissons = null ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(requireContext());
        dbAdapter.open();

        listePlats = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("plat"));
        listeBoissons= listeProduitParCategorie(dbAdapter.getProduitsByCategorie("boisson"));
        listeAccompagnements = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("accompagnement"));
    }

    protected ArrayList<String> listeProduitParCategorie(Cursor cursor) {
        ArrayList<String> listeProduits = new ArrayList<>();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int nomProduitIndex = cursor.getColumnIndexOrThrow(DBAdapter.KEY_NOMPRODUIT);
                    String nomProduit = cursor.getString(nomProduitIndex);
                    listeProduits.add(nomProduit);
                }
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la récupération des produits", e);
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(requireContext(), "Aucun produit trouvé pour cette catégorie.", Toast.LENGTH_SHORT).show();
        }
        return listeProduits;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}