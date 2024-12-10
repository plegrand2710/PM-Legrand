package com.pauline.dm.Commande.Fragments;

import android.os.Bundle;

import com.pauline.dm.Admin.Produit;
import com.pauline.dm.Commande.ConviveCommande;

import java.io.Serializable;
import java.util.List;

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