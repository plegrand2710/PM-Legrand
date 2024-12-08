package com.pauline.dm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ArrayAdapter;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;
import com.pauline.dm.Utilisateur;

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
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        tvUsername.setText(user.getUsername());
        tvRole.setText(user.getRole());

        btnEdit.setOnClickListener(v -> {
            // Implémentez ici la logique pour ouvrir une activité ou un dialog pour modifier l'utilisateur
            // Exemple :
            // Intent intent = new Intent(context, EditUserActivity.class);
            // intent.putExtra("USER_ID", user.getId());
            // context.startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            dbAdapter.deleteUtilisateur(user.getId());
            users.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}