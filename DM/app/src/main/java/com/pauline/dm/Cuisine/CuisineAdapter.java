package com.pauline.dm.Cuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.pauline.dm.R;

import java.util.List;

public class CuisineAdapter extends ArrayAdapter<PlatAccompagnementBoisson> {

    public CuisineAdapter(@NonNull Context context, @NonNull List<PlatAccompagnementBoisson> platsBoissons) {
        super(context, R.layout.plat_boisson_accompagnement_item, platsBoissons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plat_boisson_accompagnement_item, parent, false);
        }

        PlatAccompagnementBoisson platBoisson = getItem(position);

        TextView tvNom = convertView.findViewById(R.id.tvNomPlatBoisson);
        TextView tvQuantite = convertView.findViewById(R.id.tvQuantitePlatBoisson);

        if (platBoisson != null) {
            tvNom.setText(platBoisson.getNomProduit());

            if (platBoisson.getQuantite() > 1) {
                tvQuantite.setText("x" + platBoisson.getQuantite());
                tvQuantite.setVisibility(View.VISIBLE);
            } else {
                tvQuantite.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}