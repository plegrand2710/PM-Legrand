package com.pauline.dm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;

public class ManagerUtilisateursActivity extends AppCompatActivity {
    private DBAdapter dbAdapter;
    private UtilisateursAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_utilisateurs);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        ListView userListView = findViewById(R.id.userListView);
        Button btnAddUser = findViewById(R.id.btnAddUser);
        Button btnBack = findViewById(R.id.btnBack);

        userAdapter = new UtilisateursAdapter(this, dbAdapter.getAllUsers(), dbAdapter);
        userListView.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(v -> {
            AjouterUtilisateurDialog dialog = new AjouterUtilisateurDialog();
            dialog.setListener(() -> {
                userAdapter.clear();
                userAdapter.addAll(dbAdapter.getAllUsers());
                userAdapter.notifyDataSetChanged();
            });
            dialog.show(getSupportFragmentManager(), "AddUserDialog");
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}