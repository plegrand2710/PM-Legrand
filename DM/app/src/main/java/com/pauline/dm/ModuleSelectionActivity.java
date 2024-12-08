package com.pauline.dm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModuleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_selection);

        String username = getIntent().getStringExtra("USERNAME");
        String role = getIntent().getStringExtra("ROLE");

        if (role == null) {
            Toast.makeText(this, "Rôle utilisateur non défini", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toast.makeText(this, "Rôle utilisateur "+role, Toast.LENGTH_SHORT).show();

        if ("Superviseur".equals(role)) {
            enableAllModules();
        } else if ("Responsable".equals(role)) {
            enableModules(true, true, true, false);
        } else if ("Serveur".equals(role)) {
            Toast.makeText(this, "c'est bien un serveur", Toast.LENGTH_SHORT).show();
            enableModules(true, false, false, false);
        } else if ("Cuisinier".equals(role)) {
            enableModules(false, false, false, true);
        } else {
            Toast.makeText(this, "Rôle utilisateur inconnu", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void enableAllModules() {
        enableModules(true, true, true, true);
    }

    private void enableModules(boolean commandes, boolean caisse, boolean cuisine, boolean admin) {
        View lytCommandes = findViewById(R.id.layoutCommandes);
        View lytCaisse = findViewById(R.id.layoutCaisse);
        View lytCuisine = findViewById(R.id.layoutCuisine);
        View lytAdmin = findViewById(R.id.layoutAdmin);

        if (commandes) {
            lytCommandes.setVisibility(View.VISIBLE);
        } else {
            lytCommandes.setVisibility(View.GONE);
        }

        if (caisse) {
            lytCaisse.setVisibility(View.VISIBLE);
        } else {
            lytCaisse.setVisibility(View.GONE);
        }

        if (cuisine) {
            lytCuisine.setVisibility(View.VISIBLE);
        } else {
            lytCuisine.setVisibility(View.GONE);
        }

        if (admin) {
            lytAdmin.setVisibility(View.VISIBLE);
        } else {
            lytAdmin.setVisibility(View.GONE);
        }
    }

    public void openCommandesModule(View view) {
        startActivity(new Intent(this, CommandeActivity.class));
    }

    public void openCaisseModule(View view) {
        startActivity(new Intent(this, CaisseActivity.class));
    }

    public void openCuisineModule(View view) {
        startActivity(new Intent(this, CuisineActivity.class));
    }

    public void openAdminModule(View view) {
        startActivity(new Intent(this, AdminActivity.class));
    }

    public void logout(View view) {
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        finish(); // Retour à l'écran de connexion
    }
}