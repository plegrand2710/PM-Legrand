package com.pauline.dm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "DMProjet";
    private static final String PREFS_NAME = "LoginPrefs";

    private EditText editTextUsername, editTextPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBAdapter(this);
        db.open();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);

        loadSavedCredentials();

        btnLogin.setOnClickListener(view -> login());
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Log.d(TAG, "login: j'ai récupéré les données");

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Le nom d'utilisateur et le mot de passe ne doivent pas être vides", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = db.getConnexionRole(username, password);
        Log.d(TAG, "login: j'ai récupéré le rôle : " + role);

        if (role != null) {
            if (cbRememberMe.isChecked()) {
                saveCredentials(username, password);
            } else {
                clearSavedCredentials();
            }

            navigateToModuleSelection(username, role);
        } else {
            Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCredentials(String username, String password) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
        Log.d(TAG, "saveCredentials: Identifiants enregistrés.");
    }

    private void clearSavedCredentials() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Log.d(TAG, "clearSavedCredentials: Identifiants effacés.");
    }

    private void loadSavedCredentials() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            editTextUsername.setText(savedUsername);
            editTextPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
            Log.d(TAG, "loadSavedCredentials: Identifiants préremplis.");
        }
    }

    private void navigateToModuleSelection(String username, String role) {
        Intent intent = new Intent(LoginActivity.this, ModuleSelectionActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("ROLE", role);
        Log.d(TAG, "navigateToModuleSelection: je démarre l'activité");
        startActivity(intent);
        Log.d(TAG, "navigateToModuleSelection: j'ai lancé");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_force_update) {
            forceUpdateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void forceUpdateData() {
        Toast.makeText(this, "Mise à jour des données en cours...", Toast.LENGTH_SHORT).show();
        Log.d("LoginActivity", "Les données ont été mises à jour.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}