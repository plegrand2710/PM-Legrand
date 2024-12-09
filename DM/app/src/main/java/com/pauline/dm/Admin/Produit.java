package com.pauline.dm.Admin;

public class Produit {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isNecessiteCuisson() {
        return necessiteCuisson;
    }

    public void setNecessiteCuisson(boolean necessiteCuisson) {
        this.necessiteCuisson = necessiteCuisson;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}