package com.pauline.dm.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.R;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "DMProjet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnManageTables = findViewById(R.id.btnManageTables);
        Button btnManageProducts = findViewById(R.id.btnManageProducts);
        Button btnViewOrders = findViewById(R.id.btnViewOrders);

        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManagerUtilisateursActivity.class);
            startActivity(intent);
        });

        btnManageTables.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ModifierDispositionSalleActivity.class);
            startActivity(intent);
        });

        btnManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManagerProduitsActivity.class);
            startActivity(intent);
        });

        btnViewOrders.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: je charge commande");
            Intent intent = new Intent(AdminActivity.this, ManagerCommandesActivity.class);
            Log.d(TAG, "onCreate: début de commande");
            startActivity(intent);
            Log.d(TAG, "onCreate: j'ai lancé commande");
        });
    }
}