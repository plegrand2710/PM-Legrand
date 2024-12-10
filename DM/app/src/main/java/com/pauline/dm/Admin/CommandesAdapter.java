package com.pauline.dm.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class CommandesAdapter extends ArrayAdapter<Commande> {
    private final Context context;
    private final List<Commande> commandes;
    private final DBAdapter dbAdapter;
    private final Runnable refreshCallback;
    static final String TAG = "DMProjet";


    public CommandesAdapter(@NonNull Context context, @NonNull List<Commande> commandes, DBAdapter dbAdapter, Runnable refreshCallback) {
        super(context, R.layout.commande_liste_item, commandes);
        this.context = context;
        this.commandes = commandes;
        this.dbAdapter = dbAdapter;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.commande_liste_item, parent, false);
        }

        Commande commande = commandes.get(position);

        TextView tvIdCommande = convertView.findViewById(R.id.tvIdCommande);
        TextView tvNumTable = convertView.findViewById(R.id.tvNumTable);
        ImageView btnEdit = convertView.findViewById(R.id.ivEditCommande);
        ImageView btnDelete = convertView.findViewById(R.id.ivDeleteCommande);

        tvIdCommande.setText("ID Commande : " + commande.getIdCommande());
        tvNumTable.setText("Table : " + commande.getNumTable());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifierCommandeActivity.class);
            intent.putExtra("commande", commande);
            Log.d(TAG, "Commande : " + commande.toString());
            context.startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            long deleted = dbAdapter.deleteCommande(commande.getIdCommande());
            if (deleted>0) {
                commandes.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Commande supprimée avec succès.", Toast.LENGTH_SHORT).show();
                refreshCallback.run();
            } else {
                Toast.makeText(context, "Échec de la suppression de la commande.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}