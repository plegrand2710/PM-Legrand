package com.pauline.dm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConviveCommande implements Serializable {
    private List<String> plats = new ArrayList<>();
    private List<Integer> quantitesPlats = new ArrayList<>();
    private List<String> cuissonsPlats = new ArrayList<>();
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

    public List<String> get_plats() {
        return plats;
    }

    public void set_plats(List<String> plats) {
        this.plats = plats;
    }

    public List<Integer> get_quantitesPlats() {
        return quantitesPlats;
    }

    public void set_quantitesPlats(List<Integer> quantitesPlats) {
        this.quantitesPlats = quantitesPlats;
    }

    public List<String> getCuissonsPlats() {
        return cuissonsPlats;
    }

    public void setCuissonsPlats(List<String> cuissonsPlats) {
        this.cuissonsPlats = cuissonsPlats;
    }

    public void set_cuissonsPlats(String cuissonsPlats) {
        this.cuissonsPlats.add(cuissonsPlats);
    }

    public List<String> get_accompagnements() {
        return accompagnements;
    }

    public void set_accompagnements(List<String> accompagnements) {
        this.accompagnements = accompagnements;
    }

    public List<Integer> get_quantitesAccompagnements() {
        return quantitesAccompagnements;
    }

    public void set_quantitesAccompagnements(List<Integer> quantitesAccompagnements) {
        this.quantitesAccompagnements = quantitesAccompagnements;
    }

    public List<String> get_boissons() {
        return boissons;
    }

    public void set_boissons(List<String> boissons) {
        this.boissons = boissons;
    }

    public List<Integer> get_quantitesBoissons() {
        return quantitesBoissons;
    }

    public void set_quantitesBoissons(List<Integer> quantitesBoissons) {
        this.quantitesBoissons = quantitesBoissons;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Plats: \n");
        for (int i = 0; i < plats.size(); i++) {
            sb.append("  - ").append(plats.get(i));
            if (i < cuissonsPlats.size() && cuissonsPlats.get(i) != null && !cuissonsPlats.get(i).isEmpty()) {
                sb.append(" (Cuisson: ").append(cuissonsPlats.get(i)).append(")");
            }
            sb.append(" (Quantité: ").append(quantitesPlats.get(i)).append(")\n");
        }

        sb.append("Accompagnements: \n");
        for (int i = 0; i < accompagnements.size(); i++) {
            sb.append("  - ").append(accompagnements.get(i));
            sb.append(" (Quantité: ").append(quantitesAccompagnements.get(i)).append(")\n");
        }

        sb.append("Boissons: \n");
        for (int i = 0; i < boissons.size(); i++) {
            sb.append("  - ").append(boissons.get(i));
            sb.append(" (Quantité: ").append(quantitesBoissons.get(i)).append(")\n");
        }

        return sb.toString();
    }
}