package com.pauline.dm.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class ManagerProduitsActivity extends AppCompatActivity {
    private DBAdapter dbAdapter;
    private ListView productListView;
    private ProduitAdapter productAdapter;
    private Button btnAddProduct, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_produits);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        productListView = findViewById(R.id.productListView);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack);

        loadProducts();

        btnAddProduct.setOnClickListener(v -> openAddProductDialog());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProducts() {
        List<Produit> products = dbAdapter.getAllProduits();
        productAdapter = new ProduitAdapter(this, products, dbAdapter, this::loadProducts);
        productListView.setAdapter(productAdapter);
    }

    private void openAddProductDialog() {
        AjouterProduitDialog dialog = new AjouterProduitDialog();
        dialog.setListener(() -> {
            loadProducts();
            Toast.makeText(this, "Produit ajouté avec succès.", Toast.LENGTH_SHORT).show();
        });
        dialog.show(getSupportFragmentManager(), "AddProductDialog");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}