package com.pauline.dm.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireActionBDDDistante;
import com.pauline.dm.R;

import java.util.List;

public class AjouterProduitCommandeDialog extends AppCompatDialogFragment {

    private Spinner spProduits;
    private LinearLayout containerCuisson;
    private EditText etQuantiteProduit;
    private OnProduitAjouteListener listener;
    private DBAdapter dbAdapter;
    private int idCommande;
    private Produit produitSelectionne;

    public interface OnProduitAjouteListener {
        void onProduitAjoute(Contient nouveauProduit);
    }

    public void setListener(OnProduitAjouteListener listener) {
        this.listener = listener;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ajouter_produit_commande, null);

        spProduits = view.findViewById(R.id.spProduits);
        containerCuisson = view.findViewById(R.id.containerCuisson);
        etQuantiteProduit = view.findViewById(R.id.etQuantiteProduit);
        Button btnAjouterProduit = view.findViewById(R.id.btnAjouterProduit);

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        chargerProduits();

        spProduits.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                produitSelectionne = (Produit) spProduits.getSelectedItem();
                afficherOptionsCuisson(containerCuisson, produitSelectionne);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                produitSelectionne = null;
                containerCuisson.removeAllViews();
            }
        });

        btnAjouterProduit.setOnClickListener(v -> {
            if (validerChamps()) {
                String cuisson = produitSelectionne.isNecessiteCuisson() ?
                        getCuissonChoisie(containerCuisson) : "N/A";
                int quantite = Integer.parseInt(etQuantiteProduit.getText().toString().trim());

                if (listener != null) {
                    Contient nouveauProduit = new Contient(
                            idCommande,
                            produitSelectionne.getId(),
                            quantite,
                            "à préparer",
                            cuisson
                    );
                    ajouterProduitDistante(nouveauProduit);
                    listener.onProduitAjoute(nouveauProduit);

                }

                dismiss();
            }
        });

        builder.setView(view)
                .setTitle("Ajouter un produit")
                .setNegativeButton("Annuler", (dialog, which) -> dismiss());

        return builder.create();
    }

    private void chargerProduits() {
        List<Produit> produits = dbAdapter.getAllProduits();
        ArrayAdapter<Produit> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                produits
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProduits.setAdapter(adapter);
    }

    private void afficherOptionsCuisson(LinearLayout container, Produit produit) {
        container.removeAllViews();

        if (!produit.isNecessiteCuisson()) {
            container.setVisibility(View.GONE);
            return;
        }

        container.setVisibility(View.VISIBLE);

        String[] niveauxCuisson = {
                "Extra-bleu", "Bleu", "Saignant", "À point", "Cuit", "Très cuit"
        };

        android.widget.RadioGroup radioGroup = new android.widget.RadioGroup(getContext());
        radioGroup.setOrientation(android.widget.RadioGroup.VERTICAL);

        for (String niveau : niveauxCuisson) {
            android.widget.RadioButton radioButton = new android.widget.RadioButton(getContext());
            radioButton.setText(niveau);
            radioButton.setTextSize(16);
            radioButton.setPadding(10, 10, 10, 10);

            radioGroup.addView(radioButton);
        }

        container.addView(radioGroup);
    }

    private String getCuissonChoisie(LinearLayout container) {
        android.widget.RadioGroup radioGroup = (android.widget.RadioGroup) container.getChildAt(0);
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId != -1) {
            android.widget.RadioButton selectedRadioButton = container.findViewById(checkedId);
            return selectedRadioButton != null ? selectedRadioButton.getText().toString() : "N/A";
        }

        return "N/A";
    }

    private boolean validerChamps() {
        String quantiteStr = etQuantiteProduit.getText().toString().trim();

        if (quantiteStr.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer une quantité.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);
            if (quantite <= 0) {
                Toast.makeText(getContext(), "La quantité doit être supérieure à 0.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer une quantité valide.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spProduits.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Veuillez sélectionner un produit.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (produitSelectionne.isNecessiteCuisson() && getCuissonChoisie(containerCuisson).equals("N/A")) {
            Toast.makeText(getContext(), "Veuillez sélectionner un niveau de cuisson.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void ajouterProduitDistante(Contient nouveauProduit) {
        String action = "insert";
        String parameters = "table=contient&idProduit=" + nouveauProduit.getIdProduit() +
                "&idCommande=" + nouveauProduit.getIdCommande() +
                "&quantite=" + nouveauProduit.getQuantite() +
                "&traitement_contient=" + nouveauProduit.getTraitement() +
                "&cuisson_commande=" + nouveauProduit.getCuissonCommande();

        new GestionnaireActionBDDDistante(getContext(), action, parameters, success -> {
            if (success) {
                Toast.makeText(getContext(), "Produit ajouté à la base distante.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'ajout à la base distante.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}