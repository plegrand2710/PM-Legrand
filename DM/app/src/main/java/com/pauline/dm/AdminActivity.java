package com.pauline.dm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

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
            /*Intent intent = new Intent(AdminActivity.this, ManageTablesActivity.class);
            startActivity(intent);*/
        });

        btnManageProducts.setOnClickListener(v -> {
            /*Intent intent = new Intent(AdminActivity.this, ManageProductsActivity.class);
            startActivity(intent);*/
        });

        btnViewOrders.setOnClickListener(v -> {
            /*Intent intent = new Intent(AdminActivity.this, ViewOrdersActivity.class);
            startActivity(intent);*/
        });
    }
}