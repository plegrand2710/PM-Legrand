package com.pauline.dm.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.pauline.dm.R;

public class ModifierProduitDialog extends AppCompatDialogFragment {

    private EditText etNomProduit, etPrixProduit;
    private Spinner spCategorieProduit;
    private CheckBox cbCuisson;
    private Produit produit;
    private OnProductUpdateListener listener;

    public interface OnProductUpdateListener {
        void onProductUpdated(Produit updatedProduit);
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public void setListener(OnProductUpdateListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modifier_produit, null);

        etNomProduit = view.findViewById(R.id.etNomProduit);
        etPrixProduit = view.findViewById(R.id.etPrixProduit);
        spCategorieProduit = view.findViewById(R.id.spCategorieProduit);
        cbCuisson = view.findViewById(R.id.cbCuisson);

        // Charger les catégories dans le spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.product_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorieProduit.setAdapter(adapter);

        // Pré-remplir les champs si un produit est passé
        if (produit != null) {
            etNomProduit.setText(produit.getNom());
            etPrixProduit.setText(String.valueOf(produit.getPrix()));
            spCategorieProduit.setSelection(adapter.getPosition(produit.getCategorie()));
            cbCuisson.setChecked(produit.isNecessiteCuisson());
        }

        builder.setView(view)
                .setTitle(produit == null ? "Ajouter un produit" : "Modifier un produit")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Enregistrer", null); // Bouton personnalisé plus bas

        // Ajouter un comportement personnalisé au bouton Enregistrer
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                if (validerChamps()) {
                    String nomProduit = etNomProduit.getText().toString();
                    double prixProduit = Double.parseDouble(etPrixProduit.getText().toString());
                    String categorieProduit = spCategorieProduit.getSelectedItem().toString();
                    boolean cuisson = cbCuisson.isChecked();

                    if (listener != null) {
                        Produit updatedProduit = new Produit(
                                produit == null ? 0 : produit.getId(),
                                nomProduit,
                                categorieProduit,
                                cuisson,
                                prixProduit
                        );
                        listener.onProductUpdated(updatedProduit);
                    }
                    dialog.dismiss();
                }
            });
        });

        return dialog;
    }

    private boolean validerChamps() {
        String nomProduit = etNomProduit.getText().toString().trim();
        String prixProduit = etPrixProduit.getText().toString().trim();

        if (nomProduit.isEmpty()) {
            Toast.makeText(getContext(), "Le nom du produit est requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (prixProduit.isEmpty()) {
            Toast.makeText(getContext(), "Le prix du produit est requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double prix = Double.parseDouble(prixProduit);
            if (prix <= 0) {
                Toast.makeText(getContext(), "Le prix doit être supérieur à 0.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer un prix valide.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}