package com.pauline.dm.Commande;

import java.io.Serializable;

public class ProduitCommande implements Serializable{
    private String nom;
    private int quantite;
    private String cuisson;

    public ProduitCommande(String nom, int quantite, String cuisson) {
        this.nom = nom;
        this.quantite = quantite;
        this.cuisson = cuisson;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getCuisson() {
        return cuisson;
    }

    public void setCuisson(String cuisson) {
        this.cuisson = cuisson;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" (Quantité: ").append(quantite).append(")");
        if (cuisson != null && !cuisson.isEmpty()) {
            sb.append(" (Cuisson: ").append(cuisson).append(")");
        }
        return sb.toString();
    }
}
