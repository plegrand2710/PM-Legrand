package com.pauline.dm.Admin;

import java.io.Serializable;
import java.util.List;

public class Commande implements Serializable {
    private int idCommande;
    private String status;
    private int numTable;
    private int nbConvives;
    private List<Contient> produits;

    // Constructeur principal
    public Commande(int idCommande, String status, int numTable, List<Contient> produits) {
        this.idCommande = idCommande;
        this.status = status;
        this.numTable = numTable;
        this.produits = produits;
    }

    // Deuxième constructeur avec nbConvives
    public Commande(int idCommande, String status, int numTable, int nbConvives, List<Contient> produits) {
        this.idCommande = idCommande;
        this.status = status;
        this.numTable = numTable;
        this.nbConvives = nbConvives;
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

    public int getNbConvives() {
        return nbConvives;
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

    public void setNbConvives(int nbConvives) {
        this.nbConvives = nbConvives;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", status='" + status + '\'' +
                ", numTable=" + numTable +
                ", nbConvives=" + nbConvives +
                ", produits=" + produits +
                '}';
    }
}