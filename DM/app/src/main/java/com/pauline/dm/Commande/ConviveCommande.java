package com.pauline.dm.Commande;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConviveCommande implements Serializable {
    private List<ProduitCommande> _plats = new ArrayList<>();
    private List<ProduitCommande> _accompagnements = new ArrayList<>();
    private List<ProduitCommande> _boissons = new ArrayList<>();


    public List<ProduitCommande> getPlats() {
        return _plats;
    }

    public List<ProduitCommande> getAccompagnements() {
        return _accompagnements;
    }

    public List<ProduitCommande> getBoissons() {
        return _boissons;
    }

    public void ajouterPlat(ProduitCommande produit) {
        _plats.add(produit);
    }

    public void ajouterAccompagnement(ProduitCommande produit) {
        _accompagnements.add(produit);
    }

    public void ajouterBoisson(ProduitCommande produit) {
        _boissons.add(produit);
    }

    public List<ProduitCommande> getTousProduits() {
        List<ProduitCommande> tousProduits = new ArrayList<>();
        tousProduits.addAll(_plats);
        tousProduits.addAll(_accompagnements);
        tousProduits.addAll(_boissons);
        return tousProduits;
    }

    public void setPlats(List<ProduitCommande> plats) {
        _plats = plats;
    }

    public void setAccompagnements(List<ProduitCommande> accompagnements) {
        _accompagnements = accompagnements;
    }

    public void setBoissons(List<ProduitCommande> boissons) {
        _boissons = boissons;
    }


    public void ajouterProduit(String produit, int quantite, String cuisson) {
        ProduitCommande nouveauProduit = new ProduitCommande(produit, quantite, cuisson);

        if (produit.toLowerCase().contains("plat")) {
            ajouterPlat(nouveauProduit);
        } else if (produit.toLowerCase().contains("accompagnement")) {
            ajouterAccompagnement(nouveauProduit);
        } else if (produit.toLowerCase().contains("boisson")) {
            ajouterBoisson(nouveauProduit);
        } else {
            System.out.println("Catégorie inconnue pour le produit : " + produit);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Plats:\n");
        for (ProduitCommande plat : _plats) {
            sb.append("  - ").append(plat).append("\n");
        }

        sb.append("Accompagnements:\n");
        for (ProduitCommande accompagnement : _accompagnements) {
            sb.append("  - ").append(accompagnement).append("\n");
        }

        sb.append("Boissons:\n");
        for (ProduitCommande boisson : _boissons) {
            sb.append("  - ").append(boisson).append("\n");
        }

        return sb.toString();
    }
}