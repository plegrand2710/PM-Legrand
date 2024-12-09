package com.pauline.dm.Admin;

import java.io.Serializable;

public class Produit implements Serializable {
    private int id;
    private String nom;
    private String categorie;
    private boolean necessiteCuisson;
    private double prix;

    public Produit(int id, String nom, String categorie, boolean necessiteCuisson, double prix) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.necessiteCuisson = necessiteCuisson;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public boolean isNecessiteCuisson() {
        return necessiteCuisson;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public String toString() {
        return nom;
    }
}