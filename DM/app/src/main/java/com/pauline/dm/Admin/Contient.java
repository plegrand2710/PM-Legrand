package com.pauline.dm.Admin;

import java.io.Serializable;

public class Contient implements Serializable {
    private int idCommande;
    private int idProduit;
    private int quantite;
    private String traitement;

    public Contient(int idCommande, int idProduit, int quantite, String traitement) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.traitement = traitement;
    }

    // Getters
    public int getIdCommande() {
        return idCommande;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public String getTraitement() {
        return traitement;
    }

    // Setters
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }

    @Override
    public String toString() {
        return "Contient{" +
                "idCommande=" + idCommande +
                ", idProduit=" + idProduit +
                ", quantite=" + quantite +
                ", traitement='" + traitement + '\'' +
                '}';
    }
}