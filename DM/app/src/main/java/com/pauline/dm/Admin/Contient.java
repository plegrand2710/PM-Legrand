package com.pauline.dm.Admin;

import java.io.Serializable;

public class Contient implements Serializable {
    private int idCommande;
    private int idProduit;
    private int quantite;
    private String traitement;
    private String cuissonCommande;


    public Contient(int idCommande, int idProduit, int quantite, String traitement, String cuissonCommande) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.traitement = traitement;
        this.cuissonCommande = cuissonCommande;
    }

    /*public Contient(int idCommande, int idProduit, int quantite, String traitement) {
        this(idCommande, idProduit, quantite, traitement, "0");
    }*/


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

    public String getCuissonCommande() {
        return cuissonCommande;
    }


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

    public void setCuissonCommande(String cuissonCommande) {
        this.cuissonCommande = cuissonCommande;
    }

    @Override
    public String toString() {
        return "Contient{" +
                "idCommande=" + idCommande +
                ", idProduit=" + idProduit +
                ", quantite=" + quantite +
                ", traitement='" + traitement + '\'' +
                ", cuissonCommande='" + cuissonCommande + '\'' +
                '}';
    }
}