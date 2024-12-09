package com.pauline.dm.Admin;

import java.io.Serializable;
import java.util.List;

public class Commande implements Serializable {
    private int idCommande;
    private String cuissonCommande;
    private int numTable;
    private String status;
    private List<Contient> produits;

    public Commande(int idCommande, String status, String cuissonCommande, int numTable, List<Contient> produits) {
        this.idCommande = idCommande;
        this.status = status;
        this.cuissonCommande = cuissonCommande;
        this.numTable = numTable;
        this.produits = produits;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public String getStatus() {
        return status;
    }

    public String getCuissonCommande() {
        return cuissonCommande;
    }

    public int getNumTable() {
        return numTable;
    }

    public List<Contient> getProduits() {
        return produits;
    }

    public void setProduits(List<Contient> produits) {
        this.produits = produits;
    }
}