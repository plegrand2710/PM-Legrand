package com.pauline.dm.Admin;

import java.io.Serializable;
import java.util.List;

public class Commande implements Serializable {
    private int idCommande;
    private String status;
    private int numTable;
    private List<Contient> produits;

    public Commande(int idCommande, String status, int numTable, List<Contient> produits) {
        this.idCommande = idCommande;
        this.status = status;
        this.numTable = numTable;
        this.produits = produits;
    }

    // Getters
    public int getIdCommande() {
        return idCommande;
    }

    public String getStatus() {
        return status;
    }

    public int getNumTable() {
        return numTable;
    }

    public List<Contient> getProduits() {
        return produits;
    }

    // Setters
    public void setProduits(List<Contient> produits) {
        this.produits = produits;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", status='" + status + '\'' +
                ", numTable=" + numTable +
                ", produits=" + produits +
                '}';
    }
}