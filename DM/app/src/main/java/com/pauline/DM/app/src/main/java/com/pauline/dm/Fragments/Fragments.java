package com.pauline.dm.Fragments;
/*
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pauline.dm.ConviveCommande;
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
            cursor.moveToPosition(-1);
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
                    String cuisson = cursor.getString(cuissonIndex);

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

    public void afficherOptionsCuisson(LinearLayout container, ConviveCommande cc) {
        container.removeAllViews();
        String[] niveauxCuisson = {
                "Extra-bleu (Extra-rare)",
                "Bleu (Rare)",
                "Saignant (Medium rare)",
                "A point (Medium)",
                "Cuit (Medium well)",
                "Très cuit (Well done)"
        };
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (String niveau : niveauxCuisson) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(niveau);
            radioButton.setTextSize(16);
            radioButton.setPadding(10, 10, 10, 10);
            radioGroup.addView(radioButton);
        }

        container.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = container.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String cuissonChoisie = selectedRadioButton.getText().toString();
                Toast.makeText(getContext(), "Cuisson choisie : " + cuissonChoisie, Toast.LENGTH_SHORT).show();

                sauvegarderCuissonDansCommande(cuissonChoisie, cc);
            }
        });
    }

    private void sauvegarderCuissonDansCommande(String cuissonChoisie, ConviveCommande cc) {
        cc.set_cuissonsPlats(cuissonChoisie);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}*/


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pauline.dm.ConviveCommande;
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
            cursor.moveToPosition(-1);
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
                    String cuisson = cursor.getString(cuissonIndex);

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

    public void afficherOptionsCuisson(LinearLayout container, ConviveCommande cc, @Nullable String cuissonEnregistree) {
        container.removeAllViews();
        String[] niveauxCuisson = {
                "Extra-bleu (Extra-rare)",
                "Bleu (Rare)",
                "Saignant (Medium rare)",
                "A point (Medium)",
                "Cuit (Medium well)",
                "Très cuit (Well done)"
        };

        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (String niveau : niveauxCuisson) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(niveau);
            radioButton.setTextSize(16);
            radioButton.setPadding(10, 10, 10, 10);

            // Cocher si la cuisson correspond à la cuisson enregistrée
            if (niveau.equals(cuissonEnregistree)) {
                radioButton.setChecked(true);
            }

            radioGroup.addView(radioButton);
        }

        container.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = container.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String cuissonChoisie = selectedRadioButton.getText().toString();
                Toast.makeText(getContext(), "Cuisson choisie : " + cuissonChoisie, Toast.LENGTH_SHORT).show();
                // Mettre à jour la cuisson pour le plat actuel
                int indexPlat = cc.get_plats().indexOf(selectedRadioButton.getText().toString());
                if (indexPlat >= 0) {
                    cc.getCuissonsPlats().set(indexPlat, cuissonChoisie);
                }
            }
        });
    }


    public void afficherOptionsCuisson(LinearLayout container, ConviveCommande cc) {
        container.removeAllViews();
        String[] niveauxCuisson = {
                "Extra-bleu (Extra-rare)",
                "Bleu (Rare)",
                "Saignant (Medium rare)",
                "A point (Medium)",
                "Cuit (Medium well)",
                "Très cuit (Well done)"
        };
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (String niveau : niveauxCuisson) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(niveau);
            radioButton.setTextSize(16);
            radioButton.setPadding(10, 10, 10, 10);
            radioGroup.addView(radioButton);
        }

        container.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = container.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String cuissonChoisie = selectedRadioButton.getText().toString();
                Toast.makeText(getContext(), "Cuisson choisie : " + cuissonChoisie, Toast.LENGTH_SHORT).show();

                sauvegarderCuissonDansCommande(cuissonChoisie, cc);
            }
        });
    }

    private void sauvegarderCuissonDansCommande(String cuissonChoisie, ConviveCommande cc) {
        cc.set_cuissonsPlats(cuissonChoisie);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}