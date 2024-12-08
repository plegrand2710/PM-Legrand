package com.pauline.dm;

public class Utilisateur {
    private int id;
    private String identifiant;
    private String mdp;
    private String role;

    public Utilisateur(int id, String identifiant, String mdp, String role) {
        this.id = id;
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMdp() {
        return mdp;
    }

    public String getRole() {
        return role;
    }
}