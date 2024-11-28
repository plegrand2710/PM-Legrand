package com.pauline.dm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConviveCommande implements Serializable {
    private List<ProduitCommande> _plats = new ArrayList<>();
    private List<ProduitCommande> _accompagnements = new ArrayList<>();
    private List<ProduitCommande> _boissons = new ArrayList<>();


    public List<ProduitCommande> getPlats() {
        return _plats;
    }

    public List<ProduitCommande> getAccompagnements() {
        return _accompagnements;
    }

    public List<ProduitCommande> getBoissons() {
        return _boissons;
    }

    public void setPlats(List<ProduitCommande> plats) {
        _plats = plats;
    }

    public void setAccompagnements(List<ProduitCommande> accompagnements) {
        _accompagnements = accompagnements;
    }

    public void setBoissons(List<ProduitCommande> boissons) {
        _boissons = boissons;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Plats:\n");
        for (ProduitCommande plat : _plats) {
            sb.append("  - ").append(plat).append("\n");
        }

        sb.append("Accompagnements:\n");
        for (ProduitCommande accompagnement : _accompagnements) {
            sb.append("  - ").append(accompagnement).append("\n");
        }

        sb.append("Boissons:\n");
        for (ProduitCommande boisson : _boissons) {
            sb.append("  - ").append(boisson).append("\n");
        }

        return sb.toString();
    }
}