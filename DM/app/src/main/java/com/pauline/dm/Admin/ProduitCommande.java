package com.pauline.dm.Admin;

public class ProduitCommande {
    private int idCommande;
    private int idProduit;
    private String nomProduit;
    private int quantite;

    public ProduitCommande(int idCommande, int idProduit, String nomProduit, int quantite) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}