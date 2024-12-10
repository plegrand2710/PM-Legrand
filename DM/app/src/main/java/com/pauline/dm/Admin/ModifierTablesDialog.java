package com.pauline.dm.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;


public class ModifierTablesDialog extends AppCompatDialogFragment {
    private EditText editTextNbConvives, editTextNbColonnes, editTextNbLignes;
    private int tableId;
    private DBAdapter dbAdapter;

    public interface ModifierTableListener {
        void onTableModified();
    }

    private ModifierTableListener listener;

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setListener(ModifierTableListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modifier_table, null);

        editTextNbConvives = view.findViewById(R.id.editTextNbConvives);
        editTextNbColonnes = view.findViewById(R.id.editTextNbColonnes);
        editTextNbLignes = view.findViewById(R.id.editTextNbLignes);

        dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();

        loadTableData();

        builder.setView(view)
                .setTitle("Modifier une table")
                .setNegativeButton("Annuler", (dialogInterface, i) -> dismiss())
                .setPositiveButton("Enregistrer", (dialogInterface, i) -> {
                    saveTableData();
                });

        return builder.create();
    }

    private void loadTableData() {
        Table table = dbAdapter.getTableById(tableId);
        if (table != null) {
            editTextNbConvives.setText(String.valueOf(table.getNbConvives()));
            editTextNbColonnes.setText(String.valueOf(table.getNumColonne()));
            editTextNbLignes.setText(String.valueOf(table.getNumLigne()));
        } else {
            Toast.makeText(getContext(), "Erreur: Table introuvable", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void saveTableData() {
        try {
            int nbConvives = Integer.parseInt(editTextNbConvives.getText().toString());
            int nbColonnes = Integer.parseInt(editTextNbColonnes.getText().toString());
            int nbLignes = Integer.parseInt(editTextNbLignes.getText().toString());

            boolean updated = dbAdapter.updateTableBoolean(tableId, nbConvives, nbColonnes, nbLignes);
            if (updated) {
                Toast.makeText(getContext(), "Table modifiée avec succès", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onTableModified();
                }
            } else {
                Toast.makeText(getContext(), "Erreur lors de la modification de la table", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer des valeurs valides", Toast.LENGTH_SHORT).show();
            Log.e("ModifierTableDialog", "Erreur de conversion des champs numériques", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}