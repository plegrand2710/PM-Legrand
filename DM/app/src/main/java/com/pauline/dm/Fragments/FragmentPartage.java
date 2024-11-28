package com.pauline.dm.Fragments;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.pauline.dm.ConviveCommande;

public class FragmentPartage extends FragmentConvive {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            setConviveCommande((ConviveCommande) savedInstanceState.getSerializable("commandeTable"));
        } else {
            setConviveCommande(new ConviveCommande());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("commandeTable", getConviveCommande());
    }


    @Override
    public void remplirConviveCommandeDepuisUI() {
        super.remplirConviveCommandeDepuisUI();
    }

    @Override
    public ConviveCommande getConviveCommande() {
        remplirConviveCommandeDepuisUI();
        return super.getConviveCommande();
    }
}