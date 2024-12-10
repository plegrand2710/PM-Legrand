package com.pauline.dm.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireActionBDDDistante;
import com.pauline.dm.R;

public class AjouterProduitDialog extends AppCompatDialogFragment {
    private EditText etName, etPrice;
    private Spinner spCategory, spCookingRequired;
    private Runnable listener;

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ajouter_produit, null);

        etName = view.findViewById(R.id.etProductName);
        etPrice = view.findViewById(R.id.etProductPrice);
        spCategory = view.findViewById(R.id.spCategory);
        spCookingRequired = view.findViewById(R.id.spCookingRequired);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.product_categories,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> cookingAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.cooking_required,
                android.R.layout.simple_spinner_item
        );
        cookingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCookingRequired.setAdapter(cookingAdapter);

        builder.setView(view)
                .setTitle("Ajouter un produit")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Enregistrer", (dialog, which) -> addProduct());

        return builder.create();
    }

    private void addProduct() {
        String name = etName.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        boolean cookingRequired = spCookingRequired.getSelectedItem().toString().equalsIgnoreCase("Oui");
        double price;

        try {
            price = Double.parseDouble(etPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Prix invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        int cuisson = cookingRequired ? 1 : 0;
        long result = dbAdapter.insertProduit(name, category, cuisson, price);
        dbAdapter.close();

        if (result>0) {
            ajouterProduitDistante(name, category, cuisson, price);
            if (listener != null) {
                listener.run();
            }
        } else {
            Toast.makeText(getContext(), "Erreur lors de l'ajout du produit", Toast.LENGTH_SHORT).show();
        }
    }

    private void ajouterProduitDistante(String name, String category, int cuisson, double price) {
        String action = "insert";
        String parameters = "table=produit&nomProduit=" + name +
                "&categorie=" + category +
                "&cuisson=" + cuisson +
                "&prix=" + price;

        new GestionnaireActionBDDDistante(getContext(), action, parameters, success -> {
            if (success) {
                Toast.makeText(getContext(), "Produit ajouté à la base distante.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'ajout à la base distante.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}