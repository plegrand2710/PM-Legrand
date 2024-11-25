package com.pauline.dm;

import java.util.ArrayList;
import java.util.List;

public class ConviveCommande {
    private List<String> plats = new ArrayList<>();
    private List<Integer> quantitesPlats = new ArrayList<>();
    private List<String> accompagnements = new ArrayList<>();
    private List<Integer> quantitesAccompagnements = new ArrayList<>();
    private List<String> boissons = new ArrayList<>();
    private List<Integer> quantitesBoissons = new ArrayList<>();

    public void ajouterPlat(String plat, int quantite) {
        plats.add(plat);
        quantitesPlats.add(quantite);
    }

    public void ajouterAccompagnement(String accompagnement, int quantite) {
        accompagnements.add(accompagnement);
        quantitesAccompagnements.add(quantite);
    }

    public void ajouterBoisson(String boisson, int quantite) {
        boissons.add(boisson);
        quantitesBoissons.add(quantite);
    }

    @Override
    public String toString() {
        return "Plats: " + plats + ", Quantités: " + quantitesPlats +
                "\nAccompagnements: " + accompagnements + ", Quantités: " + quantitesAccompagnements +
                "\nBoissons: " + boissons + ", Quantités: " + quantitesBoissons;
    }
}