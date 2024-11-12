package com.pauline.dm;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import android.util.Log;
import java.util.ArrayList;

public class FragmentConvive extends Fragment {
    View view;
    private DBAdapter dbAdapter;
    ArrayList<String> listePlats = new ArrayList<>();
    ArrayList<String> listeAccompagnements = new ArrayList<>();
    ArrayList<String> listeBoissons = new ArrayList<>();
    String TAG = "DMProjet";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_convive, container, false);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.open();

        listePlats = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("plat"));
        listeBoissons= listeProduitParCategorie(dbAdapter.getProduitsByCategorie("boisson"));
        listeAccompagnements = listeProduitParCategorie(dbAdapter.getProduitsByCategorie("accompagnement"));

        Log.d(TAG, "Liste de plats: " + listePlats.toString());
        Log.d(TAG, "Liste de boissons: " + listeBoissons.toString());
        Log.d(TAG, "Liste d'accompagnements: " + listeAccompagnements.toString());

        ArrayAdapter<String> adapterPlats = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listePlats);
        AutoCompleteTextView autoCompletePlat = view.findViewById(R.id.platEditText);
        autoCompletePlat.setThreshold(1);
        autoCompletePlat.setAdapter(adapterPlats);

        ArrayAdapter<String> adapterAccompagnements = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        AutoCompleteTextView autoCompleteAccompagnement = view.findViewById(R.id.accompagnementEditText);
        autoCompleteAccompagnement.setThreshold(1);
        autoCompleteAccompagnement.setAdapter(adapterAccompagnements);

        ArrayAdapter<String> adapterBoissons = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeBoissons);
        AutoCompleteTextView autoCompleteBoisson = view.findViewById(R.id.boissonEditText);
        autoCompleteBoisson.setThreshold(1);
        autoCompleteBoisson.setAdapter(adapterBoissons);
        return view ;
    }
    private ArrayList<String> listeProduitParCategorie(Cursor cursor){
        ArrayList<String> listeProduits = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int nomProduitIndex = cursor.getColumnIndexOrThrow(DBAdapter.KEY_NOMPRODUIT);
                String nomProduit = cursor.getString(nomProduitIndex);
                listeProduits.add(nomProduit);
            }
            cursor.close();
        } else {
            Toast.makeText(view.getContext(), "Aucun produit trouvé pour cette catégorie.", Toast.LENGTH_SHORT).show();
        }
        return listeProduits ;
    }
}
