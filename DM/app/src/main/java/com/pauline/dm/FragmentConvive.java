package com.pauline.dm;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import android.util.Log;
import java.util.ArrayList;

public class FragmentConvive extends Fragments {
    View view;
    String TAG = "DMProjet";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_convive, container, false);

        adapterPlats = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listePlats);
        AutoCompleteTextView autoCompletePlat = view.findViewById(R.id.platEditText);
        autoCompletePlat.setThreshold(1);
        autoCompletePlat.setAdapter(adapterPlats);

        adapterAccompagnements = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        AutoCompleteTextView autoCompleteAccompagnement = view.findViewById(R.id.accompagnementEditText);
        autoCompleteAccompagnement.setThreshold(1);
        autoCompleteAccompagnement.setAdapter(adapterAccompagnements);

        adapterBoissons = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeBoissons);
        AutoCompleteTextView autoCompleteBoisson = view.findViewById(R.id.boissonEditText);
        autoCompleteBoisson.setThreshold(1);
        autoCompleteBoisson.setAdapter(adapterBoissons);
        return view ;
    }
}
