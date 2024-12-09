package com.pauline.dm.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;
public class UtilisateursAdapter extends ArrayAdapter<Utilisateur> {
    private final Context context;
    private final List<Utilisateur> users;
    private final DBAdapter dbAdapter;

    public UtilisateursAdapter(@NonNull Context context, @NonNull List<Utilisateur> users, DBAdapter dbAdapter) {
        super(context, R.layout.utilisateurs_liste_item, users);
        this.context = context;
        this.users = users;
        this.dbAdapter = dbAdapter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.utilisateurs_liste_item, parent, false);
        }

        Utilisateur user = users.get(position);

        TextView tvUsername = convertView.findViewById(R.id.tvUsername);
        TextView tvRole = convertView.findViewById(R.id.tvRole);
        ImageView ivEdit = convertView.findViewById(R.id.ivEdit);
        ImageView ivDelete = convertView.findViewById(R.id.ivDelete);

        tvUsername.setText(user.getIdentifiant());
        tvRole.setText(user.getRole());

        ivEdit.setOnClickListener(v -> {
            ModifierUtilisateurDialog dialog = new ModifierUtilisateurDialog();
            dialog.setUserId(user.getId());
            dialog.setListener(() -> {
                users.clear();
                users.addAll(dbAdapter.getAllUsers());
                notifyDataSetChanged();
            });
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "EditUserDialog");
        });

        ivDelete.setOnClickListener(v -> {
            boolean deleted = dbAdapter.deleteUtilisateur(user.getId());
            if (deleted) {
                users.remove(position); // Retirer l'utilisateur de la liste
                notifyDataSetChanged();
                Toast.makeText(context, "Utilisateur supprimé avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Échec de la suppression de l'utilisateur", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}