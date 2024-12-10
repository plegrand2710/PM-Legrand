package com.pauline.dm.Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class ProduitAdapter extends ArrayAdapter<Produit> {
    private final Context context;
    private final List<Produit> products;
    private final DBAdapter dbAdapter;
    private final Runnable refreshCallback;
    private static final String TAG = "DMProjet";


    public ProduitAdapter(@NonNull Context context, @NonNull List<Produit> products, DBAdapter dbAdapter, Runnable refreshCallback) {
        super(context, R.layout.produit_liste_item, products);
        this.context = context;
        this.products = products;
        this.dbAdapter = dbAdapter;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.produit_liste_item, parent, false);
        }

        Produit product = products.get(position);

        TextView tvName = convertView.findViewById(R.id.tvProductName);
        TextView tvCategory = convertView.findViewById(R.id.tvProductCategory);
        TextView tvPrice = convertView.findViewById(R.id.tvProductPrice);
        ImageView btnEdit = convertView.findViewById(R.id.ivEdit);
        ImageView btnDelete = convertView.findViewById(R.id.ivDelete);

        tvName.setText(product.getNom());
        tvCategory.setText(product.getCategorie());
        tvPrice.setText(String.format("%.2f €", product.getPrix()));

        btnEdit.setOnClickListener(v -> {
                ModifierProduitDialog dialog = new ModifierProduitDialog();
                dialog.setProduit(product);
                dialog.setListener(new ModifierProduitDialog.OnProductUpdateListener() {
                    @Override
                    public void onProductUpdated(Produit updatedProduit) {
                        products.clear();
                        products.addAll(dbAdapter.getAllProduits());
                        ProduitAdapter.this.notifyDataSetChanged();
                        Toast.makeText(context, "Produit modifié avec succès.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "EditProductDialog");
        });
        btnDelete.setOnClickListener(v -> deleteProduct(product.getId()));

        return convertView;
    }


    private void deleteProduct(int productId) {
        long result = dbAdapter.deleteProduit(productId);
        if (result>0) {
            refreshCallback.run();
            Toast.makeText(context, "Produit supprimé avec succès.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Échec de la suppression du produit.", Toast.LENGTH_SHORT).show();
        }
    }
}