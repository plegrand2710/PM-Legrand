package com.pauline.dm.Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class ProduitsCommandeAdapter extends ArrayAdapter<Contient> {

    private final Context context;
    private final List<Contient> produitsCommande;
    private final DBAdapter dbAdapter;
    static final String TAG = "DMProjet";
    private final int idCommandeEnCours;

    public ProduitsCommandeAdapter(@NonNull Context context, @NonNull List<Contient> produitsCommande, DBAdapter dbAdapter, int idCommandeEnCours) {
        super(context, R.layout.produit_commande_item, produitsCommande);
        this.context = context;
        this.produitsCommande = produitsCommande;
        this.dbAdapter = dbAdapter;
        this.idCommandeEnCours = idCommandeEnCours;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.produit_commande_item, parent, false);
        }

        Contient produitCommande = produitsCommande.get(position);

        TextView tvNomProduit = convertView.findViewById(R.id.tvNomProduit);
        EditText etQuantiteProduit = convertView.findViewById(R.id.etQuantiteProduit);
        ImageView ivDeleteProduit = convertView.findViewById(R.id.ivDeleteProduit);
        ImageView ivUpdateQuantite = convertView.findViewById(R.id.ivUpdateQuantite);
        ImageView ivAddProduit = convertView.findViewById(R.id.ivAddProduit);

        String nomProduit = dbAdapter.getNomProduitById(produitCommande.getIdProduit());
        tvNomProduit.setText(nomProduit != null ? nomProduit : "Produit inconnu");

        etQuantiteProduit.setText(String.valueOf(produitCommande.getQuantite()));

        ivDeleteProduit.setOnClickListener(v -> {
            boolean deleted = dbAdapter.deleteProduitFromCommande(produitCommande.getIdCommande(), produitCommande.getIdProduit());
            if (deleted) {
                produitsCommande.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Produit supprimé de la commande avec succès.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erreur lors de la suppression du produit.", Toast.LENGTH_SHORT).show();
            }
        });

        ivUpdateQuantite.setOnClickListener(v -> {
            try {
                int nouvelleQuantite = Integer.parseInt(etQuantiteProduit.getText().toString().trim());
                if (nouvelleQuantite > 0) {
                    boolean updated = dbAdapter.updateQuantiteProduitInCommande(
                            produitCommande.getIdCommande(),
                            produitCommande.getIdProduit(),
                            nouvelleQuantite
                    );
                    if (updated) {
                        produitCommande.setQuantite(nouvelleQuantite);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Quantité mise à jour avec succès.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erreur lors de la mise à jour de la quantité.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "La quantité doit être supérieure à 0.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Veuillez entrer une quantité valide.", Toast.LENGTH_SHORT).show();
            }
        });

        ivAddProduit.setOnClickListener(v -> {
            AjouterProduitCommandeDialog dialog = new AjouterProduitCommandeDialog();

            dialog.setListener(nouveauProduit -> {
                nouveauProduit.setIdCommande(idCommandeEnCours);
                produitsCommande.add(nouveauProduit);
                dbAdapter.insertProduitIntoCommande(nouveauProduit.getIdCommande(), nouveauProduit.getIdProduit(), nouveauProduit.getQuantite(), "à préparer");
                notifyDataSetChanged();
                Toast.makeText(context, "Produit ajouté à la commande avec succès.", Toast.LENGTH_SHORT).show();
            });
            notifyDataSetChanged();
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "AjouterProduitDialog");
        });

        return convertView;
    }

    public void updateData(List<Contient> newProduitsCommande) {
        Log.d(TAG, "updateData: tableau à intégrer = " + newProduitsCommande);
        produitsCommande.clear();
        Log.d(TAG, "updateData: produitsCommandes clear = " + produitsCommande);
        produitsCommande.addAll(newProduitsCommande);
        Log.d(TAG, "updateData: produitsCommandes ajout du nouveau tableau = " + produitsCommande);

        notifyDataSetChanged();
    }


}