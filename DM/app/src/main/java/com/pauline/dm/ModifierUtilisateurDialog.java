package com.pauline.dm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ModifierUtilisateurDialog extends AppCompatDialogFragment {

    private EditText etUsername, etPassword;
    private Spinner spinnerRole;
    private DBAdapter dbAdapter;

    private int userId; // ID de l'utilisateur à modifier

    public interface EditUserDialogListener {
        void onUserUpdated();
    }

    private EditUserDialogListener listener;

    public void setListener(EditUserDialogListener listener) {
        this.listener = listener;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modifier_utilisateur, null);

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);

        // Charger les rôles disponibles dans le Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Charger les données de l'utilisateur
        Utilisateur user = dbAdapter.getUtilisateur(userId);
        if (user != null) {
            etUsername.setText(user.getIdentifiant());
            etPassword.setText(user.getMdp());
            int rolePosition = adapter.getPosition(user.getRole());
            spinnerRole.setSelection(rolePosition);
        }

        builder.setView(view)
                .setTitle("Modifier l'utilisateur")
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = etUsername.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        String role = spinnerRole.getSelectedItem().toString();

                        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                            Toast.makeText(getContext(), "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        boolean updated = dbAdapter.updateUtilisateur(userId, username, password, role);
                        if (updated) {
                            Toast.makeText(getContext(), "Utilisateur mis à jour avec succès.", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onUserUpdated();
                            }
                        } else {
                            Toast.makeText(getContext(), "Erreur lors de la mise à jour.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}