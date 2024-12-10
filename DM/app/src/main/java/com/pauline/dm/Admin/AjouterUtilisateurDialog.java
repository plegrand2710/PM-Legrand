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

public class AjouterUtilisateurDialog extends AppCompatDialogFragment {
    private EditText editTextUsername, editTextPassword;
    private Spinner spinnerRole;

    private DBAdapter dbAdapter;
    private AjouterUtilisateurListener listener;

    public interface AjouterUtilisateurListener {
        void onUserAdded();
    }

    public void setListener(AjouterUtilisateurListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ajouter_utilisateur, null);

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);

        // Remplir le spinner avec les rôles possibles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.user_roles,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Ajouter un utilisateur")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Ajouter", (dialogInterface, i) -> {
                    String username = editTextUsername.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    String role = spinnerRole.getSelectedItem().toString();

                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean success = dbAdapter.insertUtilisateur(username, password, role);
                    if (success) {
                        ajouterUtilisateurDistante(username, password, role);
                        Toast.makeText(getContext(), "Utilisateur ajouté avec succès.", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onUserAdded();
                        }
                    } else {
                        Toast.makeText(getContext(), "Erreur lors de l'ajout de l'utilisateur.", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    private void ajouterUtilisateurDistante(String username, String password, String role) {
        String action = "insert";
        String parameters = "table=utilisateurs&identifiant=" + username + "&mdp=" + password + "&role=" + role;

        new GestionnaireActionBDDDistante(getContext(), action, parameters, success -> {
            if (success) {
                Toast.makeText(getContext(), "Utilisateur ajouté à la base distante.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'ajout à la base distante.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}