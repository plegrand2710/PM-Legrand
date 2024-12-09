package com.pauline.dm.Cuisine;

public class PlatAccompagnementBoisson {
    private int idProduit;
    private int idCommande;
    private String nomProduit;
    private int quantite;

    public PlatAccompagnementBoisson(int idProduit, int idCommande, String nomProduit, int quantite) {
        this.idProduit = idProduit;
        this.idCommande = idCommande;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getIdCommande() {
        return idCommande;
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

    @Override
    public String toString() {
        return "PlatAccompagnementBoisson{" +
                "idProduit=" + idProduit +
                ", idCommande=" + idCommande +
                ", nomProduit='" + nomProduit + '\'' +
                ", quantite=" + quantite +
                '}';
    }
}