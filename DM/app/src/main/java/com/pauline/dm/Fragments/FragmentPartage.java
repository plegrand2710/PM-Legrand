package com.pauline.dm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.pauline.dm.R;

public class FragmentPartage extends Fragments {
    View view;
    EditText etQ1 ;
    EditText etQ2 ;
    EditText etQ3 ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partage, container, false);

        adapterPlats = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listePlats);
        AutoCompleteTextView autoCompletePlat = view.findViewById(R.id.platPartageEditText);
        autoCompletePlat.setThreshold(1);
        autoCompletePlat.setAdapter(adapterPlats);

        etQ1 = (EditText) view.findViewById(R.id.platPartageQuantiteEditText);

        adapterAccompagnements = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        AutoCompleteTextView autoCompleteAccompagnement = view.findViewById(R.id.accompagnementPartageEditText);
        autoCompleteAccompagnement.setThreshold(1);
        autoCompleteAccompagnement.setAdapter(adapterAccompagnements);

        etQ2 = (EditText) view.findViewById(R.id.accompagmentPartageQuantiteEditText);

        adapterBoissons = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, listeBoissons);
        AutoCompleteTextView autoCompleteBoisson = view.findViewById(R.id.boissonPartageEditText);
        autoCompleteBoisson.setThreshold(1);
        autoCompleteBoisson.setAdapter(adapterBoissons);

        etQ3 = (EditText) view.findViewById(R.id.boissonPartageQuantiteEditText);
        return view ;
    }

}
